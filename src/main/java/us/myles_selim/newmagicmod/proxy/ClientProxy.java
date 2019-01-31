package us.myles_selim.newmagicmod.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import us.myles_selim.newmagicmod.ModRegistry;
import us.myles_selim.newmagicmod.NewMagicMod;
import us.myles_selim.newmagicmod.ParticleColoredBubble;
import us.myles_selim.newmagicmod.TileSpellCauldron;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {}

	@Override
	public void init(FMLInitializationEvent event) {}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		registerParticles();
		Minecraft mc = Minecraft.getMinecraft();
		mc.getBlockColors().registerBlockColorHandler(new IBlockColor() {

			// TODO: 1.13 update this for proper water color
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos,
					int tintIndex) {
				TileEntity te = worldIn.getTileEntity(pos);
				if (te == null || !(te instanceof TileSpellCauldron))
					return 0;
				return ((TileSpellCauldron) te).getWaterColor();
			}
		}, ModRegistry.Blocks.SPELL_CAULDRON);
	}

	public static void registerParticles() {
		int id = getNextParticleId();
		ModRegistry.Particles.COLORED_BUBBLE_PARTICLE = EnumHelper.addEnum(EnumParticleTypes.class,
				NewMagicMod.MOD_ID + "COLORED_BUBBLE_PARTICLE",
				new Class[] { String.class, int.class, boolean.class, int.class },
				NewMagicMod.MOD_ID + ":colored_bubble_particle", id, false, 1);
		Minecraft.getMinecraft().effectRenderer.registerParticle(id,
				new ParticleColoredBubble.Factory());
	}

	private static int particleId;

	private static int getNextParticleId() {
		while (EnumParticleTypes.getParticleFromId(particleId) == null)
			particleId++;
		return particleId;
	}

}
