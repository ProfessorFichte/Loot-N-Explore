package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.projectile.FrostballEntity;
import more_rpg_loot.sounds.ModSounds;
import more_rpg_loot.util.HelperMethods;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

public class GlazeEntity extends HostileEntity {
    private float eyeOffset = 0.5F;
    private int eyeOffsetCooldown;

    public GlazeEntity(EntityType<? extends GlazeEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.experiencePoints = 10;
    }

    protected void initGoals() {
        this.goalSelector.add(4, new GlazeEntity.GlazeSpecialAttacksGoal(this));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0, 0.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createGlazeAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        if(FabricLoader.getInstance().isModLoaded("thermoo")){
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        return entityData2;
    }

    protected void initDataTracker() {
        super.initDataTracker();
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_GLAZE_AMBIENT_EVENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ENTITY_GLAZE_HURT_EVENT;
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_GLAZE_DEATH_EVENT;
    }

    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    public void tickMovement() {
        if (!this.isOnGround() && this.getVelocity().y < 0.0) {
            this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
        }

        if (this.getWorld().isClient) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.getWorld().playSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5,
                        ModSounds.ENTITY_GLAZE_FREEZE_EVENT, this.getSoundCategory(), 1F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

            for(int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                        this.getParticleX(1.5), this.getRandomBodyY(), this.getParticleZ(1.5),
                        0, -0.1, 0);
                this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                        this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5),
                        0, 0, 0);
            }
        }

        super.tickMovement();
    }

    public boolean hurtByWater() {
        return false;
    }

    protected void mobTick() {
        --this.eyeOffsetCooldown;
        if (this.eyeOffsetCooldown <= 0) {
            this.eyeOffsetCooldown = 100;
            this.eyeOffset = (float)this.random.nextTriangular(0.5, 6.891);
        }

        LivingEntity livingEntity = this.getTarget();
        if (livingEntity != null && livingEntity.getEyeY() > this.getEyeY() + (double)this.eyeOffset && this.canTarget(livingEntity)) {
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(this.getVelocity().add(0.0, (0.30000001192092896 - vec3d.y) * 0.30000001192092896, 0.0));
            this.velocityDirty = true;
        }

        super.mobTick();
    }

    public boolean tryAttack(Entity target) {
        if (super.tryAttack(target)) {
            if (target instanceof LivingEntity entity) {
                entity.setFrozenTicks(entity.getFrozenTicks() + 50);
            }
            return true;
        } else {
            return false;
        }
    }

    private static class GlazeSpecialAttacksGoal extends Goal {
        private final GlazeEntity glaze;
        private int frostballsHailFired;
        private int frostballHailCooldown;
        private int frostStormCooldown;
        private int frostStormFired;
        private int targetNotVisibleTicks;

        public GlazeSpecialAttacksGoal(GlazeEntity glaze) {
            this.glaze = glaze;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            LivingEntity livingEntity = this.glaze.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.glaze.canTarget(livingEntity);
        }

        public void start() {
            this.frostballsHailFired = 0;
            this.frostStormFired = 0;
        }

        public void stop() {
            this.targetNotVisibleTicks = 0;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            --this.frostballHailCooldown;
            --this.frostStormCooldown;
            double followRangeSquare = this.getFollowRange()*this.getFollowRange();
            double frostStormRange = 40;
            LivingEntity livingEntity = this.glaze.getTarget();
            if (livingEntity != null) {
                boolean bl = this.glaze.getVisibilityCache().canSee(livingEntity);
                if (bl) {
                    this.targetNotVisibleTicks = 0;
                } else {
                    ++this.targetNotVisibleTicks;
                }

                double d = this.glaze.squaredDistanceTo(livingEntity);
                if (d < 8.0) {
                    if (!bl) {
                        return;
                    }
                    this.glaze.tryAttack(livingEntity);
                    this.glaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
                }

                /*
                                 Random rand = new Random();
                                float random = rand.nextFloat() * (0.025F - 0.0F) + 0.0F;
                                BlockPos blockPos = this.glaze.getBlockPos()
                                        .add(-2 + this.glaze.random.nextInt(5), 2, -2 + this.glaze.random.nextInt(5));
                                FrostballEntity frostballEntity2 = new  FrostballEntity(livingEntity.getWorld(), livingEntity);
                                frostballEntity2.setOwner(this.glaze);
                                frostballEntity2.refreshPositionAndAngles(blockPos, 0.0F, 0.0F);
                                frostballEntity2.setVelocity(
                                        this.glaze,
                                        this.glaze.getPitch() + this.glaze.random.nextFloat(),
                                        this.glaze.getHeadYaw() + this.glaze.random.nextFloat(),
                                        0.0f, 2.5f+random, 0.5f+random
                                this.glaze.getWorld().spawnEntity(frostballEntity2);
                                */

                else if(d < followRangeSquare & d> frostStormRange  && bl){
                    //FROSTBALL HAIL
                    if (this.frostballHailCooldown <= 0) {
                        ++this.frostballsHailFired;
                        if (this.frostballsHailFired == 1) {
                            this.frostballHailCooldown = 60;
                        } else if (this.frostballsHailFired <= 3) {
                            this.frostballHailCooldown = 6;
                        } else {
                            this.frostballHailCooldown = 100;
                            this.frostballsHailFired = 0;
                        }

                        if (this.frostballsHailFired > 1) {
                            for(int i = 0; i < 1; ++i) {
                                Random rand = new Random();
                                float random = rand.nextFloat() * (0.025F - 0.0F) + 0.0F;
                                float randomDivergence = rand.nextFloat() * (0.5F - 0.0F) + 0.0F;
                                float randomHeight = rand.nextFloat() * (3.0F - 1.0F) + 1.0F;

                                FrostballEntity frostballEntity = new  FrostballEntity(livingEntity.getWorld(), livingEntity);
                                frostballEntity.setOwner(this.glaze);
                                frostballEntity.setPosition(livingEntity.getX() + random, livingEntity.getBodyY(randomHeight) + 0.5, livingEntity.getZ() + random);
                                frostballEntity.setVelocity(frostballEntity, frostballEntity.getPitch(), frostballEntity.getYaw(), random, random, randomDivergence);
                                this.glaze.getWorld().spawnEntity(frostballEntity);
                            }
                        }
                    }

                    this.glaze.getLookControl().lookAt(livingEntity, 10.0F, 10.0F);
                }
                else if(d< frostStormRange && bl){
                    //FROSTSTORM
                    if (this.frostStormCooldown <= 0) {
                        ++this.frostStormFired;
                        if (this.frostStormFired == 1) {
                            this.frostStormCooldown = 600;
                        }
                        if (!glaze.getWorld().isClient) {
                            HelperMethods.spawnCloudEntity(glaze, 4.0F, 10, 5.0F,
                                    Effects.FREEZING, 10, 0);
                        }
                    }
                }
                else if (this.targetNotVisibleTicks < 5) {
                    this.glaze.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
                }

                super.tick();
            }
        }

        private double getFollowRange() {
            return this.glaze.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }
    }

}
