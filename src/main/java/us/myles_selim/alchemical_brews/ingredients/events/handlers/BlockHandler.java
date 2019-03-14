package us.myles_selim.alchemical_brews.ingredients.events.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.myles_selim.alchemical_brews.AlchemicalConstants;
import us.myles_selim.alchemical_brews.IngredientList;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.events.CauldronIngredientUpdateEvent;
import us.myles_selim.alchemical_brews.ingredients.types.BlockSpellIngredient;
import us.myles_selim.alchemical_brews.ingredients.types.OreDictBlockSpellIngredient;
import us.myles_selim.alchemical_brews.utils.MiscUtils;

@Mod.EventBusSubscriber(modid = AlchemicalConstants.MOD_ID)
public class BlockHandler {

	@SubscribeEvent
	public static void stackPickup(CauldronIngredientUpdateEvent event) {
		TileBrewingCauldron cauldron = event.getCauldron();
		IngredientList ings = cauldron.getIngredients();
		World world = cauldron.getWorld();
		Map<BlockPos, IngredientStack> bIng = new HashMap<>();
		for (IngredientStack ing : cauldron.getIngredients()) {
			if (ing.getIngredient() instanceof BlockSpellIngredient) {
				BlockSpellIngredient bsi = (BlockSpellIngredient) ing.getIngredient();
				bIng.put(bsi.getPos(ing), ing);
			}
			if (ing.getIngredient() instanceof OreDictBlockSpellIngredient) {
				OreDictBlockSpellIngredient odsi = (OreDictBlockSpellIngredient) ing.getIngredient();
				bIng.put(odsi.getPos(ing), ing);
			}
		}

		IngredientList toRemove = new IngredientList();
		for (Entry<BlockPos, IngredientStack> e : bIng.entrySet())
			if (e.getValue().getIngredient() instanceof BlockSpellIngredient
					&& !world.getBlockState(e.getKey())
							.equals(((BlockSpellIngredient) e.getValue().getIngredient()).getState()))
				toRemove.add(e.getValue());
			else if (e.getValue().getIngredient() instanceof OreDictBlockSpellIngredient
					&& !MiscUtils.hasOreDict(world.getBlockState(e.getKey()),
							((OreDictBlockSpellIngredient) e.getValue().getIngredient()).getOre()))
				toRemove.add(e.getValue());
		boolean removedStuff = ings.removeAll(toRemove);

		BlockPos pos = cauldron.getPos();
		IngredientList toAdd = new IngredientList();
		boolean addedStuff = false;
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-4, -1, -4), pos.add(4, 2, 4))) {
			IBlockState state = world.getBlockState(p);
			// SpellIngredient ing = null;
			IngredientStack ingStack = null;
			if (BlockSpellIngredient.isBlockSpellIngredient(state)) {
				BlockSpellIngredient bsi = BlockSpellIngredient.getIngredient(state);
				ingStack = new IngredientStack(bsi);
				bsi.setPos(ingStack, p);
			} else if (OreDictBlockSpellIngredient.isOreDictBlockSpellIngredient(state)) {
				OreDictBlockSpellIngredient odbsi = OreDictBlockSpellIngredient.getIngredient(state);
				ingStack = new IngredientStack(odbsi);
				odbsi.setPos(ingStack, p);
			}
			if (ingStack == null)
				continue;
			// cauldron.addIngredient(null, ingStack);
			if (bIng.put(p, ingStack) == null) {
				toAdd.add(ingStack);
				event.setChanged(true);
				addedStuff = true;
			}
		}

		if (addedStuff) {
			// for (Entry<BlockPos, IngredientStack> e : bIng.entrySet()) {
			// IngredientStack stack = e.getValue();
			for (IngredientStack stack : toAdd) {
				// boolean oldVal = stack.setExactComparison(true);
				// if (!ings.contains(stack)) {
				ings.add(stack);
				// }
				// stack.setExactComparison(oldVal);
			}
		}
		if (!event.isChanged())
			event.setChanged(removedStuff || addedStuff);
	}

}
