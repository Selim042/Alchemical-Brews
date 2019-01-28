package us.myles_selim.newmagicmod;

import net.minecraft.items.ItemStack;

public class SpellIngredient {
    
    public ItemStack getStack() {}
    
    public boolean matches(ItemStack stack) {
        if (getStack().equals(stack))
            return true;
        return false;
    }
    
}