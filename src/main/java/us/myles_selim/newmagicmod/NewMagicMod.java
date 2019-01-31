package us.myles_selim.newmagicmod;

import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import us.myles_selim.newmagicmod.entities.EntitySpecialSpellItem;
import us.myles_selim.newmagicmod.proxy.CommonProxy;

// TODO: figure out a real name for the mod and update all names here (and mcmod.info)
@Mod(modid = NewMagicMod.MOD_ID, name = NewMagicMod.NAME, version = NewMagicMod.VERSION)
public class NewMagicMod {

	public static final String MOD_ID = "new_magic_mod";
	public static final String NAME = "New Magic Mod";
	public static final String VERSION = "1.0.0";
	public static Logger logger;

	@Instance(MOD_ID)
	public static NewMagicMod instance;

	@SidedProxy(clientSide = "us.myles_selim.newmagicmod.proxy.ClientProxy",
			serverSide = "us.myles_selim.newmagicmod.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final StackIngCreativeTab CREATIVE_TAB = new StackIngCreativeTab();

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
