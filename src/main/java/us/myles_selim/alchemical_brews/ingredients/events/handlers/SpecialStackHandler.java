package us.myles_selim.alchemical_brews.ingredients.events.handlers;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.events.CauldronIngredientUpdateEvent;
import us.myles_selim.alchemical_brews.ingredients.stack.special.SpecialStackSpellIngredient;

@Mod.EventBusSubscriber
public class SpecialStackHandler {

	@SubscribeEvent
	public static void stackPickup(CauldronIngredientUpdateEvent event) {
		TileBrewingCauldron cauldron = event.getCauldron();
		BlockPos pos = cauldron.getPos();
		for (EntityItem ei : getCaptureIngredients(cauldron.getWorld(), pos.getX(), pos.getY(),
				pos.getZ())) {
			EntityPlayer player = event.getWorld().getPlayerEntityByName(ei.getOwner());
			ItemStack stack = ei.getItem();
			if (!SpecialStackSpellIngredient.isSpecialSpellIngredient(stack))
				continue;
			SpecialStackSpellIngredient ing = SpecialStackSpellIngredient.getIngredient(stack);
			if (cauldron.checkCatalyst(player, stack)) {
				event.setChanged(true);
				return;
			}
			if (ing == null)
				return;
			for (int i = 0; i < stack.getCount(); i++)
				cauldron.addIngredient(player, ing);
			ei.setDead();
			event.setChanged(true);
			return;
		}
		return;
	}

	private static List<EntityItem> getCaptureIngredients(World worldIn, double xPos, double yPos,
			double zPos) {
		return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class,
				new AxisAlignedBB(xPos - 0.5D, yPos, zPos - 0.5D, xPos + 0.5D, yPos + 1.5D, zPos + 0.5D),
				(EntityItem ei) -> true);
	}

}
