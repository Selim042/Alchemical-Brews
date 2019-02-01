package us.myles_selim.alchemical_brews;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import us.myles_selim.alchemical_brews.blocks.BlockGardeningLamp;
import us.myles_selim.alchemical_brews.blocks.BlockSpellCauldron;
import us.myles_selim.alchemical_brews.blocks.tiles.TileSpellCauldron;
import us.myles_selim.alchemical_brews.ingredients.SpecialItemHandler;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.ChickenFeatherIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.FriedNetherCocoaIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.FullMoonGunpowderIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.JebWoolIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.SpecialStackSpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.StrayBoneIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.TripleMoonEggIngredient;
import us.myles_selim.alchemical_brews.items.ItemSpawnPotion;
import us.myles_selim.alchemical_brews.recipes.ISpellRecipe;
import us.myles_selim.alchemical_brews.recipes.StackSpellRecipe;

@Mod.EventBusSubscriber
public class ModRegistry {

	public static class ModRegistries {

		public static IForgeRegistry<SpellIngredient> SPELL_INGREDIENTS;
		public static IForgeRegistry<SpecialItemHandler> SPECIAL_ITEM_HANDLERS;
		public static IForgeRegistry<ISpellRecipe> SPELL_RECIPES;

	}

	@GameRegistry.ObjectHolder(AlchemicalBrews.MOD_ID)
	public static class ModIngredients {

		public static final SpecialStackSpellIngredient FULL_MOON_GUNPOWDER = new FullMoonGunpowderIngredient();
		public static final SpecialStackSpellIngredient TRIPLE_MOON_EGG = new TripleMoonEggIngredient();
		public static final SpecialStackSpellIngredient FRIED_NETHER_COCOA = new FriedNetherCocoaIngredient();
		// public static final SpecialStackSpellIngredient MOON_FERMENTED_EYE =
		// new MoonEyeSpellIngredient();
		public static final SpecialStackSpellIngredient CHICKEN_FEATHER = new ChickenFeatherIngredient();
		// public static final SpecialStackSpellIngredient END_DIAMOND = new
		// EndDiamondIngredient();
		public static final SpecialStackSpellIngredient JEB_WOOL = new JebWoolIngredient();
		public static final SpecialStackSpellIngredient STRAY_BONE = new StrayBoneIngredient();

	}

	@GameRegistry.ObjectHolder(AlchemicalBrews.MOD_ID)
	public static class ModBlocks {

		public static final Block SPELL_CAULDRON = new BlockSpellCauldron();
		public static final Block GARDENING_LAMP = new BlockGardeningLamp();

	}

	@GameRegistry.ObjectHolder(AlchemicalBrews.MOD_ID)
	public static class ModItems {

		public static final ItemBlock SPELL_CAULDRON = null;
		public static final Item SPAWN_POTION = new ItemSpawnPotion();
		public static final ItemBlock GARDENING_LAMP = null;

	}

	public static class ModParticles {

		public static EnumParticleTypes COLORED_BUBBLE_PARTICLE;

	}

	@SubscribeEvent
	public static void registerCommonRegistries(RegistryEvent.NewRegistry event) {
		ModRegistries.SPELL_INGREDIENTS = new RegistryBuilder<SpellIngredient>()
				.setType(SpellIngredient.class)
				.setName(new ResourceLocation(AlchemicalBrews.MOD_ID, "spell_ingredients")).create();
		ModRegistries.SPECIAL_ITEM_HANDLERS = new RegistryBuilder<SpecialItemHandler>()
				.setType(SpecialItemHandler.class)
				.setName(new ResourceLocation(AlchemicalBrews.MOD_ID, "special_item_handlers")).create();
		ModRegistries.SPELL_RECIPES = new RegistryBuilder<ISpellRecipe>().setType(ISpellRecipe.class)
				.setName(new ResourceLocation(AlchemicalBrews.MOD_ID, "spell_recipes")).create();
	}

	@SubscribeEvent
	public static void registerSpellIngredients(RegistryEvent.Register<SpellIngredient> event) {
		IForgeRegistry<SpellIngredient> registry = event.getRegistry();
		registry.register(ModIngredients.FULL_MOON_GUNPOWDER);
		registry.register(ModIngredients.TRIPLE_MOON_EGG);
		registry.register(ModIngredients.FRIED_NETHER_COCOA);
		// TODO: disabled, can't get crafting to work as intended
		// registry.register(Ingredients.MOON_FERMENTED_EYE);
		registry.register(ModIngredients.CHICKEN_FEATHER);
		// TODO: disabled, can't get smelting to work as intended
		// registry.register(Ingredients.END_DIAMOND);
		registry.register(ModIngredients.JEB_WOOL);
		registry.register(ModIngredients.STRAY_BONE);
	}

	@SubscribeEvent
	public static void registerSpecialHandlers(RegistryEvent.Register<SpecialItemHandler> event) {
		IForgeRegistry<SpecialItemHandler> registry = event.getRegistry();
		registry.register(new FriedNetherCocoaIngredient.FriedNetherCocoaItemHandler());
		registry.register(new TripleMoonEggIngredient.TripleleEggItemHandler());
	}

	@SubscribeEvent
	public static void registerSpellRecipes(RegistryEvent.Register<ISpellRecipe> event) {
		System.out.println("registering spell recipes");
		IForgeRegistry<ISpellRecipe> registry = event.getRegistry();
		registry.register(new StackSpellRecipe(new ItemStack(ModItems.SPAWN_POTION),
				new ItemStack(Items.GLASS_BOTTLE), ModIngredients.CHICKEN_FEATHER,
				ModIngredients.CHICKEN_FEATHER, ModIngredients.JEB_WOOL)
						.setRegistryName("spawn_potion"));
		registry.register(new StackSpellRecipe(new ItemStack(ModItems.GARDENING_LAMP),
				new ItemStack(Blocks.REDSTONE_LAMP), ModIngredients.STRAY_BONE,
				ModIngredients.STRAY_BONE, ModIngredients.STRAY_BONE).setRegistryName("gardening_lamp"));
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(ModBlocks.SPELL_CAULDRON);
		GameRegistry.registerTileEntity(TileSpellCauldron.class, new ResourceLocation("spell_cauldron"));
		registry.register(ModBlocks.GARDENING_LAMP);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registerItemBlock(registry, ModBlocks.SPELL_CAULDRON);
		registry.register(ModItems.SPAWN_POTION);
		registerItemBlock(registry, ModBlocks.GARDENING_LAMP);
	}

	private static ItemBlock registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		ItemBlock ret = new ItemBlock(block);
		ret.setRegistryName(block.getRegistryName());
		registry.register(ret);
		return ret;
	}

}