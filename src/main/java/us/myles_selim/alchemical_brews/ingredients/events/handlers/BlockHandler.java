package us.myles_selim.alchemical_brews.ingredients.events.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.myles_selim.alchemical_brews.AlchemicalBrews;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.SpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.events.CauldronIngredientUpdateEvent;
import us.myles_selim.alchemical_brews.ingredients.types.BlockSpellIngredient;

@Mod.EventBusSubscriber(modid = AlchemicalBrews.MOD_ID)
public class BlockHandler {

	@SubscribeEvent
	public static void stackPickup(CauldronIngredientUpdateEvent event) {
		TileBrewingCauldron cauldron = event.getCauldron();
		List<SpellIngredient> toRemove = new ArrayList<>();
		for (SpellIngredient ing : cauldron.getIngredients())
			if (ing instanceof BlockSpellIngredient)
				toRemove.add(ing);
		cauldron.getIngredients().removeAll(toRemove);
		World world = cauldron.getWorld();
		BlockPos pos = cauldron.getPos();
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-8, -2, -8), pos.add(8, 2, 8))) {
			IBlockState state = world.getBlockState(p);
			if (!BlockSpellIngredient.isBlockSpellIngredient(state))
				continue;
			BlockSpellIngredient ing = BlockSpellIngredient.getIngredient(state);
			if (ing == null)
				return;
			cauldron.addIngredient(null, ing);
			event.setChanged(true);
			return;
		}
		return;
	}

}
