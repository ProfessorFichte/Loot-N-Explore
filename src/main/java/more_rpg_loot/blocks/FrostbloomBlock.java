package more_rpg_loot.blocks;

import more_rpg_loot.effects.Effects;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class FrostbloomBlock extends FlowerBlock {
    // 1.20.1: blocks have no MapCodec (no block codecs before 1.20.5), so there is no CODEC field
    // and no getCodec() override. FlowerBlock also has no SuspiciousStewEffectsComponent - the
    // suspicious-stew effect is a single (StatusEffect, duration) pair passed straight to super.
    //
    // 1.20.1: `effectDuration` is in SECONDS, same unit as the float 1.21 passes: FlowerBlock's
    // constructor itself does `effectDuration * 20` for non-instant effects (and leaves instant
    // effects alone), which is what 1.21's createStewEffectList(effect, seconds) did. So the value
    // from the call site carries over unchanged - do NOT pre-multiply by 20 here.
    public FrostbloomBlock(StatusEffect suspiciousStewEffect, int effectDuration, AbstractBlock.Settings settings) {
        super(suspiciousStewEffect, effectDuration, settings);
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(floor, world, pos) || floor.isOf(Blocks.DIRT) || floor.isOf(Blocks.COARSE_DIRT) || floor.isOf(Blocks.ROOTED_DIRT);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        VoxelShape voxelShape = this.getOutlineShape(state, world, pos, ShapeContext.absent());
        Vec3d vec3d = voxelShape.getBoundingBox().getCenter();
        double d = (double)pos.getX() + vec3d.x;
        double e = (double)pos.getZ() + vec3d.z;

        for(int i = 0; i < 3; ++i) {
            if (random.nextBoolean()) {
                world.addParticle(ParticleTypes.SNOWFLAKE, d + random.nextDouble() / 5.0, (double)pos.getY() + (1.0 - random.nextDouble()), e + random.nextDouble() / 5.0, 0.0, 0.0, 0.0);
            }
        }

    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && world.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity instanceof LivingEntity livingEntity) {
                EntityType<?> type = entity.getType();
                if (!type.isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(Effects.FREEZING.effect, 40));
                }
            }

        }
    }
}
