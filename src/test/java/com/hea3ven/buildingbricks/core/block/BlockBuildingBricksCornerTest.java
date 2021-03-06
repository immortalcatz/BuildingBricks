package com.hea3ven.buildingbricks.core.block;

import org.junit.Ignore;
import org.junit.Test;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import com.hea3ven.buildingbricks.core.block.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

@Ignore
public class BlockBuildingBricksCornerTest extends BlockTestBase {

	@Test
	public void testGetBoundingBoxDefault() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();

		assertAABBEquals(0, 0, 0, 0.5d, 0.5d, 0.5d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Test
	public void testGetBoundingBoxRot90() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT90);

		assertAABBEquals(0.5d, 0, 0, 1.0d, 0.5d, 0.5d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Test
	public void testGetBoundingBoxRot180() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT180);

		assertAABBEquals(0.5d, 0, 0.5d, 1.0d, 0.5d, 1.0d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Test
	public void testGetBoundingBoxRot270() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT270);

		assertAABBEquals(0.0d, 0, 0.5d, 0.5d, 0.5d, 1.0d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Test
	public void testGetBoundingBoxUpperRot0() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0, 0.5d, 0, 0.5d, 1.0d, 0.5d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Test
	public void testGetBoundingBoxUpperRot90() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT90);
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0.5d, 0.5d, 0, 1.0d, 1.0d, 0.5d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Test
	public void testGetBoundingBoxUpperRot180() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT180);
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0.5d, 0.5d, 0.5d, 1.0d, 1.0d, 1.0d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Test
	public void testGetBoundingBoxUpperRot270() {
		Block corner = new BlockBuildingBricksCorner(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT270);
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0.0d, 0.5d, 0.5d, 0.5d, 1.0d, 1.0d,
				corner.getBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

}
