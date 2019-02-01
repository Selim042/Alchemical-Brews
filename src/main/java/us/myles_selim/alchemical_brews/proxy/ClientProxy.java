package us.myles_selim.alchemical_brews.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.AlchemicalBrews;
import us.myles_selim.alchemical_brews.ModRegistry;
import us.myles_selim.alchemical_brews.blocks.tiles.TileSpellCauldron;
import us.myles_selim.alchemical_brews.particles.ParticleColoredBubble;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
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
		}, ModRegistry.ModBlocks.SPELL_CAULDRON);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(ModRegistry.ModItems.SPELL_CAULDRON, 0,
				new ModelResourceLocation(ModRegistry.ModItems.SPELL_CAULDRON.getRegistryName(),
						"inventory"));
		ModelLoader.setCustomModelResourceLocation(ModRegistry.ModItems.SPAWN_POTION, 0,
				new ModelResourceLocation(ModRegistry.ModItems.SPAWN_POTION.getRegistryName(),
						"inventory"));
		ModelLoader.setCustomModelResourceLocation(ModRegistry.ModItems.GARDENING_LAMP, 0,
				new ModelResourceLocation(ModRegistry.ModItems.GARDENING_LAMP.getRegistryName(),
						"inventory"));
	}

	public static void registerParticles() {
		int id = getNextParticleId();
		ModRegistry.ModParticles.COLORED_BUBBLE_PARTICLE = EnumHelper.addEnum(EnumParticleTypes.class,
				AlchemicalBrews.MOD_ID + "COLORED_BUBBLE_PARTICLE",
				new Class[] { String.class, int.class, boolean.class, int.class },
				AlchemicalBrews.MOD_ID + ":colored_bubble_particle", id, false, 1);
		Minecraft.getMinecraft().effectRenderer.registerParticle(id,
				new ParticleColoredBubble.Factory());
	}

	private static int particleId;

	private static int getNextParticleId() {
		while (EnumParticleTypes.getParticleFromId(particleId) != null)
			particleId++;
		return particleId;
	}

}
