package us.myles_selim.alchemical_brews.network;

import java.util.HashSet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.alchemical_brews.BiomeModifier;

public class BiomeUpdateMessage implements IMessage {

	private BlockPos[] toUpdate;
	private Biome biome;

	public BiomeUpdateMessage() {
		this.biome = null;
	}

	public BiomeUpdateMessage(World world, BlockPos pos) {
		this(world.getBiomeForCoordsBody(pos), pos);
	}

	public BiomeUpdateMessage(Biome biome, BlockPos... toUpdate) {
		this.biome = biome;
		this.toUpdate = toUpdate;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		biome = Biome.getBiome(buf.readUnsignedByte());
		int len = buf.readShort();
		toUpdate = new BlockPos[len];
		for (int i = 0; i < len; i++)
			toUpdate[i] = new BlockPos(buf.readInt(), buf.readUnsignedByte(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(Biome.getIdForBiome(biome));
		buf.writeShort(toUpdate.length);
		for (BlockPos p : toUpdate) {
			buf.writeInt(p.getX());
			buf.writeByte(p.getY());
			buf.writeInt(p.getZ());
		}
	}

	public static class BiomeUpdateMessageHandler
			implements IMessageHandler<BiomeUpdateMessage, IMessage> {

		@Override
		public IMessage onMessage(BiomeUpdateMessage message, MessageContext ctx) {
			if (ctx.side.equals(Side.CLIENT))
				clientStuff(message, ctx);
			return null;
		}

		@SideOnly(Side.CLIENT)
		private void clientStuff(BiomeUpdateMessage message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				WorldClient world = Minecraft.getMinecraft().world;
				BiomeModifier.setMultiBiome(world, message.biome, message.toUpdate);

				HashSet<ChunkPos> finishedPos = new HashSet<>();
				for (BlockPos pos : message.toUpdate) {
					if (finishedPos.add(new ChunkPos(pos))) {
						int chunkX = pos.getX() >> 4;
						int chunkZ = pos.getZ() >> 4;

						world.markBlockRangeForRenderUpdate(chunkX << 4, 0, chunkZ << 4,
								(chunkX << 4) + 15, 256, (chunkZ << 4) + 15);
					}
				}

				for (BlockPos bp : message.toUpdate) {
					for (int i = 0; i < 3 - Minecraft.getMinecraft().gameSettings.particleSetting; i++) {
						BlockPos pp = Minecraft.getMinecraft().world.getTopSolidOrLiquidBlock(bp);
						world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
								pp.getX() + 0.25f + (world.rand.nextFloat() / 2), pp.getY(),
								pp.getZ() + 0.25f + (world.rand.nextFloat() / 2),
								world.rand.nextFloat() / 10, 0.25D - world.rand.nextFloat() / 4,
								world.rand.nextFloat() / 10);
					}
				}
			});
		}

	}

}
