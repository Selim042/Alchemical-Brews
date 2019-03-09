package us.myles_selim.alchemical_brews;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
import us.myles_selim.alchemical_brews.ingredients.types.SpecialStackSpellIngredient;
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

		public static final BlockSpellIngredient DIAMOND_ORE = new BlockSpellIngredient("diamond_ore",
				Blocks.DIAMOND_ORE.getDefaultState()) {

			@Override
			public int getIngredientColor() {
				return 0x3BD6C6;
			}
		};

		public static final BlockSpellIngredient SPAWNER = new BlockSpellIngredient("spawner",
				Blocks.MOB_SPAWNER.getDefaultState()) {

			@Override
			public int getIngredientColor() {
				return 0x778899;
			}

			@Override
			public void onCraft(TileBrewingCauldron cauldron, IngredientStack stack) {}
		};

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

		registry.register(ModIngredients.DIAMOND_ORE);
		registry.register(ModIngredients.SPAWNER);
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

		registry.register(new StackSpellRecipe(new ItemStack(Blocks.DIAMOND_BLOCK),
				new ItemStack(Items.DIAMOND), ModIngredients.DIAMOND_ORE, ModIngredients.DIAMOND_ORE)
						.setRegistryName("diamond_block"));
		registry.register(new ISpellRecipe() {

			@Override
			public ISpellRecipe setRegistryName(ResourceLocation name) {
				return this;
			}

			@Override
			public ResourceLocation getRegistryName() {
				return new ResourceLocation("pig_spawner");
			}

			@Override
			public Class<ISpellRecipe> getRegistryType() {
				return ISpellRecipe.class;
			}

			@Override
			public ItemStack getCatalyst() {
				return new ItemStack(Items.PORKCHOP);
			}

			@Override
			public List<IngredientStack> getIngredients() {
				List<IngredientStack> ings = new ArrayList<>();
				ings.add(new IngredientStack(ModIngredients.SPAWNER));
				ings.add(new IngredientStack(ModIngredients.JEB_WOOL));
				return ings;
			}

			@Override
			public void executeResult(World world, EntityPlayer player, BlockPos pos,
					List<IngredientStack> ings) {
				BlockPos spawnerPos = null;
				for (IngredientStack ing : ings)
					if (ing.getIngredient() instanceof BlockSpellIngredient
							&& ((BlockSpellIngredient) ing.getIngredient()).getState()
									.getBlock() instanceof BlockMobSpawner)
						spawnerPos = ((BlockSpellIngredient) ing.getIngredient()).getPos(ing);
				if (spawnerPos == null)
					return;
				TileEntity te = world.getTileEntity(spawnerPos);
				if (!(te instanceof TileEntityMobSpawner))
					return;
				TileEntityMobSpawner spawner = (TileEntityMobSpawner) te;
				spawner.getSpawnerBaseLogic().setEntityId(new ResourceLocation("minecraft", "pig"));
				spawner.markDirty();
				world.markBlockRangeForRenderUpdate(spawnerPos, spawnerPos);
			}
		});
		registry.register(new ISpellRecipe() {

			@Override
			public ISpellRecipe setRegistryName(ResourceLocation name) {
				return this;
			}

			@Override
			public ResourceLocation getRegistryName() {
				return new ResourceLocation("jungle_biome");
			}

			@Override
			public Class<ISpellRecipe> getRegistryType() {
				return ISpellRecipe.class;
			}

			@Override
			public ItemStack getCatalyst() {
				return new ItemStack(Blocks.LOG, 1, 3);
			}

			@Override
			public List<IngredientStack> getIngredients() {
				List<IngredientStack> ings = new ArrayList<>();
				ings.add(new IngredientStack(ModIngredients.JEB_WOOL));
				return ings;
			}

			@Override
			public void executeResult(World world, EntityPlayer player, BlockPos pos,
					List<IngredientStack> ings) {
				if (world.isRemote)
					return;
				List<BlockPos> poses = new ArrayList<>();
				for (int x = -8; x < 16; x++)
					for (int z = -8; z < 16; z++)
						poses.add(pos.add(x, 0, z));

				int delay = 0;
				while (!poses.isEmpty()) {
					List<BlockPos> subset = new ArrayList<>();
					for (int i = 0; i < world.rand.nextInt(8) + 16 && !poses.isEmpty(); i++) {
						int index = world.rand.nextInt(poses.size());
						subset.add(poses.get(index));
						poses.remove(index);
					}

					TickScheduler.scheduleTask(world, 10 * delay++, () -> {
						BiomeModifier.setMultiBiome(world, Biomes.JUNGLE,
								subset.toArray(new BlockPos[0]));
					});
				}
			}
		});
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
	}

	private static ItemBlock registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		ItemBlock ret = new ItemBlock(block);
		ret.setRegistryName(block.getRegistryName());
		registry.register(ret);
		return ret;
	}

}