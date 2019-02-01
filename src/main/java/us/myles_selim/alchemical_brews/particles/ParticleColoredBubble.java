package us.myles_selim.alchemical_brews.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.utils.ColorUtils;

@SideOnly(Side.CLIENT)
public class ParticleColoredBubble extends Particle {

	protected ParticleColoredBubble(World world, double xCoord, double yCoord, double zCoord,
			double xSpeed, double ySpeed, double zSpeed) {
		this(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, 1.0f, 1.0f, 1.0f);
	}

	protected ParticleColoredBubble(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
			double xSpeedIn, double ySpeedIn, double zSpeedIn, float[] color) {
		this(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, color[0], color[1],
				color[2]);
	}

	protected ParticleColoredBubble(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
			double xSpeedIn, double ySpeedIn, double zSpeedIn, float r, float g, float b) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.particleRed = r;
		this.particleGreen = g;
		this.particleBlue = b - 1.0f;
		this.setParticleTextureIndex(32);
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
		this.motionX = xSpeedIn * 0.20000000298023224D
				+ (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
		this.motionY = ySpeedIn * 0.20000000298023224D
				+ (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
		this.motionZ = zSpeedIn * 0.20000000298023224D
				+ (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY += 0.002D;
		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.8500000238418579D;
		this.motionY *= 0.8500000238418579D;
		this.motionZ *= 0.8500000238418579D;

		// if (this.world.getBlockState(new BlockPos(this.posX, this.posY,
		// this.posZ))
		// .getMaterial() != Material.WATER) {
		// this.setExpired();
		// }

		if (this.particleMaxAge-- <= 0)
			this.setExpired();
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {

		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn,
				double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... args) {
			if (args.length < 1)
				return new ParticleColoredBubble(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn,
						ySpeedIn, zSpeedIn);
			return new ParticleColoredBubble(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn,
					zSpeedIn, ColorUtils.rgbToIndFloats(args[0]));
		}
	}

}