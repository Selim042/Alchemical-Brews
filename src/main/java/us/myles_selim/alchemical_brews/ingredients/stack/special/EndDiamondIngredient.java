package us.myles_selim.alchemical_brews.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;

//TODO: finish, crafting only sorta works
//@Mod.EventBusSubscriber
public class EndDiamondIngredient extends SpecialStackSpellIngredient {

	public EndDiamondIngredient() {
		super("end_diamond");
	}

	@Override
	protected ItemStack getIngredientStack() {
		return new ItemStack(Items.DIAMOND);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		tooltip.add("WIP A diamond smelted in the end air");
	}

	@Override
	public int getIngredientColor() {
		return 0x6dc0c6;
	}

	@SubscribeEvent
	public static void onSmeltEvent(ItemSmeltedEvent event) {
		if (!event.player.world.isRemote && event.player.world.provider.getDimension() == 1
				&& event.smelting.getItem().equals(Items.DIAMOND)) {
			// event.smelting
			// .setTagCompound(ModRegistry.Ingredients.END_DIAMOND.getStack().getTagCompound());
		}
	}

}
