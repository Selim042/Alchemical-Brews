package us.myles_selim.alchemical_brews.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.ModRegistry;
import us.myles_selim.alchemical_brews.blocks.BlockSpellCauldron;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.SpecialStackSpellIngredient;
import us.myles_selim.alchemical_brews.recipes.ISpellRecipe;
import us.myles_selim.alchemical_brews.utils.ColorUtils;
import us.myles_selim.alchemical_brews.utils.MiscUtils;
import us.myles_selim.alchemical_brews.utils.VanillaPacketDispatcher;

public class TileSpellCauldron extends TileEntity implements ITickable {

	private final List<SpellIngredient> ingredients;
	private BlockPos waterSource;
	private int transferCooldown = -1;

	public TileSpellCauldron() {
		this.ingredients = new ArrayList<>();
	}

	public BlockPos getWaterSource() {
		return waterSource;
	}

	public List<SpellIngredient> getIngredients() {
		return ingredients;
	}

	public boolean isBoiling() {
		IBlockState state = world.getBlockState(getPos());
		return state.getValue(BlockSpellCauldron.IS_FULL)
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
			int[] color = ColorUtils.rgbToIndInts(ingredients.get(i).getIngredientColor());
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
			if (this.transferCooldown <= 0 && ingredients.size() < 16 && pickupIngredients(this)) {
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

	private static boolean pickupIngredients(TileSpellCauldron cauldron) {
		BlockPos pos = cauldron.getPos();
		for (EntityItem ei : getCaptureIngredients(cauldron.getWorld(), pos.getX(), pos.getY(),
				pos.getZ())) {
			ItemStack stack = ei.getItem();
			for (ISpellRecipe r : ModRegistry.ModRegistries.SPELL_RECIPES.getValuesCollection()) {
				if (r.matches(cauldron.ingredients) && ItemStack.areItemsEqual(r.getCatalyst(), stack)) {
					r.executeResult(cauldron.getWorld(), ei.getOwner() == null ? null
							: cauldron.getWorld().getPlayerEntityByName(ei.getOwner()), pos);
					ei.setDead();
					cauldron.ingredients.clear();
					cauldron.world.setBlockState(cauldron.pos,
							ModRegistry.ModBlocks.SPELL_CAULDRON.getDefaultState());
					return true;
				}
			}
			if (!SpecialStackSpellIngredient.isSpecialSpellIngredient(stack))
				continue;
			SpecialStackSpellIngredient ing = SpecialStackSpellIngredient.getIngredient(stack);
			if (ing == null)
				return false;
			for (int i = 0; i < stack.getCount(); i++)
				cauldron.ingredients.add(ing);
			ei.setDead();
			return true;
		}
		return false;
	}

	private static List<EntityItem> getCaptureIngredients(World worldIn, double xPos, double yPos,
			double zPos) {
		return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class,
				new AxisAlignedBB(xPos - 0.5D, yPos, zPos - 0.5D, xPos + 0.5D, yPos + 1.5D, zPos + 0.5D),
				(EntityItem ei) -> true);
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
			this.ingredients.add(ModRegistry.ModRegistries.SPELL_INGREDIENTS
					.getValue(new ResourceLocation(ings.getString(k))));
		this.transferCooldown = nbt.getInteger("transferCooldown");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("waterSource", MiscUtils.blockPosToNBT(this.waterSource));
		NBTTagCompound ings = new NBTTagCompound();
		for (int i = 0; ingredients != null && i < ingredients.size(); i++)
			if (ingredients.get(i) != null)
				ings.setString(Integer.toString(i), ingredients.get(i).getRegistryName().toString());
		nbt.setTag("ingredients", ings);
		nbt.setInteger("transferCooldown", this.transferCooldown);
		return super.writeToNBT(nbt);
	}

	@Override
	public boolean hasFastRenderer() {
		return true;
	}

}
