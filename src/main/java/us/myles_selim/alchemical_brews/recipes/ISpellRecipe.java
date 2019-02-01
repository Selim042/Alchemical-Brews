package us.myles_selim.alchemical_brews.recipes;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;

public interface ISpellRecipe extends IForgeRegistryEntry<ISpellRecipe> {

	public ItemStack getCatalyst();

	public List<SpellIngredient> getIngredients();

	public default boolean matches(List<SpellIngredient> ingredients) {
		if (getIngredients().size() != ingredients.size())
			return false;
		for (SpellIngredient ing : getIngredients())
			if (!ingredients.contains(ing))
				return false;
		return true;
	}

	public void executeResult(World world, EntityPlayer player, BlockPos pos);

}
