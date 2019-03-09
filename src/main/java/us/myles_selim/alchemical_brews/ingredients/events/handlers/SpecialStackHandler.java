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
import us.myles_selim.alchemical_brews.AlchemicalConstants;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.events.CauldronIngredientUpdateEvent;
import us.myles_selim.alchemical_brews.ingredients.types.SpecialStackSpellIngredient;

@Mod.EventBusSubscriber(modid = AlchemicalConstants.MOD_ID)
public class SpecialStackHandler {

	@SubscribeEvent
	public static void stackPickup(CauldronIngredientUpdateEvent event) {
		TileBrewingCauldron cauldron = event.getCauldron();
		BlockPos pos = cauldron.getPos();
		for (EntityItem ei : getCaptureIngredients(cauldron.getWorld(), pos.getX(), pos.getY(),
				pos.getZ())) {
			EntityPlayer player = null;
			if (ei.getOwner() != null)
				player = event.getWorld().getPlayerEntityByName(ei.getOwner());
			ItemStack stack = ei.getItem();
			if (cauldron.checkCatalyst(player, stack)) {
				ei.setDead();
				event.setChanged(true);
				return;
			}
			if (!SpecialStackSpellIngredient.isSpecialSpellIngredient(stack))
				continue;
			SpecialStackSpellIngredient ing = SpecialStackSpellIngredient.getIngredient(stack);
			if (ing == null)
				return;
			for (int i = 0; i < stack.getCount(); i++)
				cauldron.addIngredient(player, new IngredientStack(ing));
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
