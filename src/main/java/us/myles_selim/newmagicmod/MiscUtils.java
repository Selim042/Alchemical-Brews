package us.myles_selim.newmagicmod;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MiscUtils {

	public static boolean isFullMoon(World world) {
		return world.getCurrentMoonPhaseFactor() > 0.75f && !world.isDaytime();
	}

	public static BlockPos nbtToBlockPos(NBTTagCompound nbt) {
		return new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}

	public static NBTTagCompound blockPosToNBT(BlockPos pos) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		return nbt;
	}

}
