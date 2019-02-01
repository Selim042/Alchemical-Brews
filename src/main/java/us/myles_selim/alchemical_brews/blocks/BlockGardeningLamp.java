package us.myles_selim.alchemical_brews.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import us.myles_selim.alchemical_brews.AlchemicalBrews;

public class BlockGardeningLamp extends Block {

	public static PropertyBool IS_ON = PropertyBool.create("is_on");

	public BlockGardeningLamp() {
		super(Material.REDSTONE_LIGHT);
		this.setRegistryName("gardening_lamp");
		this.setHardness(0.3F);
		this.setUnlocalizedName(AlchemicalBrews.MOD_ID + ".gardening_lamp");
		this.setCreativeTab(AlchemicalBrews.RESULTS_TAB);
		this.setTickRandomly(true);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(IS_ON) ? 15 : 0;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			if (state.getValue(IS_ON) && !worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, getDefaultState().withProperty(IS_ON, false), 2);
			else if (!state.getValue(IS_ON) && worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, getDefaultState().withProperty(IS_ON, true), 2);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn,
			BlockPos fromPos) {
		if (!worldIn.isRemote) {
			if (state.getValue(IS_ON) && !worldIn.isBlockPowered(pos))
				worldIn.scheduleUpdate(pos, this, 4);
			else if (!state.getValue(IS_ON) && worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, getDefaultState().withProperty(IS_ON, true), 2);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			if (state.getValue(IS_ON) && !worldIn.isBlockPowered(pos))
				worldIn.setBlockState(pos, getDefaultState().withProperty(IS_ON, false), 2);
		}
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		super.randomTick(worldIn, pos, state, random);
		if (worldIn.isRemote || !state.getValue(IS_ON))
			return;
		boolean grew = false;
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-3, -1, -3), pos.add(3, 1, 3))) {
			IBlockState nState = worldIn.getBlockState(p);
			if (nState.getBlock() instanceof BlockCrops) {
				BlockCrops crops = (BlockCrops) nState.getBlock();
				if (!crops.canGrow(worldIn, pos, nState, worldIn.isRemote))
					continue;
				// worldIn.spawnParticle(EnumParticleTypes., xCoord, yCoord,
				// zCoord, xSpeed, ySpeed, zSpeed, parameters);
				// crops.grow(worldIn, worldIn.rand, p, nState);
				ItemDye.applyBonemeal(new ItemStack(Items.DYE, 1, 15), worldIn, p);
				worldIn.playEvent(2005, p, 0);
				grew = true;
			}
		}
		if (grew)
			worldIn.playEvent(2005, pos, 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(IS_ON, meta > 0 ? true : false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (state.getValue(IS_ON))
			return 1;
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { IS_ON });
	}

}
