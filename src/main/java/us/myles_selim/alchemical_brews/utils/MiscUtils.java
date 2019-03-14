package us.myles_selim.alchemical_brews.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class MiscUtils {

	public static boolean isFullMoon(World world) {
		return world.getCurrentMoonPhaseFactor() > 0.75f && !world.isDaytime();
	}

	public static BlockPos nbtToBlockPos(NBTTagCompound nbt) {
		if (nbt == null)
			return new BlockPos(0, 0, 0);
		return new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}

	public static NBTTagCompound blockPosToNBT(BlockPos pos) {
		if (pos == null)
			return new NBTTagCompound();
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		return nbt;
	}

	public static <T> boolean arrCont(T[] tA, T tV) {
		for (T t : tA)
			if (t == tV || t.equals(tV))
				return true;
		return false;
	}

	public static boolean arrCont(int[] iA, int iV) {
		for (int i : iA)
			if (i == iV)
				return true;
		return false;
	}

	public static boolean hasOreDict(ItemStack stack, String ore) {
		return !stack.isEmpty() && OreDictionary.doesOreNameExist(ore)
				&& MiscUtils.arrCont(OreDictionary.getOreIDs(stack), OreDictionary.getOreID(ore));
	}

	public static boolean hasOreDict(IBlockState state, String ore) {
		ItemStack stack = new ItemStack(state.getBlock());
		return !stack.isEmpty() && OreDictionary.doesOreNameExist(ore)
				&& MiscUtils.arrCont(OreDictionary.getOreIDs(stack), OreDictionary.getOreID(ore));
	}

}
