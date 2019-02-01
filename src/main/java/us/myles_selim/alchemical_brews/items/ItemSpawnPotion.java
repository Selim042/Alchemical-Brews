package us.myles_selim.alchemical_brews.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import us.myles_selim.alchemical_brews.AlchemicalBrews;

public class ItemSpawnPotion extends Item {

	public ItemSpawnPotion() {
		this.setRegistryName("spawn_potion");
		this.setUnlocalizedName(AlchemicalBrews.MOD_ID + ".spawn_potion");
		this.setMaxStackSize(1);
		this.setCreativeTab(AlchemicalBrews.RESULTS_TAB);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer entityPlayer = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving
				: null;
		if (entityPlayer == null || !entityPlayer.capabilities.isCreativeMode)
			stack.shrink(1);
		if (entityPlayer instanceof EntityPlayerMP)
			CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityPlayer, stack);

		double oldX = entityPlayer.posX;
		double oldY = entityPlayer.posY;
		double oldZ = entityPlayer.posZ;
		BlockPos oldPos = entityPlayer.getPosition();
		if (!worldIn.isRemote && entityPlayer != null) {
			BlockPos spawnLoc = EntityPlayer.getBedSpawnLocation(worldIn, entityPlayer.getBedLocation(),
					false);
			if (spawnLoc == null)
				spawnLoc = worldIn.getTopSolidOrLiquidBlock(worldIn.getSpawnPoint());
			entityPlayer.setPositionAndUpdate(spawnLoc.getX() + 0.5f, spawnLoc.getY(),
					spawnLoc.getZ() + 0.5f);
			worldIn.playSound((EntityPlayer) null, oldPos, SoundEvents.ENTITY_ENDERMEN_TELEPORT,
					SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		if (worldIn.isRemote && entityPlayer != null) {
			for (int i = 0; i < 75 / (Minecraft.getMinecraft().gameSettings.particleSetting + 1); i++) {
				double sX = ((double) worldIn.rand.nextFloat() - 0.5D) * 0.5D;
				double sY = ((double) worldIn.rand.nextFloat() - 0.5D) * 0.5D;
				double sZ = ((double) worldIn.rand.nextFloat() - 0.5D) * 0.5D;
				int k = worldIn.rand.nextInt(2) * 2 - 1;

				if (worldIn.rand.nextBoolean())
					sZ = (double) (worldIn.rand.nextFloat() * 1.0F * (float) k);
				else
					sX = (double) (worldIn.rand.nextFloat() * 1.0F * (float) k);
				worldIn.spawnParticle(EnumParticleTypes.PORTAL, oldX, oldY, oldZ, sX, sY, sZ);
			}
		}

		if (entityPlayer != null)
			entityPlayer.addStat(StatList.getObjectUseStats(this));
		if (entityPlayer == null || !entityPlayer.capabilities.isCreativeMode) {
			if (stack.isEmpty())
				return new ItemStack(Items.GLASS_BOTTLE);
			if (entityPlayer != null)
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn,
			EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

}
