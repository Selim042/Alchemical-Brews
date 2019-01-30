package us.myles_selim.newmagicmod.items;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SpecialItemHandler extends IForgeRegistryEntry.Impl<SpecialItemHandler> {

	private ItemStack stack;

	public SpecialItemHandler(ItemStack stack) {
		this.setRegistryName(stack.getItem().getRegistryName());
		this.stack = stack;
	}

	public boolean matchesStack(ItemStack stack) {
		return this.stack.isItemEqual(stack);
	}

	public boolean onBurned(EntityItem entity) {
		return false;
	}

	public boolean onTick(EntityItem entity) {
		return false;
	}

}
