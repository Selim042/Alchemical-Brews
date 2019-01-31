package us.myles_selim.newmagicmod.ingredients.stack.special;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.newmagicmod.NewMagicMod;
import us.myles_selim.newmagicmod.ingredients.SpecialItemHandler;
import us.myles_selim.newmagicmod.utils.MiscUtils;

@Mod.EventBusSubscriber
public class TripleMoonEggIngredient extends SpecialStackSpellIngredient {

	private static final String MOON_COUNTER = NewMagicMod.MOD_ID + "_egg_moons";
	private static final String MOON_TIMER = NewMagicMod.MOD_ID + "_egg_time";

	public TripleMoonEggIngredient() {
		super("triple_moon_egg");
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

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		// TODO: localize
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return;
		tooltip.add("An egg bathed in the light of " + nbt.getInteger(MOON_COUNTER) + " full moons");
	}

	@Override
	public int getIngredientColor() {
		return 0xFFFDD0;
	}

	@Override
	public boolean matchesStack(ItemStack stack) {
		return stack.hasTagCompound()
				&& stack.getTagCompound().getString(SpecialStackSpellIngredient.INGREDIENT_KEY)
						.equals(this.getRegistryName().toString());
	}

	public static class TripleleEggItemHandler extends SpecialItemHandler {

		public TripleleEggItemHandler() {
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
