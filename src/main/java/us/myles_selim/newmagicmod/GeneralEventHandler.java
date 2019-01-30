package us.myles_selim.newmagicmod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.newmagicmod.ingredients.stack.special.SpecialStackSpellIngredient;
import us.myles_selim.newmagicmod.items.SpecialItemHandler;

@Mod.EventBusSubscriber
public class GeneralEventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onTooltip(ItemTooltipEvent event) {
		if (SpecialStackSpellIngredient.isSpecialSpellIngredient(event.getItemStack())) {
			ItemStack stack = event.getItemStack();
			List<String> tooltip = event.getToolTip();
			int offset = event.getFlags().isAdvanced() ? (stack.hasTagCompound() ? 2 : 1) : 0;
			if (tooltip.get(tooltip.size() - 1).contains(Loader.instance().getIndexedModList()
					.get(stack.getItem().getRegistryName().getResourceDomain()).getName()))
				offset++;
			SpecialStackSpellIngredient ing = SpecialStackSpellIngredient.getIngredient(stack);
			if (ing == null) {
				NewMagicMod.logger.error(stack
						+ " is marked as a SpeciallStackSpellIngredient but no matching entry was found");
				return;
			}
			List<String> strings = new ArrayList<>();
			ing.addInformation(stack, event.getEntityLiving().world, strings, event.getFlags());
			event.getToolTip().addAll(tooltip.size() - offset, strings);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityAdded(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote)
			return;
		Entity entity = event.getEntity();
		if (entity instanceof EntityItem && !(entity instanceof EntitySpecialSpellItem)) {
			EntityItem entityItem = (EntityItem) entity;
			ItemStack stack = entityItem.getItem();
			ResourceLocation key = stack.getItem().getRegistryName();
			if (!stack.isEmpty() && ModRegistry.Registries.SPECIAL_ITEM_HANDLERS.containsKey(key)) {
				SpecialItemHandler handler = ModRegistry.Registries.SPECIAL_ITEM_HANDLERS.getValue(key);
				if (!handler.matchesStack(stack))
					return;
				// System.out.println("exchanged");
				EntitySpecialSpellItem newEntity = new EntitySpecialSpellItem(entityItem, handler);
				entityItem.setDead();

				int i = MathHelper.floor(newEntity.posX / 16.0D);
				int j = MathHelper.floor(newEntity.posZ / 16.0D);
				event.getWorld().getChunkFromChunkCoords(i, j).addEntity(newEntity);
				event.getWorld().loadedEntityList.add(newEntity);
				event.getWorld().onEntityAdded(newEntity);

				event.setCanceled(true);
			}
		}
	}

}
