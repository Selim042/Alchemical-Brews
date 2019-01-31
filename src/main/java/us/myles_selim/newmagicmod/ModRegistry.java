package us.myles_selim.newmagicmod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import us.myles_selim.newmagicmod.ingredients.SpellIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.ChickenFeatherIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.FriedNetherCocoaIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.FullMoonGunpowderIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.SpecialStackSpellIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.TripleMoonEggIngredient;
import us.myles_selim.newmagicmod.items.SpecialItemHandler;

@Mod.EventBusSubscriber
public class ModRegistry {

	public static class Registries {

		public static IForgeRegistry<SpellIngredient> SPELL_INGREDIENTS;
		public static IForgeRegistry<SpecialItemHandler> SPECIAL_ITEM_HANDLERS;

	}

	@GameRegistry.ObjectHolder(NewMagicMod.MOD_ID)
	public static class Ingredients {

		public static final SpecialStackSpellIngredient FULL_MOON_GUNPOWDER = null;
		public static final SpecialStackSpellIngredient TRIPPLE_MOON_EGG = null;
		public static final SpecialStackSpellIngredient FRIED_NETHER_COCOA = null;
		// public static final SpecialStackSpellIngredient MOON_FERMENTED_EYE =
		// null;
		public static final SpecialStackSpellIngredient CHICKEN_FEATHER = null;
		// public static final SpecialStackSpellIngredient END_DIAMOND = null;

	}

	@GameRegistry.ObjectHolder(NewMagicMod.MOD_ID)
	public static class Blocks {

		public static final Block SPELL_CAULDRON = null;

	}

	@GameRegistry.ObjectHolder(NewMagicMod.MOD_ID)
	public static class Items {}

	public static class Particles {

		public static EnumParticleTypes COLORED_BUBBLE_PARTICLE;

	}

	@SubscribeEvent
	public static void registerCommonRegistries(RegistryEvent.NewRegistry event) {
		Registries.SPELL_INGREDIENTS = new RegistryBuilder<SpellIngredient>()
				.setType(SpellIngredient.class)
				.setName(new ResourceLocation(NewMagicMod.MOD_ID, "spell_ingredients")).create();
		Registries.SPECIAL_ITEM_HANDLERS = new RegistryBuilder<SpecialItemHandler>()
				.setType(SpecialItemHandler.class)
				.setName(new ResourceLocation(NewMagicMod.MOD_ID, "special_item_handlers")).create();
	}

	@SubscribeEvent
	public static void registerSpellIngredients(RegistryEvent.Register<SpellIngredient> event) {
		IForgeRegistry<SpellIngredient> registry = event.getRegistry();
		registry.register(new FullMoonGunpowderIngredient());
		registry.register(new TripleMoonEggIngredient());
		registry.register(new FriedNetherCocoaIngredient());
		// TODO: disabled, can't get crafting to work as intended
		// registry.register(new MoonEyeSpellIngredient());
		registry.register(new ChickenFeatherIngredient());
		// TODO: disabled, can't get smelting to work as intended
		// registry.register(new EndDiamondIngredient());
	}

	@SubscribeEvent
	public static void registerSpecialHandlers(RegistryEvent.Register<SpecialItemHandler> event) {
		IForgeRegistry<SpecialItemHandler> registry = event.getRegistry();
		registry.register(new FriedNetherCocoaIngredient.FriedNetherCocoaItemHandler());
		registry.register(new TripleMoonEggIngredient.TripleleEggItemHandler());
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(new BlockSpellCauldron());
		GameRegistry.registerTileEntity(TileSpellCauldron.class, new ResourceLocation("spell_cauldron"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(new ItemBlock(Blocks.SPELL_CAULDRON)
				.setRegistryName(Blocks.SPELL_CAULDRON.getRegistryName()));
	}

}