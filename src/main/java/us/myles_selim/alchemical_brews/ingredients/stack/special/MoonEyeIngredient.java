package us.myles_selim.alchemical_brews.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import us.myles_selim.alchemical_brews.utils.MiscUtils;

// TODO: finish, crafting only sorta works
//@Mod.EventBusSubscriber
public class MoonEyeIngredient extends SpecialStackSpellIngredient {

	public MoonEyeIngredient() {
		super("moon_fermented_eye");
	}

	@Override
	protected ItemStack getIngredientStack() {
		return new ItemStack(Items.FERMENTED_SPIDER_EYE);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		tooltip.add("WIP Fermented under a full moon");
	}

	@Override
	public int getIngredientColor() {
		return 0x661137;
	}

	@SubscribeEvent
	public static void onCraftEvent(ItemCraftedEvent event) {
		if (!event.player.world.isRemote && MiscUtils.isFullMoon(event.player.world)
				&& event.crafting.getItem().equals(Items.FERMENTED_SPIDER_EYE)) {
			// event.crafting.setTagCompound(
			// ModRegistry.Ingredients.MOON_FERMENTED_EYE.getStack().getTagCompound());
		}
	}

}
