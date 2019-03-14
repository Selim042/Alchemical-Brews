package us.myles_selim.alchemical_brews.ingredients.types;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;

public class BlockSpellIngredient extends SpellIngredient {

	private static final Map<IBlockState, BlockSpellIngredient> STATE_MAP = new HashMap<>();

	private final IBlockState state;
	private final int color;

	public BlockSpellIngredient(IBlockState state, int color) {
		STATE_MAP.put(state, this);
		this.state = state;
		this.color = color;
	}

	public IBlockState getState() {
		return this.state;
	}

	@Override
	public boolean isStrict() {
		return false;
	}

	@Override
	public int getIngredientColor() {
		return this.color;
	}

	@Override
	public void onCraft(TileBrewingCauldron cauldron, IngredientStack stack, boolean used) {
		BlockPos pos = getPos(stack);
		if (pos == null || !used)
			return;
		if (cauldron.getWorld().getBlockState(pos).equals(this.getState()))
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

	public static boolean isBlockSpellIngredient(IBlockState state) {
		return STATE_MAP.containsKey(state);
	}

	public static BlockSpellIngredient getIngredient(IBlockState state) {
		if (!isBlockSpellIngredient(state))
			return null;
		return STATE_MAP.get(state);
	}

}
