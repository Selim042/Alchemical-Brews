package us.myles_selim.alchemical_brews.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {}

	public void init(FMLInitializationEvent event) {}

	public void postInit(FMLPostInitializationEvent event) {}

	public int getAverageTextureColor(String oreDict) {
		return 0x777777;
	}

	public int getAverageTextureColor(IBlockState state) {
		return 0x777777;
	}

	public int getAverageTextureColor(ItemStack stack) {
		return 0x777777;
	}

}
