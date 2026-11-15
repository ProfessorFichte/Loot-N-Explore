package lne.blocks.frozen_depths;

import lne.blocks.FrozenDepthsBlocks;
import lne.entity.frozen_depths.conjured.PortalPlacement;
import lne.fx.LNEFx;
import lne.util.LneTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SealedMonarchBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 24, 14);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private static final double GENERAL_SPAWN_RADIUS = 8.0;
    private static final int PORTAL_LIFETIME = 80;
    private static final int GENERAL_SPAWN_DELAY = 50;

    public SealedMonarchBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world instanceof ServerWorld serverWorld) {
            world.breakBlock(pos, false, player);
            Block.dropStack(world, pos, new ItemStack(FrozenDepthsBlocks.MONARCHS_CROWN.item()));
            unseal(serverWorld, pos);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (world instanceof ServerWorld serverWorld) {
            unseal(serverWorld, pos);
        }
    }

    private static void unseal(ServerWorld world, BlockPos pos) {
        Vec3d center = Vec3d.ofBottomCenter(pos);
        LNEFx.play(world, LNEFx.SEALED_MONARCH_BREAK, center);
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        List<EntityType<?>> generals = Registries.ENTITY_TYPE.getEntryList(LneTags.GENERALS)
                .map(list -> list.stream().<EntityType<?>>map(entry -> entry.value()).toList())
                .orElse(List.of());
        if (generals.isEmpty()) {
            return;
        }
        EntityType<?> general = generals.get(world.getRandom().nextInt(generals.size()));
        List<Vec3d> spots = PortalPlacement.findSpots(world, center, world.getRandom(), 1, GENERAL_SPAWN_RADIUS, null, false, false);
        Vec3d spot = spots.isEmpty() ? center : spots.getFirst();
        PortalPlacement.openAt(world, null, spot, general, PORTAL_LIFETIME, GENERAL_SPAWN_DELAY, PORTAL_LIFETIME, false);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}
