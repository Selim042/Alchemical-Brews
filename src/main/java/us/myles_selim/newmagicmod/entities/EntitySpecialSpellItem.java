package us.myles_selim.newmagicmod.entities;

import javax.annotation.Nonnull;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import us.myles_selim.newmagicmod.items.SpecialItemHandler;

public class EntitySpecialSpellItem extends EntityItem {

	private boolean changed;
	private SpecialItemHandler handler;

	public EntitySpecialSpellItem(World world) {
		super(world);
		isImmuneToFire = true;
	}

	public EntitySpecialSpellItem(EntityItem toConvert, SpecialItemHandler handler) {
		this(toConvert.getEntityWorld());
		this.handler = handler;
		NBTTagCompound copyTag = new NBTTagCompound();
		readFromNBT(toConvert.writeToNBT(copyTag));
	}

	@Override
	public boolean isEntityInvulnerable(@Nonnull DamageSource source) {
		if (source == DamageSource.IN_FIRE && world.provider.getDimension() == -1) {
			if (handler != null && !changed)
				changed = handler.onBurned(this);
			return true;
		} else
			return super.isEntityInvulnerable(source);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (handler != null && !changed)
			changed = handler.onTick(this);
	}

	@Override
	public boolean isBurning() {
		return false;
	}

}