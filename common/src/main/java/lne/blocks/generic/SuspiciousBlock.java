package lne.blocks.generic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SuspiciousBlock extends BrushableBlock {
    private final boolean falls;

    public SuspiciousBlock(Block baseBlock, SoundEvent brushingSound, SoundEvent brushingCompleteSound, boolean falls, Settings settings) {
        super(baseBlock, brushingSound, brushingCompleteSound, settings);
        this.falls = falls;
    }

    public Direction ejectDirection(Direction hitSide, PlayerEntity player) {
        return hitSide;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (falls) {
            super.scheduledTick(state, world, pos, random);
        } else if (world.getBlockEntity(pos) instanceof BrushableBlockEntity brushable) {
            brushable.scheduledTick();
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (falls) {
            super.randomDisplayTick(state, world, pos, random);
        }
    }

    public static class Sideways extends SuspiciousBlock {
        public Sideways(Block baseBlock, SoundEvent brushingSound, SoundEvent brushingCompleteSound, Settings settings) {
            super(baseBlock, brushingSound, brushingCompleteSound, false, settings);
        }

        @Override
        public Direction ejectDirection(Direction hitSide, PlayerEntity player) {
            return hitSide.getAxis().isHorizontal() ? hitSide : player.getHorizontalFacing().getOpposite();
        }
    }
}
