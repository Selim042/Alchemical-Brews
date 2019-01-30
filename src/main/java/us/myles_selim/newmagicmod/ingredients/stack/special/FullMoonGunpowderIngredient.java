package us.myles_selim.newmagicmod.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.myles_selim.newmagicmod.MiscUtils;
import us.myles_selim.newmagicmod.ModRegistry;

@Mod.EventBusSubscriber
public class FullMoonGunpowderIngredient extends SpecialStackSpellIngredient {

	public FullMoonGunpowderIngredient() {
		super("full_moon_gunpowder");
	}

	@Override
	public ItemStack getIngredientStack() {
		return new ItemStack(Items.GUNPOWDER);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		tooltip.add("Obtained from a creeper killed under a full moon");
	}

	@SubscribeEvent
	public static void onMobDeath(LivingDropsEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityCreeper)
			if (MiscUtils.isFullMoon(living.world))
				for (EntityItem ei : event.getDrops())
					if (ei.getItem().getItem().equals(Items.GUNPOWDER))
						ei.setItem(ModRegistry.Ingredients.FULL_MOON_GUNPOWDER.getStack());
	}

}
