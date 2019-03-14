package us.myles_selim.alchemical_brews.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.IngredientList;

public interface ISpellRecipe extends IForgeRegistryEntry<ISpellRecipe> {

	public boolean matchesCatalyst(ItemStack stack);

	public IngredientList getIngredients();

	public default boolean matches(IngredientList ingredients) {
		IngredientList ings = new IngredientList(getIngredients());
		ings.removeAllPrecise(ingredients);
		return ings.isEmpty();
	}

	public void executeResult(World world, EntityPlayer player, BlockPos pos, IngredientList ings);

}
