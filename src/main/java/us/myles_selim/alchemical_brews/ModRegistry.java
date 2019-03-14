package us.myles_selim.alchemical_brews;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import us.myles_selim.alchemical_brews.blocks.BlockBrewingCauldron;
import us.myles_selim.alchemical_brews.blocks.BlockGardeningLamp;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.SpecialItemHandler;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.ChickenFeatherIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.FriedNetherCocoaIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.FullMoonGunpowderIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.JebWoolIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.StrayBoneIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.TripleMoonEggIngredient;
import us.myles_selim.alchemical_brews.ingredients.types.BlockSpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.types.OreDictBlockSpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.types.SpecialStackSpellIngredient;
import us.myles_selim.alchemical_brews.items.ItemHeartyBeetroot;
import us.myles_selim.alchemical_brews.items.ItemSpawnPotion;
import us.myles_selim.alchemical_brews.recipes.BiomeSpellRecipe;
import us.myles_selim.alchemical_brews.recipes.ISpellRecipe;
import us.myles_selim.alchemical_brews.recipes.SpawnerSpellRecipe;
import us.myles_selim.alchemical_brews.recipes.StackSpellRecipe;
import us.myles_selim.alchemical_brews.utils.MiscUtils;

@Mod.EventBusSubscriber
public class ModRegistry {

	public static class ModRegistries {

		public static IForgeRegistry<SpellIngredient> SPELL_INGREDIENTS;
		public static IForgeRegistry<SpecialItemHandler> SPECIAL_ITEM_HANDLERS;
		public static IForgeRegistry<ISpellRecipe> SPELL_RECIPES;

	}

	@GameRegistry.ObjectHolder(AlchemicalConstants.MOD_ID)
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

		// public static final BlockSpellIngredient DIAMOND_ORE = new
		// BlockSpellIngredient("diamond_ore",
		// Blocks.DIAMOND_ORE.getDefaultState(), 0x3BD6C6);
		public static final SpellIngredient SPAWNER = new BlockSpellIngredient(
				Blocks.MOB_SPAWNER.getDefaultState(), 0x778899).setRegistryName("spawner");
	}

	@GameRegistry.ObjectHolder(AlchemicalConstants.MOD_ID)
	public static class ModBlocks {

		public static final Block SPELL_CAULDRON = new BlockBrewingCauldron();
		public static final Block GARDENING_LAMP = new BlockGardeningLamp();

	}

	@GameRegistry.ObjectHolder(AlchemicalConstants.MOD_ID)
	public static class ModItems {

		public static final ItemBlock SPELL_CAULDRON = null;
		public static final Item SPAWN_POTION = new ItemSpawnPotion();
		public static final ItemBlock GARDENING_LAMP = null;
		public static final Item HEARTY_BEETROOT = new ItemHeartyBeetroot();

	}

	public static class ModParticles {

		public static EnumParticleTypes COLORED_BUBBLE_PARTICLE;

	}

	@SubscribeEvent
	public static void registerCommonRegistries(RegistryEvent.NewRegistry event) {
		ModRegistries.SPELL_INGREDIENTS = new RegistryBuilder<SpellIngredient>()
				.setType(SpellIngredient.class)
				.setName(new ResourceLocation(AlchemicalConstants.MOD_ID, "spell_ingredients")).create();
		ModRegistries.SPECIAL_ITEM_HANDLERS = new RegistryBuilder<SpecialItemHandler>()
				.setType(SpecialItemHandler.class)
				.setName(new ResourceLocation(AlchemicalConstants.MOD_ID, "special_item_handlers"))
				.create();
		ModRegistries.SPELL_RECIPES = new RegistryBuilder<ISpellRecipe>().setType(ISpellRecipe.class)
				.setName(new ResourceLocation(AlchemicalConstants.MOD_ID, "spell_recipes")).create();
	}

	private static final Map<String, SpellIngredient> ORE_DICT_INGREDIENTS = new HashMap<>();

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

		// registry.register(ModIngredients.DIAMOND_ORE);
		registry.register(ModIngredients.SPAWNER);

		for (OreSet set : OreSet.getOreSets()) {
			// TODO: auto decide color
			SpellIngredient ing = new OreDictBlockSpellIngredient(set.ore, 0xFFFFFF)
					.setRegistryName(set.name + "_ore");
			registry.register(ing);
			ORE_DICT_INGREDIENTS.put(set.name, ing);
		}
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

		// registry.register(new BlockSpellRecipe(new ItemStack(Items.DIAMOND),
		// new IngredientStack(ORE_DICT_INGREDIENTS.get("diamond")),
		// Blocks.DIAMOND_BLOCK.getDefaultState(),
		// new IngredientStack(ModIngredients.FULL_MOON_GUNPOWDER))
		// .setRegistryName("diamond_block"));
		registry.register(new SpawnerSpellRecipe(new ItemStack(Items.PORKCHOP),
				new ResourceLocation("minecraft", "pig")).setRegistryName("pig_spawner"));

		registry.register(new BiomeSpellRecipe(new ItemStack(Blocks.SAPLING, 1, 3), Biomes.JUNGLE)
				.setRegistryName("jungle_biome"));
		registry.register(new BiomeSpellRecipe(new ItemStack(Blocks.SAPLING), Biomes.FOREST)
				.setRegistryName("forest_biome"));
		registry.register(new BiomeSpellRecipe(new ItemStack(Blocks.SAPLING, 1, 1), Biomes.BIRCH_FOREST)
				.setRegistryName("birch_forest_biome"));
		registry.register(new BiomeSpellRecipe(new ItemStack(Blocks.SAND), Biomes.DESERT)
				.setRegistryName("desert_biome"));

		for (OreSet set : OreSet.getOreSets()) {
			registry.register(new ISpellRecipe() {

				@Override
				public ISpellRecipe setRegistryName(ResourceLocation name) {
					return this;
				}

				@Override
				public ResourceLocation getRegistryName() {
					return new ResourceLocation(AlchemicalConstants.MOD_ID, "ore_block_" + set.name);
				}

				@Override
				public Class<ISpellRecipe> getRegistryType() {
					return ISpellRecipe.class;
				}

				@Override
				public boolean matchesCatalyst(ItemStack stack) {
					return MiscUtils.arrCont(OreDictionary.getOreIDs(stack),
							OreDictionary.getOreID(set.ingot));
				}

				@Override
				public IngredientList getIngredients() {
					IngredientList list = new IngredientList();
					list.add(new IngredientStack(ORE_DICT_INGREDIENTS.get(set.name)));
					list.add(new IngredientStack(ModIngredients.FULL_MOON_GUNPOWDER));
					list.add(new IngredientStack(ModIngredients.FULL_MOON_GUNPOWDER));
					return list;
				}

				@Override
				public void executeResult(World world, EntityPlayer player, BlockPos pos,
						IngredientList ings) {
					BlockPos orePos = null;
					String domain = null;
					for (IngredientStack stack : ings) {
						if (stack.getIngredient() instanceof OreDictBlockSpellIngredient) {
							if (!((OreDictBlockSpellIngredient) stack.getIngredient()).getOre()
									.equals(set.ore))
								continue;
							orePos = ((OreDictBlockSpellIngredient) stack.getIngredient()).getPos(stack);
							IBlockState state = world.getBlockState(orePos);
							domain = state.getBlock().getRegistryName().getResourceDomain();
							break;
						}
					}
					if (orePos == null || domain == null)
						return;
					IBlockState newState = null;
					for (ItemStack stack : OreDictionary.getOres(set.resource)) {
						if (stack.getItem() instanceof ItemBlock && domain
								.equals(stack.getItem().getRegistryName().getResourceDomain())) {
							Block block = ((ItemBlock) stack.getItem()).getBlock();
							newState = block.getDefaultState();
							break;
						}
					}
					if (newState == null)
						return;
					world.setBlockState(orePos, newState);
					for (int i = 0; i < 10; i++)
						world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + 0.5f,
								pos.getY() + 0.75f, pos.getZ() + 0.5f, 0.25f, 0.25f, 0.25f);
				}
			});
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(ModBlocks.SPELL_CAULDRON);
		GameRegistry.registerTileEntity(TileBrewingCauldron.class,
				new ResourceLocation("spell_cauldron"));
		registry.register(ModBlocks.GARDENING_LAMP);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registerItemBlock(registry, ModBlocks.SPELL_CAULDRON);
		registry.register(ModItems.SPAWN_POTION);
		registerItemBlock(registry, ModBlocks.GARDENING_LAMP);
		registry.register(ModItems.HEARTY_BEETROOT);

		OreDictionary.registerOre("coal", Items.COAL);
	}

	private static ItemBlock registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		ItemBlock ret = new ItemBlock(block);
		ret.setRegistryName(block.getRegistryName());
		registry.register(ret);
		return ret;
	}

}