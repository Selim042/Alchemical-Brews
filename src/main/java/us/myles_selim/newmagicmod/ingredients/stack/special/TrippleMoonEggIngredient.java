package us.myles_selim.newmagicmod.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import us.myles_selim.newmagicmod.MiscUtils;
import us.myles_selim.newmagicmod.NewMagicMod;
import us.myles_selim.newmagicmod.items.SpecialItemHandler;

@Mod.EventBusSubscriber
public class TrippleMoonEggIngredient extends SpecialStackSpellIngredient {

	private static final String MOON_COUNTER = NewMagicMod.MOD_ID + "_egg_moons";
	private static final String MOON_TIMER = NewMagicMod.MOD_ID + "_egg_time";

	public TrippleMoonEggIngredient() {
		super("tripple_moon_egg");
	}

	@Override
	public ItemStack getIngredientStack() {
		ItemStack stack = new ItemStack(Items.EGG);
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setInteger(MOON_COUNTER, 3);
		return stack;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return;
		tooltip.add("An egg bathed in the light of " + nbt.getInteger(MOON_COUNTER) + " full moons");
	}

	public static class TrippleEggItemHandler extends SpecialItemHandler {

		public TrippleEggItemHandler() {
			super(new ItemStack(Items.EGG));
		}

		@Override
		public boolean onTick(EntityItem entity) {
			World world = entity.world;
			if (!MiscUtils.isFullMoon(world))
				return false;
			ItemStack stack = entity.getItem();
			// System.out.println("found egg");
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt == null) {
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}
			int time = nbt.getInteger(MOON_TIMER) * 24000;
			if (time != 0 && world.getWorldTime() < time + 24000) // 1 MC day
				return false;
			int moons = nbt.getInteger(MOON_COUNTER);
			nbt.setInteger(MOON_COUNTER, ++moons);
			nbt.setInteger(MOON_TIMER, (int) (world.getWorldTime() / 24000));
			nbt.setString(SpecialStackSpellIngredient.INGREDIENT_KEY,
					NewMagicMod.MOD_ID + ":tripple_moon_egg");
			return true;
		}

	}

}
