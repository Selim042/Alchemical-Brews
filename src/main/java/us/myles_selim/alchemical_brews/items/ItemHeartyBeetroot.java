package us.myles_selim.alchemical_brews.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import us.myles_selim.alchemical_brews.AlchemicalBrews;
import us.myles_selim.alchemical_brews.AlchemicalConstants;

public class ItemHeartyBeetroot extends ItemFood {

	public ItemHeartyBeetroot() {
		// same values as normal beetroot
		super(1, 0.6F, false);
		this.setRegistryName("hearty_beetroot");
		this.setUnlocalizedName(AlchemicalConstants.MOD_ID + ".hearty_beetroot");
		this.setCreativeTab(AlchemicalBrews.RESULTS_TAB);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if (!worldIn.isRemote)
			player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 400, 1));
	}

}
