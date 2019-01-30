package us.myles_selim.newmagicmod.ingredients;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SpellIngredient extends IForgeRegistryEntry.Impl<SpellIngredient> {

	public SpellIngredient(String name) {
		this.setRegistryName(name);
	}

	public SpellIngredient(ResourceLocation name) {
		this.setRegistryName(name);
	}

	public SpellIngredient(String modID, String name) {
		this.setRegistryName(modID, name);
	}

}
