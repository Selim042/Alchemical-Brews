package us.myles_selim.alchemical_brews;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.stack.special.SpecialStackSpellIngredient;

public class StackIngCreativeTab extends CreativeTabs {

	public StackIngCreativeTab() {
		super(AlchemicalBrews.MOD_ID + "_ingredients");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModRegistry.ModItems.SPELL_CAULDRON);
	}

	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> items) {
		for (SpellIngredient ing : ModRegistry.ModRegistries.SPELL_INGREDIENTS.getValuesCollection()) {
			if (ing instanceof SpecialStackSpellIngredient)
				items.add(((SpecialStackSpellIngredient) ing).getStack());
		}
	}

}
