package us.myles_selim.alchemical_brews.recipes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import us.myles_selim.alchemical_brews.IngredientList;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;
import us.myles_selim.alchemical_brews.ingredients.types.BlockSpellIngredient;

public class BlockSpellRecipe extends IForgeRegistryEntry.Impl<ISpellRecipe> implements ISpellRecipe {

	private ItemStack catalyst;
	private IngredientStack replacedBlock;
	private IBlockState newState;
	private IngredientList ings;

	public BlockSpellRecipe(ItemStack catalyst, IngredientStack replacedBlock, IBlockState newState,
			IngredientStack... ings) {
		this.catalyst = catalyst;
		this.replacedBlock = replacedBlock;
		this.newState = newState;
		this.ings = new IngredientList(IngredientList.asList(ings));
		this.ings.add(replacedBlock);
	}

	@Override
	public boolean matchesCatalyst(ItemStack stack) {
		return ItemStack.areItemStacksEqual(stack, catalyst);
	}

	@Override
	public IngredientList getIngredients() {
		return IngredientList.unmodifiableList(this.ings);
	}

	@Override
	public void executeResult(World world, EntityPlayer player, BlockPos pos, IngredientList ings) {
		BlockPos orePos = null;
		for (IngredientStack stack : ings) {
			if (this.replacedBlock.equals(stack)) {
				orePos = ((BlockSpellIngredient) stack.getIngredient()).getPos(stack);
				break;
			}
		}
		if (orePos == null)
			return;
		world.setBlockState(orePos, newState);
		for (int i = 0; i < 10; i++)
			world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + 0.5f, pos.getY() + 0.5f,
					pos.getZ() + 0.5f, 0.25f, 0.25f, 0.25f);
	}

}
