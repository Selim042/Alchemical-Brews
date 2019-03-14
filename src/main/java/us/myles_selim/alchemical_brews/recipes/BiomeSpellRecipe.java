package us.myles_selim.alchemical_brews.recipes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistryEntry;
import scala.actors.threadpool.Arrays;
import us.myles_selim.alchemical_brews.AreaUtils;
import us.myles_selim.alchemical_brews.BiomeModifier;
import us.myles_selim.alchemical_brews.IngredientList;
import us.myles_selim.alchemical_brews.TickScheduler;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;

public class BiomeSpellRecipe extends IForgeRegistryEntry.Impl<ISpellRecipe> implements ISpellRecipe {

	private ItemStack catalyst;
	private Biome biome;
	private IngredientStack[] ings;

	public BiomeSpellRecipe(ItemStack catalyst, Biome biome, IngredientStack... ings) {
		this.catalyst = catalyst;
		this.biome = biome;
		this.ings = ings;
	}

	@Override
	public boolean matchesCatalyst(ItemStack stack) {
		return ItemStack.areItemStacksEqual(stack, catalyst);
	}

	@Override
	public IngredientList getIngredients() {
		return IngredientList.asList(this.ings);
	}

	@Override
	public void executeResult(World world, EntityPlayer player, BlockPos pos, IngredientList ings) {
		if (world.isRemote)
			return;
		@SuppressWarnings("unchecked")
		List<BlockPos> poses = new LinkedList<>(Arrays.asList(AreaUtils.flatArea(pos, 16)));
		int delay = 0;
		while (!poses.isEmpty()) {
			List<BlockPos> subset = new ArrayList<>();
			for (int i = 0; i < world.rand.nextInt(8) + 16 && i < poses.size(); i++) {
				BlockPos p = poses.get(world.rand.nextInt(poses.size()));
				subset.add(p);
				poses.remove(p);
			}
			TickScheduler.scheduleTask(world, 10 * delay++,
					() -> BiomeModifier.setMultiBiome(world, biome, subset.toArray(new BlockPos[0])));
		}
	}

}
