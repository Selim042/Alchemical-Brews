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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import us.myles_selim.alchemical_brews.entities.EntitySpecialSpellItem;
import us.myles_selim.alchemical_brews.proxy.CommonProxy;

@Mod(modid = AlchemicalBrews.MOD_ID, name = AlchemicalBrews.NAME, version = AlchemicalBrews.VERSION)
public class AlchemicalBrews {

	public static final String MOD_ID = "alchemical_brews";
	public static final String NAME = "Alchemical Brews";
	public static final String VERSION = "1.0.0";
	public static Logger logger;

	@Instance(MOD_ID)
	public static AlchemicalBrews instance;

	@SidedProxy(clientSide = "us.myles_selim.alchemical_brews.proxy.ClientProxy",
			serverSide = "us.myles_selim.alchemical_brews.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final StackIngCreativeTab INGREDIENT_TAB = new StackIngCreativeTab();
	public static final CreativeTabs RESULTS_TAB = new CreativeTabs(
			AlchemicalBrews.MOD_ID + "_results") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModRegistry.ModItems.SPAWN_POTION);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		logger = event.getModLog();

		EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "special_spell_item"),
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
