package us.myles_selim.newmagicmod;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.myles_selim.newmagicmod.ingredients.SpellIngredient;

public class BlockSpellCauldron extends BlockContainer {

	public static final PropertyBool IS_FULL = PropertyBool.create("is_full");

	protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D,
			1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D,
			1.0D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D,
			1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D,
			1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D,
			1.0D, 1.0D);

	protected BlockSpellCauldron() {
		super(Material.IRON, MapColor.STONE);
		this.setRegistryName("spell_cauldron");
		this.setUnlocalizedName(NewMagicMod.MOD_ID + ":spell_cauldron");
		this.setDefaultState(getStateFromMeta(0));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSpellCauldron();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileSpellCauldron) || !((TileSpellCauldron) te).isBoiling())
			return;
		double x = (double) pos.getX() + 0.5D;
		double y = (double) pos.getY() + 0.25D;
		double z = (double) pos.getZ() + 0.5D;
		// System.out.println(Minecraft.getMinecraft().gameSettings.particleSetting);

		Minecraft mc = Minecraft.getMinecraft();
		for (float offX = -0.3f; offX < 0.3f; offX += (rand.nextFloat() / 2))
			for (float offZ = -0.3f; offZ < 0.3f; offZ += (rand.nextFloat() / 2))
				for (float offY = 0.0f; offY < 0.75f; offY += (rand.nextFloat() / 2))
					if (rand.nextBoolean()
							&& rand.nextFloat() > ((mc.gameSettings.particleSetting + 1) * 0.15f))
						worldIn.spawnParticle(ModRegistry.Particles.COLORED_BUBBLE_PARTICLE, x + offX,
								y + offY, z + offZ, rand.nextFloat() / 10, 0.25D - rand.nextFloat() / 4,
								rand.nextFloat() / 10, ((TileSpellCauldron) te).getWaterColor());
		// worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D,
		// 0.0D, 0.0D);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY,
			float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		TileSpellCauldron cauldron = (TileSpellCauldron) worldIn.getTileEntity(pos);
		if (worldIn.isRemote)
			for (SpellIngredient ing : cauldron.getIngredients())
				playerIn.sendStatusMessage(new TextComponentString(ing.getRegistryName().toString()),
						false);

		if (stack.isEmpty())
			return true;
		else {
			if (hasWaterBucket(stack)) {
				if (!state.getValue(IS_FULL) && !worldIn.isRemote) {
					if (!playerIn.capabilities.isCreativeMode)
						playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
					playerIn.addStat(StatList.CAULDRON_FILLED);
					state.withProperty(IS_FULL, true);
					worldIn.setBlockState(pos, state.withProperty(IS_FULL, true), 2);
					worldIn.playSound((EntityPlayer) null, pos, SoundEvents.ITEM_BUCKET_EMPTY,
							SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				return true;
			}
		}
		return false;
	}

	private static boolean hasWaterBucket(ItemStack stack) {
		if (!stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
			return false;
		IFluidHandlerItem fluid = stack
				.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		boolean hasWater = false;
		for (IFluidTankProperties t : fluid.getTankProperties()) {
			if (t.canDrainFluidType(new FluidStack(FluidRegistry.WATER, 1000)) && t.getContents() != null
					&& t.getContents().amount >= 1000) {
				hasWater = true;
				break;
			}
		}
		return hasWater;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
			AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn,
			boolean isActualState) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(IS_FULL, meta > 0 ? true : false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (state.getValue(IS_FULL))
			return 1;
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { IS_FULL });
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos,
			EnumFacing face) {
		if (face == EnumFacing.UP)
			return BlockFaceShape.BOWL;
		else
			return face == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
	}

}
