package com.me.farmaddon.block;

import com.google.common.collect.ImmutableMap;
import com.me.farmaddon.block.entity.CropLabelBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CropLabelBlock extends BlockWithEntity implements BlockEntityProvider {
	public static final DirectionProperty FACING;
	private static final VoxelShape EAST_SHAPE;
	private static final VoxelShape WEST_SHAPE;
	private static final VoxelShape SOUTH_SHAPE;
	private static final VoxelShape NORTH_SHAPE;
	private final Map<BlockState, VoxelShape> shapesByState;

	private static final Collection<Item> ALLOWED_ITEMS;

	public CropLabelBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
		this.shapesByState = ImmutableMap.copyOf(this.stateManager.getStates().stream().collect(Collectors.toMap(Function.identity(), CropLabelBlock::getShapeForState)));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.shapesByState.get(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			BlockEntity t = world.getBlockEntity(pos);
			if (t instanceof CropLabelBlockEntity blockEntity) {
				Item item = player.getStackInHand(hand).getItem();

				if (ALLOWED_ITEMS.contains(item) || item.getDefaultStack().isOf(Items.AIR)) {
					blockEntity.setHandledCrop(item.getDefaultStack());
					blockEntity.markDirty();
					return ActionResult.SUCCESS;
				}

				return ActionResult.PASS;
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isIn(BlockTags.DIRT);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return true;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CropLabelBlockEntity(pos, state);
	}

	private static VoxelShape getShapeForState(BlockState state) {
		return switch (state.get(FACING)) {
			default -> NORTH_SHAPE;
			case EAST -> EAST_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
		};
	}

	static {
		FACING = HorizontalFacingBlock.FACING;
		EAST_SHAPE = Block.createCuboidShape(12, 0, 5, 15, 7, 11);
		WEST_SHAPE = Block.createCuboidShape(1, 0, 5, 4, 7, 11);
		SOUTH_SHAPE = Block.createCuboidShape(5, 0, 12, 11, 7, 15);
		NORTH_SHAPE = Block.createCuboidShape(5, 0, 1, 11, 7, 4);

		// compat with other mods
		ALLOWED_ITEMS = Registries.ITEM.stream().filter((item) -> item instanceof BlockItem && ((((BlockItem) item).getBlock() instanceof PlantBlock) || (((BlockItem) item).getBlock() instanceof GourdBlock)))
				.collect(Collectors.toCollection(ArrayList::new));
	}
}
