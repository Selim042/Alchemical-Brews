package us.myles_selim.alchemical_brews;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class AreaUtils {

	public static BlockPos[] flatArea(BlockPos center, double range) {
		double sRange = range * range;
		List<BlockPos> poses = new ArrayList<>();
		for (int x = (int) -range; x < range; x++)
			for (int z = (int) -range; z < range; z++)
				if (center.distanceSqToCenter(center.getX() + x, center.getY(),
						center.getZ() + z) < sRange)
					poses.add(center.add(x, 0, z));
		return poses.toArray(new BlockPos[0]);
	}

	public static BlockPos[] flatRingArea(BlockPos center, double range) {
		double oRange = range * range;
		double iRange = range - 2;
		List<BlockPos> poses = new ArrayList<>();
		for (double x = -range; x <= range; x++) {
			for (double z = -range; z <= range; z++) {
				double sDist = center.distanceSqToCenter(center.getX() + x, center.getY(),
						center.getZ() + z);
				if (sDist < oRange && sDist > iRange)
					poses.add(center.add(x, 0, z));
			}
		}
		return poses.toArray(new BlockPos[0]);
	}

}
