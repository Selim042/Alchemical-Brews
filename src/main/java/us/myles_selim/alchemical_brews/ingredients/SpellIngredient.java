package us.myles_selim.alchemical_brews.ingredients;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;

public abstract class SpellIngredient extends IForgeRegistryEntry.Impl<SpellIngredient> {

	public SpellIngredient(String name) {
		this.setRegistryName(name);
	}

	public SpellIngredient(ResourceLocation name) {
		this.setRegistryName(name);
	}

	public SpellIngredient(String modID, String name) {
		this.setRegistryName(modID, name);
	}

	public abstract int getIngredientColor();

	public boolean isStrict() {
		return true;
	}

	public void onCraft(TileBrewingCauldron cauldron, IngredientStack stack) {}

	public boolean equals(IngredientStack stackA, IngredientStack stackB) {
		return stackA.getIngredient().equals(stackB.getIngredient());
	}

}
