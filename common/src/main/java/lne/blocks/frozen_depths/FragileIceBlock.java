package lne.blocks.frozen_depths;

import net.minecraft.sound.SoundCategory;
import com.mrpg_lib.sounds.MRPGLibSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FragileIceBlock extends TranslucentBlock {
    private static final float RUN_BREAK_CHANCE = 0.30F;
    private static final float WALK_BREAK_CHANCE = 0.05F;
    private static final float CREAK_CHANCE = 0.15F;

    public FragileIceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance);
        if (!world.isClient && canBreakUnder(entity)) {
            world.breakBlock(pos, false);
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (world.isClient || !canBreakUnder(entity)) {
            return;
        }

        float chance = entity.isSprinting() ? RUN_BREAK_CHANCE : WALK_BREAK_CHANCE;
        if (world.getRandom().nextFloat() < chance) {
            world.breakBlock(pos, false);
        } else if (world.getRandom().nextFloat() < CREAK_CHANCE) {
            world.playSound(null, pos, MRPGLibSounds.FROST_CRACKLE.soundEvent(), SoundCategory.BLOCKS,
                    0.5F, 1.5F + world.getRandom().nextFloat() * 0.3F);
        }
    }

    private static boolean canBreakUnder(Entity entity) {
        return !PowderSnowBlock.canWalkOnPowderSnow(entity);
    }
}
