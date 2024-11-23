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
    public FrostbloomBlock(StatusEffect effect, int i, Settings settings) {
        super(effect, 8, settings);
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
                    livingEntity.addStatusEffect(new StatusEffectInstance(Effects.FREEZING, 40));
                }
            }

        }
    }
}
