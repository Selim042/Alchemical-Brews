package us.myles_selim.alchemical_brews.recipes;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.IngredientList;
import us.myles_selim.alchemical_brews.ModRegistry.ModIngredients;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.types.BlockSpellIngredient;

public class SpawnerSpellRecipe extends IForgeRegistryEntry.Impl<ISpellRecipe> implements ISpellRecipe {

	private ItemStack catalyst;
	private ResourceLocation entityEntry;
	private IngredientList ings;

	public SpawnerSpellRecipe(ItemStack catalyst, ResourceLocation entityEntry,
			IngredientStack... ings) {
		this.catalyst = catalyst;
		this.entityEntry = entityEntry;
		this.ings = new IngredientList(IngredientList.asList(ings));
		this.ings.add(new IngredientStack(ModIngredients.SPAWNER));
	}

	@Override
	public boolean matchesCatalyst(ItemStack stack) {
		return ItemStack.areItemStacksEqual(stack, catalyst);
	}

	@Override
	public IngredientList getIngredients() {
		return IngredientList.unmodifiableList(this.ings);
	}

	@Override
	public void executeResult(World world, EntityPlayer player, BlockPos pos, IngredientList ings) {
		if (world.isRemote)
			return;
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
		spawner.getSpawnerBaseLogic().setEntityId(this.entityEntry);
		spawner.markDirty();
		world.markBlockRangeForRenderUpdate(spawnerPos, spawnerPos);
	}

}
