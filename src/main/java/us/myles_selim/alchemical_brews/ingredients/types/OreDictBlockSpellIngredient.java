package us.myles_selim.alchemical_brews.ingredients.types;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.oredict.OreDictionary;
import us.myles_selim.alchemical_brews.AlchemicalBrews;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;
import us.myles_selim.alchemical_brews.utils.MiscUtils;

public class OreDictBlockSpellIngredient extends SpellIngredient {

	private static final Map<String, OreDictBlockSpellIngredient> DICT_MAP = new HashMap<>();

	private final String oreDict;
	private final int color;

	public OreDictBlockSpellIngredient(String oreDict, int color) {
		DICT_MAP.put(oreDict, this);
		this.oreDict = oreDict;
		this.color = color;
	}

	public String getOre() {
		return this.oreDict;
	}

	@Override
	public boolean isStrict() {
		return false;
	}

	@Override
	public int getIngredientColor() {
		// return this.color;
		return AlchemicalBrews.proxy.getAverageTextureColor(oreDict);
	}

	@Override
	public void onCraft(TileBrewingCauldron cauldron, IngredientStack stack, boolean used) {
		BlockPos pos = getPos(stack);
		if (pos == null || !used)
			return;
		IBlockState state = cauldron.getWorld().getBlockState(pos);
		if (MiscUtils.arrCont(OreDictionary.getOreIDs(state.getBlock().getPickBlock(state,
				new RayTraceResult(new Vec3d(pos), EnumFacing.UP, pos), cauldron.getWorld(), pos, null)),
				OreDictionary.getOreID(this.getOre())))
			cauldron.getWorld().destroyBlock(pos, false);
	}

	public BlockPos getPos(IngredientStack stack) {
		NBTTagCompound posNbt = stack.getTag().getCompoundTag("pos");
		if (posNbt == null)
			return null;
		return new BlockPos(posNbt.getInteger("x"), posNbt.getInteger("y"), posNbt.getInteger("z"));
	}

	public void setPos(IngredientStack stack, BlockPos pos) {
		NBTTagCompound posNbt = new NBTTagCompound();
		posNbt.setInteger("x", pos.getX());
		posNbt.setInteger("y", pos.getY());
		posNbt.setInteger("z", pos.getZ());
		stack.getTag().setTag("pos", posNbt);
	}

	@Override
	public boolean equalsPrecise(IngredientStack stackA, IngredientStack stackB) {
		return super.equals(stackA, stackB);
	}

	public static boolean isOreDictBlockSpellIngredient(IBlockState state) {
		ItemStack stack = new ItemStack(state.getBlock());
		if (stack.isEmpty())
			return false;
		for (int id : OreDictionary.getOreIDs(stack))
			if (DICT_MAP.containsKey(OreDictionary.getOreName(id)))
				return true;
		return false;
	}

	public static OreDictBlockSpellIngredient getIngredient(IBlockState state) {
		ItemStack stack = new ItemStack(state.getBlock());
		for (int id : OreDictionary.getOreIDs(stack))
			if (DICT_MAP.containsKey(OreDictionary.getOreName(id)))
				return DICT_MAP.get(OreDictionary.getOreName(id));
		return null;
	}

	public static boolean isOreDictBlockSpellIngredient(String oreDict) {
		return DICT_MAP.containsKey(oreDict);
	}

	public static OreDictBlockSpellIngredient getOreDictIngredient(String oreDict) {
		if (!isOreDictBlockSpellIngredient(oreDict))
			return null;
		return DICT_MAP.get(oreDict);
	}

}
