package us.myles_selim.alchemical_brews.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;

public interface ISpellRecipe extends IForgeRegistryEntry<ISpellRecipe> {

	public ItemStack getCatalyst();

	public List<IngredientStack> getIngredients();

	public default boolean matches(List<IngredientStack> ingredients) {
		List<IngredientStack> ings = new ArrayList<>(getIngredients());
		for (IngredientStack ing : ingredients)
			ings.remove(ing);
		return ings.isEmpty();
	}

	public void executeResult(World world, EntityPlayer player, BlockPos pos,
			List<IngredientStack> ings);

}
