package us.myles_selim.alchemical_brews.ingredients;

import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;

public abstract class SpellIngredient extends IForgeRegistryEntry.Impl<SpellIngredient> {

	public abstract int getIngredientColor();

	public boolean isStrict() {
		return true;
	}

	public void onCraft(TileBrewingCauldron cauldron, IngredientStack stack, boolean used) {}

	public boolean equalsPrecise(IngredientStack stackA, IngredientStack stackB) {
		return stackA.getIngredient().equals(stackB.getIngredient())
				&& stackA.getTag().equals(stackB.getTag());
	}

	public boolean equals(IngredientStack stackA, IngredientStack stackB) {
		return stackA.getIngredient().equals(stackB.getIngredient());
	}

}
