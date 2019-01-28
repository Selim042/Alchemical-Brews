package us.myles_selim.newmagicmod;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

// TODO: figure out a real name for the mod and update all names here (& and mcmod.info)
@Mod(modid = NewMagicMod.MODID, name = NewMagicMod.NAME, version = NewMagicMod.VERSION)
public class NewMagicMod
{
    public static final String MODID = "new_magic_mod";
    public static final String NAME = "New Magic Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
