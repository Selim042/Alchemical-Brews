package us.myles_selim.newmagicmod.capability;

import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import us.myles_selim.newmagicmod.ingredients.SpellIngredient;

public interface IAdvancedCauldronCapability extends INBTSerializable<NBTTagCompound> {

	public default int getCapacity() {
		return 1000;
	}

	public int getAmount();

	public BlockPos getWaterSource();

	public List<SpellIngredient> getIngredients();

	static boolean registered = false;

	@CapabilityInject(IAdvancedCauldronCapability.class)
	public static Capability<IAdvancedCauldronCapability> BACKPACK_HANDLER_CAPABILITY = null;

	public static void register() {
		if (registered)
			return;
		CapabilityManager.INSTANCE.register(IAdvancedCauldronCapability.class,
				new Capability.IStorage<IAdvancedCauldronCapability>() {

					@Override
					public NBTBase writeNBT(Capability<IAdvancedCauldronCapability> capability,
							IAdvancedCauldronCapability instance, EnumFacing side) {
						return instance.serializeNBT();
					}

					@Override
					public void readNBT(Capability<IAdvancedCauldronCapability> capability,
							IAdvancedCauldronCapability instance, EnumFacing side, NBTBase base) {
						if (!(base instanceof NBTTagCompound))
							throw new IllegalArgumentException("only accepts NBTTagCompounds");
						instance.deserializeNBT((NBTTagCompound) base);
					}
				}, AdvancedCauldronCapabilityHandler::new);
		// CapabilityContainerListenerManager.registerListenerFactory(ContainerListenerBackpack::new);
	}

}
