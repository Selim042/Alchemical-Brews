package us.myles_selim.alchemical_brews.ingredients.events;

import net.minecraftforge.event.world.WorldEvent;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;

public class CauldronIngredientUpdateEvent extends WorldEvent {

	private final TileBrewingCauldron cauldron;
	private boolean changed;

	public CauldronIngredientUpdateEvent(TileBrewingCauldron cauldron) {
		super(cauldron.getWorld());
		this.cauldron = cauldron;
	}

	public TileBrewingCauldron getCauldron() {
		return this.cauldron;
	}

	public boolean isChanged() {
		return this.changed;
	}

	public boolean setChanged(boolean changed) {
		boolean oldVal = this.changed;
		this.changed = changed;
		return oldVal;
	}

}
