package com.hea3ven.buildingbricks.core.blocks.base;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.common.util.ForgeDirection;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockLogic;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.transition.helpers.BlockHelper.IBlock;
import com.hea3ven.transition.m.block.properties.IProperty;
import com.hea3ven.transition.m.block.state.BlockState;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;
import com.hea3ven.transition.m.util.EnumFacing;
import com.hea3ven.transition.m.util.EnumWorldBlockLayer;

public class BlockBuildingBricksBase extends Block implements IBlock {

	protected MaterialBlockLogic blockLogic;

	public BlockBuildingBricksBase(StructureMaterial structMat, MaterialBlockType blockType) {
		super(structMat.getMcMaterial());

		blockLogic = new MaterialBlockLogic(structMat, blockType);
		blockLogic.initBlock(this);
		blockState = createBlockState();
	}

	public boolean requiresUpdates() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return blockLogic.getBlockColor();
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		return blockLogic.getRenderColor(state);
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return blockLogic.colorMultiplier(worldIn, pos, renderPass);
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return blockLogic.getBlockLayer();
	}

	public int getHarvestLevel(IBlockState state) {
		return 0;
	}

	public String getHarvestTool(IBlockState state) {
		return blockLogic.getHarvestTool(state);
	}

	/**************** 1.7.10 ****************/

	protected BlockState blockState;

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[0]);
	}

	public void setDefaultState(IBlockState state) {
		blockState.setDefault(state);
	}

	public IBlockState getDefaultState() {
		return blockState.getDefault();
	}

	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMaterial();
	}

	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state;
	}

	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(super.onBlockPlaced(world, pos.getX(), pos.getY(), pos.getZ(),
				facing.ordinal(), hitX, hitY, hitZ, meta));
	}

	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos.getX(), pos.getY(), pos.getZ(), placer, stack);
	}

	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
		return TileMaterial.getPickBlock(this, target, world, pos);
	}

	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		return super.getDrops((World) world, pos.getX(), pos.getY(), pos.getZ(),
				getMetaFromState(state), fortune);
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos.getX(), pos.getY(), pos.getZ(), state.getBlock(),
				getMetaFromState(state));
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		super.setBlockBoundsBasedOnState(world, pos.getX(), pos.getY(), pos.getZ());
	}

	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return super.getCollisionBoundingBoxFromPool(world, pos.getX(), pos.getY(), pos.getZ());
	}

	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return super.shouldSideBeRendered(world, pos.getX(), pos.getY(), pos.getZ(),
				side.ordinal());
	}

	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return super.isSideSolid(world, pos.getX(), pos.getY(), pos.getZ(), side.getForgeDir());
	}

	@Override
	public boolean isReplaceable(World world, BlockPos pos) {
		return super.isReplaceable(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return hasTileEntity(getStateFromMeta(metadata));
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return createTileEntity(world, getStateFromMeta(metadata));
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int facingIdx, float hitX,
			float hitY, float hitZ, int meta) {
		return getMetaFromState(onBlockPlaced(world, new BlockPos(x, y, z),
				EnumFacing.get(facingIdx), hitX, hitY, hitZ, meta, null));
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer,
			ItemStack stack) {
		onBlockPlacedBy(world, new BlockPos(x, y, z),
				getStateFromMeta(world.getBlockMetadata(x, y, z)), placer, stack);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z,
			EntityPlayer player) {
		return getPickBlock(target, world, new BlockPos(x, y, z));
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		return (ArrayList<ItemStack>) getDrops(world, new BlockPos(x, y, z), getStateFromMeta(meta),
				fortune);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		breakBlock(world, new BlockPos(x, y, z), getStateFromMeta(meta));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, new BlockPos(x, y, z));
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return getCollisionBoundingBox(world, new BlockPos(x, y, z),
				getStateFromMeta(world.getBlockMetadata(x, y, z)));
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int facingIdx) {
		return shouldSideBeRendered(world, new BlockPos(x, y, z), EnumFacing.get(facingIdx));
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return isSideSolid(world, new BlockPos(x, y, z), EnumFacing.get(side));
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		return isReplaceable((World) world, new BlockPos(x, y, z));
	}
}