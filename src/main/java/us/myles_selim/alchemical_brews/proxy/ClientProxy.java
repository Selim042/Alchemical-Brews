package us.myles_selim.alchemical_brews.proxy;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.oredict.OreDictionary;
import us.myles_selim.alchemical_brews.AlchemicalConstants;
import us.myles_selim.alchemical_brews.ModRegistry;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.particles.ParticleColoredBubble;
import us.myles_selim.alchemical_brews.utils.ColorUtils;

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
				if (worldIn == null || pos == null)
					return -1;
				TileEntity te = worldIn.getTileEntity(pos);
				if (te == null || !(te instanceof TileBrewingCauldron))
					return 0;
				return ((TileBrewingCauldron) te).getWaterColor();
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
				AlchemicalConstants.MOD_ID + "COLORED_BUBBLE_PARTICLE",
				new Class[] { String.class, int.class, boolean.class, int.class },
				AlchemicalConstants.MOD_ID + ":colored_bubble_particle", id, false, 1);
		Minecraft.getMinecraft().effectRenderer.registerParticle(id,
				new ParticleColoredBubble.Factory());
	}

	private static int particleId;

	private static int getNextParticleId() {
		while (EnumParticleTypes.getParticleFromId(particleId) != null)
			particleId++;
		return particleId;
	}

	@Override
	public int getAverageTextureColor(String oreDict) {
		if (!OreDictionary.doesOreNameExist(oreDict))
			return 0x777777;
		ItemStack stack = null;
		for (ItemStack s : OreDictionary.getOres(oreDict)) {
			stack = s;
			break;
		}
		if (stack == null)
			return 0x777777;
		return getAverageTextureColor(stack);
	}

	@Override
	public int getAverageTextureColor(IBlockState state) {
		TextureAtlasSprite texture = Minecraft.getMinecraft().getBlockRendererDispatcher()
				.getBlockModelShapes().getTexture(state);
		return getAverageColor(texture);
	}

	@Override
	public int getAverageTextureColor(ItemStack stack) {
		TextureAtlasSprite texture = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getParticleIcon(stack.getItem(), stack.getMetadata());
		return getAverageColor(texture);
	}

	private static int getAverageColor(TextureAtlasSprite texture) {
		int width = texture.getIconWidth();
		int height = texture.getIconHeight();
		int frames = texture.getFrameCount();

		BufferedImage img = new BufferedImage(width, height * frames, BufferedImage.TYPE_4BYTE_ABGR);
		for (int i = 0; i < frames; i++)
			img.setRGB(0, i * height, width, height, texture.getFrameTextureData(0)[0], 0, width);

		int[][] colorData = getColorMap(img, 6, 5, true);
		if (colorData != null) {
			int[] ret = new int[colorData.length];
			for (int i = 0; i < ret.length; i++)
				ret[i] = ColorUtils.invIntsToRGB(colorData[i]);
			long color = ret[0];
			for (int i = 1; i < ret.length; i++)
				color += ret[i];
			return (int) (color / ret.length);
		}
		return 0x0;
	}

	private static int[][] getColorMap(BufferedImage sourceImage, int colorCount, int quality,
			boolean ignoreWhite) {
		if (quality < 1)
			throw new IllegalArgumentException("Specified quality should be greater then 0.");

		int[][] pixelArray;
		switch (sourceImage.getType()) {
		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_4BYTE_ABGR:
			pixelArray = getPixelsFast(sourceImage, quality, ignoreWhite);
			break;
		default:
			pixelArray = getPixelsSlow(sourceImage, quality, ignoreWhite);
		}
		return pixelArray;
	}

	private static int[][] getPixelsFast(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
		DataBufferByte imageData = (DataBufferByte) sourceImage.getRaster().getDataBuffer();
		byte[] pixels = imageData.getData();
		int pixelCount = sourceImage.getWidth() * sourceImage.getHeight();

		int colorDepth;
		int type = sourceImage.getType();
		switch (type) {
		case BufferedImage.TYPE_3BYTE_BGR:
			colorDepth = 3;
			break;
		case BufferedImage.TYPE_4BYTE_ABGR:
			colorDepth = 4;
			break;
		default:
			throw new IllegalArgumentException("Unhandled type: " + type);
		}

		int expectedDataLength = pixelCount * colorDepth;
		if (expectedDataLength != pixels.length) {
			throw new IllegalArgumentException("(expectedDataLength = " + expectedDataLength
					+ ") != (pixels.length = " + pixels.length + ")");
		}

		int numRegardedPixels = (pixelCount + quality - 1) / quality;
		int numUsedPixels = 0;
		int[][] pixelArray = new int[numRegardedPixels][];
		int offset, r, g, b, a;

		switch (type) {
		case BufferedImage.TYPE_3BYTE_BGR:
			for (int i = 0; i < pixelCount; i += quality) {
				offset = i * 3;
				b = pixels[offset] & 0xFF;
				g = pixels[offset + 1] & 0xFF;
				r = pixels[offset + 2] & 0xFF;

				if (!(ignoreWhite && r > 250 && g > 250 && b > 250)) {
					pixelArray[numUsedPixels] = new int[] { r, g, b };
					numUsedPixels++;
				}
			}
			break;
		case BufferedImage.TYPE_4BYTE_ABGR:
			for (int i = 0; i < pixelCount; i += quality) {
				offset = i * 4;
				a = pixels[offset] & 0xFF;
				b = pixels[offset + 1] & 0xFF;
				g = pixels[offset + 2] & 0xFF;
				r = pixels[offset + 3] & 0xFF;

				// If pixel is mostly opaque and not white
				if (a >= 125 && !(ignoreWhite && r > 250 && g > 250 && b > 250)) {
					pixelArray[numUsedPixels] = new int[] { r, g, b };
					numUsedPixels++;
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Unhandled type: " + type);
		}

		return Arrays.copyOfRange(pixelArray, 0, numUsedPixels);
	}

	private static int[][] getPixelsSlow(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int pixelCount = width * height;
		int numRegardedPixels = (pixelCount + quality - 1) / quality;
		int numUsedPixels = 0;

		int[][] res = new int[numRegardedPixels][];
		int r, g, b;

		for (int i = 0; i < pixelCount; i += quality) {
			int row = i / width;
			int col = i % width;
			int rgb = sourceImage.getRGB(col, row);

			r = (rgb >> 16) & 0xFF;
			g = (rgb >> 8) & 0xFF;
			b = (rgb) & 0xFF;
			if (!(ignoreWhite && r > 250 && g > 250 && b > 250)) {
				res[numUsedPixels] = new int[] { r, g, b };
				numUsedPixels++;
			}
		}

		return Arrays.copyOfRange(res, 0, numUsedPixels);
	}

}
