package us.myles_selim.newmagicmod.recipes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.newmagicmod.ingredients.SpellIngredient;

public class StackSpellRecipe extends IForgeRegistryEntry.Impl<ISpellRecipe> implements ISpellRecipe {

	private ItemStack result;
	private ItemStack catalyst;
	private List<SpellIngredient> ingredients;

	public StackSpellRecipe(ItemStack result, ItemStack catalyst, SpellIngredient... ingredients) {
		this(result, catalyst, Arrays.asList(ingredients));
	}

	public StackSpellRecipe(ItemStack result, ItemStack catalyst, List<SpellIngredient> ingredients) {
		this.result = result;
		this.catalyst = catalyst;
		this.ingredients = Collections.unmodifiableList(ingredients);
	}

	@Nonnull
	public ItemStack getResult() {
		if (result == null)
			return ItemStack.EMPTY;
		return this.result;
	}

	@Nonnull
	@Override
	public ItemStack getCatalyst() {
		if (this.catalyst == null)
			return ItemStack.EMPTY;
		return this.catalyst;
	}

	@Override
	public List<SpellIngredient> getIngredients() {
		return ingredients;
	}

	@Override
	public void executeResult(World world, EntityPlayer player, BlockPos pos) {
		if (!world.isRemote)
			world.spawnEntity(new EntityItem(world, pos.getX() + 0.5d, pos.getY() + 0.25d,
					pos.getZ() + 0.5d, getResult()));
	}

}
