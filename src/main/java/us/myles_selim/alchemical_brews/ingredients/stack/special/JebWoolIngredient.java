package us.myles_selim.alchemical_brews.ingredients.stack.special;

import java.util.List;
import java.util.Random;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.ModRegistry;

@Mod.EventBusSubscriber
public class JebWoolIngredient extends SpecialStackSpellIngredient {

	public JebWoolIngredient() {
		super("jeb_wool");
	}

	@Override
	public ItemStack getIngredientStack() {
		return new ItemStack(Blocks.WOOL);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		tooltip.add("Sheared from a jeb sheep");
	}

	@Override
	public int getIngredientColor() {
		return 0xaa17c4;
	}

	@SubscribeEvent
	public static void onShear(PlayerInteractEvent.EntityInteract event) {
		Entity living = event.getTarget();
		if (living instanceof EntitySheep && living.hasCustomName()
				&& "jeb_".equals(living.getCustomNameTag())) {
			EntityPlayer player = event.getEntityPlayer();
			if (player.getActiveHand() == null)
				return;
			ItemStack stack = player.getHeldItem(player.getActiveHand());
			if (stack.getItem() instanceof ItemShears) {
				event.setCanceled(true);

				IShearable target = (IShearable) living;
				BlockPos pos = new BlockPos(living.posX, living.posY, living.posZ);
				if (target.isShearable(stack, living.world, pos)) {
					List<ItemStack> drops = target.onSheared(stack, living.world, pos,
							EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

					if (living.world.isRemote)
						return;
					Random rand = new Random();
					for (ItemStack s : drops) {
						s.setTagCompound(
								ModRegistry.ModIngredients.JEB_WOOL.getStack().getTagCompound());
						EntityItem ent = living.entityDropItem(s, 1.0F);
						ent.motionY += rand.nextFloat() * 0.05F;
						ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
						ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					}
					stack.damageItem(1, (EntityLivingBase) living);
				}
			}
		}
	}

}