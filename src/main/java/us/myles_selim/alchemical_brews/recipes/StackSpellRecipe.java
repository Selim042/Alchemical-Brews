package us.myles_selim.alchemical_brews.recipes;

import javax.annotation.Nonnull;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.IngredientList;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;

public class StackSpellRecipe extends IForgeRegistryEntry.Impl<ISpellRecipe> implements ISpellRecipe {

	private ItemStack result;
	private ItemStack catalyst;
	private IngredientList ingredients;

	public StackSpellRecipe(ItemStack result, ItemStack catalyst, SpellIngredient... ingredients) {
		this.result = result;
		this.catalyst = catalyst;
		IngredientList stacks = new IngredientList();
		for (SpellIngredient ing : ingredients)
			stacks.add(new IngredientStack(ing));
		this.ingredients = IngredientList.unmodifiableList(stacks);
	}

	public StackSpellRecipe(ItemStack result, ItemStack catalyst, IngredientStack... ingredients) {
		this(result, catalyst, IngredientList.asList(ingredients));
	}

	public StackSpellRecipe(ItemStack result, ItemStack catalyst, IngredientList ingredients) {
		this.result = result;
		this.catalyst = catalyst;
		this.ingredients = ingredients;
	}

	@Nonnull
	public ItemStack getResult() {
		if (result == null)
			return ItemStack.EMPTY;
		return this.result;
	}

	@Override
	public boolean matchesCatalyst(ItemStack stack) {
		return ItemStack.areItemStacksEqual(stack, catalyst);
	}

	@Override
	public IngredientList getIngredients() {
		return ingredients;
	}

	@Override
	public void executeResult(World world, EntityPlayer player, BlockPos pos, IngredientList ings) {
		if (!world.isRemote)
			world.spawnEntity(new EntityItem(world, pos.getX() + 0.5d, pos.getY() + 0.25d,
					pos.getZ() + 0.5d, getResult()));
	}

}
