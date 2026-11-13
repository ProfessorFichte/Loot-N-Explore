package lne.blocks.frozen_depths;

import lne.blocks.FrozenDepthsBlocks;
import com.mojang.serialization.MapCodec;
import lne.effects.Effects;
import lne.particle.Particles;
import com.mrpg_lib.sounds.MRPGLibSounds;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class FrostflameBlock extends AbstractFireBlock {
    public static final MapCodec<FrostflameBlock> CODEC = createCodec(FrostflameBlock::new);
    private static final float DAMAGE = 2.0F;
    private static final int FROZEN_TICKS_PER_TICK = 3;
    private static final int RAIN_CHECK_INTERVAL = 20;

    public FrostflameBlock(Settings settings) {
        super(settings, DAMAGE);
    }

    @Override
    protected MapCodec<? extends AbstractFireBlock> getCodec() {
        return CODEC;
    }

    public static boolean isFrostflameBase(BlockState state) {
        return state.isOf(FrozenDepthsBlocks.CRYOTIC_BLOCK.block());
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                   WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.canPlaceAt(state, world, pos) ? state : Blocks.AIR.getDefaultState();
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isFrostflameBase(world.getBlockState(pos.down()));
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!this.canPlaceAt(state, world, pos)) {
            world.removeBlock(pos, false);
            return;
        }
        world.scheduleBlockTick(pos, this, RAIN_CHECK_INTERVAL);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.hasRain(pos)) {
            world.removeBlock(pos, false);
            return;
        }
        world.scheduleBlockTick(pos, this, RAIN_CHECK_INTERVAL);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(24) == 0) {
            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, MRPGLibSounds.FROST_CRACKLE.soundEvent(), SoundCategory.BLOCKS,
                    0.6F, 0.8F + random.nextFloat() * 0.4F, false);
        }
        for (int i = 0; i < 2; i++) {
            world.addParticle(Particles.FROST_MIST, pos.getX() + random.nextDouble(), pos.getY() + 0.2 + random.nextDouble() * 0.6,
                    pos.getZ() + random.nextDouble(), (random.nextDouble() - 0.5) * 0.01, 0.01 + random.nextDouble() * 0.02,
                    (random.nextDouble() - 0.5) * 0.01);
        }
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient || !(entity instanceof LivingEntity living)) {
            return;
        }
        if (living.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                || living.hasStatusEffect(Effects.FROST_RESISTANCE.registryEntry)) {
            return;
        }
        if (living.canFreeze()) {
            int frozenTicks = Math.min(living.getMinFreezeDamageTicks(), living.getFrozenTicks() + FROZEN_TICKS_PER_TICK);
            living.setFrozenTicks(frozenTicks);
        }
        float damage = living.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES) ? DAMAGE * 2.5F : DAMAGE;
        living.damage(world.getDamageSources().freeze(), damage);
    }
}
