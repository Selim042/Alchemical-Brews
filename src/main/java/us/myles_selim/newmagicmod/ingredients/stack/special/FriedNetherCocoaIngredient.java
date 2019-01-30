package us.myles_selim.newmagicmod.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import us.myles_selim.newmagicmod.ModRegistry;
import us.myles_selim.newmagicmod.items.SpecialItemHandler;

@Mod.EventBusSubscriber
public class FriedNetherCocoaIngredient extends SpecialStackSpellIngredient {

	public FriedNetherCocoaIngredient() {
		super("fried_nether_cocoa");
	}

	@Override
	public ItemStack getIngredientStack() {
		return new ItemStack(Items.DYE, 1, 3);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		tooltip.add("Fried on a nether fire");
	}

	public static class FriedNetherCocoaItemHandler extends SpecialItemHandler {

		public FriedNetherCocoaItemHandler() {
			super(new ItemStack(Items.DYE, 1, 3));
		}

		@Override
		public boolean onBurned(EntityItem entity) {
			ItemStack stack = entity.getItem().copy();
			ItemStack newStack = ItemStack.EMPTY;
			if (stack.getItem() == Items.DYE && stack.getMetadata() == 3)
				newStack = ModRegistry.Ingredients.FRIED_NETHER_COCOA.getStack();
			entity.setItem(newStack);
			return true;
		}

	}

}
