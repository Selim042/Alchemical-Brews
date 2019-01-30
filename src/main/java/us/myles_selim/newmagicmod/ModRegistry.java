package us.myles_selim.newmagicmod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import us.myles_selim.newmagicmod.ingredients.SpellIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.FriedNetherCocoaIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.FullMoonGunpowderIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.SpecialStackSpellIngredient;
import us.myles_selim.newmagicmod.ingredients.stack.special.TrippleMoonEggIngredient;
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
		registry.register(new TrippleMoonEggIngredient());
		registry.register(new FriedNetherCocoaIngredient());
	}

	@SubscribeEvent
	public static void registerSpecialHandlers(RegistryEvent.Register<SpecialItemHandler> event) {
		IForgeRegistry<SpecialItemHandler> registry = event.getRegistry();
		registry.register(new FriedNetherCocoaIngredient.FriedNetherCocoaItemHandler());
		registry.register(new TrippleMoonEggIngredient.TrippleEggItemHandler());
	}

}