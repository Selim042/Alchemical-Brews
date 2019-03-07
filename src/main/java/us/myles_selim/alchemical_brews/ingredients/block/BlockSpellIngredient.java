package us.myles_selim.alchemical_brews.ingredients.block;

import net.minecraft.util.ResourceLocation;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;

// TODO: find a way to attach position of ing to this, mainly for BlockSpellIngredient#onCraft
public abstract class BlockSpellIngredient extends SpellIngredient {

	public BlockSpellIngredient(String name) {
		super(name);
	}

	public BlockSpellIngredient(ResourceLocation name) {
		super(name);
	}

	public BlockSpellIngredient(String modID, String name) {
		super(modID, name);
	}

	@Override
	public boolean isStrict() {
		return false;
	}

	@Override
	public void onCraft(TileBrewingCauldron cauldron) {}

}
