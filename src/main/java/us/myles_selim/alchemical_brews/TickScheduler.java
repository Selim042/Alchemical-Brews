package us.myles_selim.alchemical_brews;

import java.util.concurrent.PriorityBlockingQueue;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber(modid = AlchemicalConstants.MOD_ID)
public class TickScheduler {

	private static int nextId = 0;
	private static final PriorityBlockingQueue<Task> TASKS = new PriorityBlockingQueue<>();

	public static int scheduleTask(World world, int delay, Runnable runnable) {
		Task task = new Task(world, world.getTotalWorldTime() + delay, runnable);
		TASKS.add(task);
		return task.id;
	}

	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {
		Task task = TASKS.peek();
		if (task != null && event.world.provider.getDimension() == task.world.provider.getDimension()
				&& event.world.getTotalWorldTime() >= task.targetedTick) {
			task.runnable.run();
			try {
				TASKS.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static final class Task implements Comparable<Task> {

		public final int id;
		public final World world;
		public final long targetedTick;
		public final Runnable runnable;

		public Task(World world, long targetedTick, Runnable runnable) {
			this.id = nextId++;
			this.world = world;
			this.targetedTick = targetedTick;
			this.runnable = runnable;
		}

		@Override
		public int compareTo(Task arg0) {
			return Long.compare(targetedTick, arg0.targetedTick);
		}

	}

}
