package us.myles_selim.newmagicmod.capability;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import us.myles_selim.newmagicmod.MiscUtils;
import us.myles_selim.newmagicmod.ModRegistry;
import us.myles_selim.newmagicmod.ingredients.SpellIngredient;

public class AdvancedCauldronCapabilityHandler
		implements IAdvancedCauldronCapability, INBTSerializable<NBTTagCompound> {

	private int amount = 0;
	private BlockPos waterSource;
	private List<SpellIngredient> ingredients;

	public AdvancedCauldronCapabilityHandler() {
		this.ingredients = new ArrayList<>();
	}

	@Override
	public int getAmount() {
		return this.amount;
	}

	@Override
	public BlockPos getWaterSource() {
		return this.waterSource;
	}

	@Override
	public List<SpellIngredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("amount", this.amount);
		nbt.setTag("waterSource", MiscUtils.blockPosToNBT(this.waterSource));
		NBTTagCompound ings = new NBTTagCompound();
		for (int i = 0; ingredients != null && i < ingredients.size(); i++)
			ings.setString(Integer.toString(i), ingredients.get(i).getRegistryName().toString());
		nbt.setTag("ingredients", ings);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.amount = nbt.getInteger("amount");
		this.waterSource = MiscUtils.nbtToBlockPos(nbt.getCompoundTag("waterSource"));
		this.ingredients = new ArrayList<>();
		NBTTagCompound ings = nbt.getCompoundTag("ingredients");
		for (String k : ings.getKeySet())
			this.ingredients
					.add(ModRegistry.Registries.SPELL_INGREDIENTS.getValue(new ResourceLocation(k)));
	}

}
