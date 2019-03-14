package us.myles_selim.alchemical_brews.ingredients;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import us.myles_selim.alchemical_brews.ModRegistry;

public final class IngredientStack {

	private final SpellIngredient ingredient;
	private NBTTagCompound nbt;

	public IngredientStack(SpellIngredient ing) {
		this.ingredient = ing;
		nbt = new NBTTagCompound();
	}

	public IngredientStack(IngredientStack stack) {
		this.ingredient = stack.ingredient;
		this.nbt = stack.nbt.copy();
	}

	public IngredientStack(NBTTagCompound nbt) {
		ResourceLocation loc = new ResourceLocation(nbt.getString("ingredient"));
		this.ingredient = ModRegistry.ModRegistries.SPELL_INGREDIENTS.getValue(loc);
		if (this.ingredient == null)
			throw new IllegalArgumentException("ingredient with registry name " + loc + " not found");
		this.nbt = nbt.getCompoundTag("data");
	}

	public SpellIngredient getIngredient() {
		return this.ingredient;
	}

	public NBTTagCompound getTag() {
		return this.nbt;
	}

	public void setTag(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	public final NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("ingredient", ingredient.getRegistryName().toString());
		if (this.nbt != null)
			nbt.setTag("data", this.nbt);
		return nbt;
	}

	public boolean equalsPrecise(Object obj) {
		if (!(obj instanceof IngredientStack))
			return false;
		IngredientStack stack = (IngredientStack) obj;
		return this.ingredient.equalsPrecise(this, stack) && stack.ingredient.equalsPrecise(this, stack);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IngredientStack))
			return false;
		IngredientStack stack = (IngredientStack) obj;
		return this.ingredient.equals(this, stack);
	}

}
