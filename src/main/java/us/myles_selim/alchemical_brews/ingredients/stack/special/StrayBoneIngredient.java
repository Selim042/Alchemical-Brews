package us.myles_selim.alchemical_brews.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.ModRegistry;
import us.myles_selim.alchemical_brews.ingredients.types.SpecialStackSpellIngredient;

@Mod.EventBusSubscriber
public class StrayBoneIngredient extends SpecialStackSpellIngredient {

	public StrayBoneIngredient() {
		super("stray_bone");
	}

	@Override
	public ItemStack getIngredientStack() {
		return new ItemStack(Items.BONE);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		tooltip.add("Obtained from a stray-spider jockey");
	}

	@Override
	public int getIngredientColor() {
		return 0xe2e2e2;
	}

	@SubscribeEvent
	public static void onMobDeath(LivingDropsEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityStray)
			if (living.isRiding() && living.getRidingEntity() instanceof EntitySpider)
				for (EntityItem ei : event.getDrops())
					if (ei.getItem().getItem().equals(Items.BONE))
						ei.setItem(ModRegistry.ModIngredients.STRAY_BONE.getStack());
	}

}
