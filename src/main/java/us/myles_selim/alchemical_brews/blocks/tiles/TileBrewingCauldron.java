package us.myles_selim.alchemical_brews.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.IngredientList;
import us.myles_selim.alchemical_brews.ModRegistry;
import us.myles_selim.alchemical_brews.blocks.BlockBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.events.CauldronIngredientUpdateEvent;
import us.myles_selim.alchemical_brews.recipes.ISpellRecipe;
import us.myles_selim.alchemical_brews.utils.ColorUtils;
import us.myles_selim.alchemical_brews.utils.MiscUtils;
import us.myles_selim.alchemical_brews.utils.VanillaPacketDispatcher;

public class TileBrewingCauldron extends TileEntity implements ITickable {

	private final List<ItemStack> failedCatalysts = new ArrayList<>();
	private final IngredientList ingredients;
	private BlockPos waterSource;
	private int transferCooldown = -1;

	public TileBrewingCauldron() {
		this.ingredients = new IngredientList();
	}

	public BlockPos getWaterSource() {
		return waterSource;
	}

	public IngredientList getIngredients() {
		return ingredients;
	}

	public boolean checkCatalyst(EntityPlayer player, ItemStack stack) {
		if (failedCatalysts.contains(stack))
			return false;
		for (ISpellRecipe r : ModRegistry.ModRegistries.SPELL_RECIPES.getValuesCollection()) {
			if (r.matches(getIngredients()) && r.matchesCatalyst(stack)) {
				r.executeResult(getWorld(), player, pos, getIngredients());
				IngredientList unused = new IngredientList(r.getIngredients());
				for (IngredientStack ing : getIngredients()) {
					ing.getIngredient().onCraft(this, ing, unused.containsPrecise(ing));
					unused.removePrecise(ing);
				}
				getIngredients().clear();
				getWorld().setBlockState(getPos(),
						ModRegistry.ModBlocks.SPELL_CAULDRON.getDefaultState());
				return true;
			}
		}
		failedCatalysts.add(stack);
		return false;
	}

	public boolean addIngredient(EntityPlayer player, IngredientStack ing) {
		if (ingredients.add(ing)) {
			failedCatalysts.clear();
			return true;
		}
		return false;
	}

	public boolean isBoiling() {
		IBlockState state = world.getBlockState(getPos());
		return state.getValue(BlockBrewingCauldron.IS_FULL)
				&& world.getBlockState(getPos().down()).getBlock() instanceof BlockFire;
	}

	@SideOnly(Side.CLIENT)
	public int getWaterColor() {
		int[] waterColor = ColorUtils
				.rgbToIndInts(BiomeColorHelper.getGrassColorAtPos(world, pos) - 0x121200 + 0x000022);
		int r = waterColor[0];
		int g = waterColor[1];
		int b = waterColor[2];
		int numIng = 1;
		for (int i = 0; i < ingredients.size(); i++) {
			int[] color = ColorUtils
					.rgbToIndInts(ingredients.get(i).getIngredient().getIngredientColor());
			r += color[0];
			g += color[1];
			b += color[2];
			numIng++;
		}
		return ColorUtils.invIntsToRGB(r / numIng, g / numIng, b / numIng);
	}

	@Override
	public void update() {
		if (this.world != null && !this.world.isRemote && isBoiling()) {
			this.transferCooldown--;
			if (this.transferCooldown <= 0) {
				this.transferCooldown = 0;
				this.updateCauldron();
			}
		}
	}

	protected boolean updateCauldron() {
		if (this.world != null && !this.world.isRemote) {
			if (this.transferCooldown <= 0 && updateIngredients()) {
				this.transferCooldown = 8;
				this.markDirty();
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(getWorld(), getPos());
				this.getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
				return true;
			}
			return false;
		} else
			return false;
	}

	public boolean updateIngredients() {
		CauldronIngredientUpdateEvent event = new CauldronIngredientUpdateEvent(this);
		MinecraftForge.EVENT_BUS.post(event);
		return event.isChanged();
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.serializeNBT();
	}

	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		this.deserializeNBT(nbt);
		this.getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.waterSource = MiscUtils.nbtToBlockPos(nbt.getCompoundTag("waterSource"));
		this.ingredients.clear();
		NBTTagCompound ings = nbt.getCompoundTag("ingredients");
		for (String k : ings.getKeySet())
			this.ingredients.add(new IngredientStack(ings.getCompoundTag(k)));
		// ModRegistry.ModRegistries.SPELL_INGREDIENTS.getValue(new
		// ResourceLocation(ings.getString(k))))
		this.transferCooldown = nbt.getInteger("transferCooldown");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("waterSource", MiscUtils.blockPosToNBT(this.waterSource));
		NBTTagCompound ings = new NBTTagCompound();
		for (int i = 0; ingredients != null && i < ingredients.size(); i++)
			if (ingredients.get(i) != null)
				ings.setTag(Integer.toString(i), ingredients.get(i).serialize());
		// ingredients.get(i).getIngredient().getRegistryName().toString());
		nbt.setTag("ingredients", ings);
		nbt.setInteger("transferCooldown", this.transferCooldown);
		return super.writeToNBT(nbt);
	}

	@Override
	public boolean hasFastRenderer() {
		return true;
	}

}
