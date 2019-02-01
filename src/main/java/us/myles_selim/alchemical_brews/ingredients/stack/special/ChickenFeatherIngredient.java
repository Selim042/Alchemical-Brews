package us.myles_selim.alchemical_brews.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.ModRegistry;

@Mod.EventBusSubscriber
public class ChickenFeatherIngredient extends SpecialStackSpellIngredient {

	public ChickenFeatherIngredient() {
		super("chicken_feather");
	}

	@Override
	public ItemStack getIngredientStack() {
		return new ItemStack(Items.FEATHER);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		tooltip.add("Obtained from a chicken killed by a skeleton");
	}

	@Override
	public int getIngredientColor() {
		return 0xF0F0F0;
	}

	@SubscribeEvent
	public static void onMobDeath(LivingDropsEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityChicken) {
			if (event.getSource().getTrueSource() != null
					&& event.getSource().getTrueSource() instanceof EntitySkeleton)
				for (EntityItem ei : event.getDrops())
					if (ei.getItem().getItem().equals(Items.FEATHER))
						ei.setItem(ModRegistry.ModIngredients.CHICKEN_FEATHER.getStack());
		}
	}

}
