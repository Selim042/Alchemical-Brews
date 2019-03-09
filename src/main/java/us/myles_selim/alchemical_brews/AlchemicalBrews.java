package us.myles_selim.alchemical_brews;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import us.myles_selim.alchemical_brews.entities.EntitySpecialSpellItem;
import us.myles_selim.alchemical_brews.network.BiomeUpdateMessage;
import us.myles_selim.alchemical_brews.network.BiomeUpdateMessage.BiomeUpdateMessageHandler;
import us.myles_selim.alchemical_brews.proxy.CommonProxy;

@Mod(modid = AlchemicalConstants.MOD_ID, name = AlchemicalConstants.NAME,
		version = AlchemicalConstants.VERSION)
public class AlchemicalBrews {

	public static Logger logger;
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE
			.newSimpleChannel(AlchemicalConstants.MOD_ID);

	static {
		NETWORK.registerMessage(BiomeUpdateMessageHandler.class, BiomeUpdateMessage.class,
				AlchemicalConstants.Discriminators.BIOME_UPDATE, Side.CLIENT);
	}

	@Instance(AlchemicalConstants.MOD_ID)
	public static AlchemicalBrews instance;

	@SidedProxy(clientSide = "us.myles_selim.alchemical_brews.proxy.ClientProxy",
			serverSide = "us.myles_selim.alchemical_brews.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final StackIngCreativeTab INGREDIENT_TAB = new StackIngCreativeTab();
	public static final CreativeTabs RESULTS_TAB = new CreativeTabs(
			AlchemicalConstants.MOD_ID + "_results") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModRegistry.ModItems.SPAWN_POTION);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		logger = event.getModLog();

		EntityRegistry.registerModEntity(
				new ResourceLocation(AlchemicalConstants.MOD_ID, "special_spell_item"),
				EntitySpecialSpellItem.class, "special_spell_item", 0, instance, 64, 10, true);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
