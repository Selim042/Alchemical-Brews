package us.myles_selim.alchemical_brews.ingredients.events.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.myles_selim.alchemical_brews.AlchemicalConstants;
import us.myles_selim.alchemical_brews.blocks.tiles.TileBrewingCauldron;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.events.CauldronIngredientUpdateEvent;
import us.myles_selim.alchemical_brews.ingredients.types.BlockSpellIngredient;

@Mod.EventBusSubscriber(modid = AlchemicalConstants.MOD_ID)
public class BlockHandler {

	@SubscribeEvent
	public static void stackPickup(CauldronIngredientUpdateEvent event) {
		TileBrewingCauldron cauldron = event.getCauldron();
		List<IngredientStack> toRemove = new ArrayList<>();
		for (IngredientStack ing : cauldron.getIngredients())
			if (ing.getIngredient() instanceof BlockSpellIngredient)
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
			IngredientStack ingStack = new IngredientStack(ing);
			NBTTagCompound posNbt = new NBTTagCompound();
			posNbt.setInteger("x", p.getX());
			posNbt.setInteger("y", p.getY());
			posNbt.setInteger("z", p.getZ());
			ingStack.getTag().setTag("pos", posNbt);
			cauldron.addIngredient(null, ingStack);
			event.setChanged(true);
		}
		return;
	}

}
