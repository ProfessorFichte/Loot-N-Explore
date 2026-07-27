package more_rpg_loot.entity.frozen_depths.mob.glaze;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.frozen_depths.projectile.FrostballEntity;
import more_rpg_loot.sounds.ModSounds;
import more_rpg_loot.util.HelperMethods;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
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

import static more_rpg_loot.util.HelperMethods.stackFreezeStacks;

public class GlazeEntity extends HostileEntity {
    public final AnimationState idleAnimationState = new AnimationState();
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
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData);
        if(FabricLoader.getInstance().isModLoaded("thermoo")){
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        return entityData2;
    }


    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_GLAZE_AMBIENT.soundEvent();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ENTITY_GLAZE_HURT.soundEvent();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_GLAZE_DEATH.soundEvent();
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
                        ModSounds.ENTITY_GLAZE_FREEZE.soundEvent(), this.getSoundCategory(), 1F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

            net.minecraft.util.math.random.Random random = this.getWorld().random;

            for (int i = 0; i < 10; i++) {
                double offsetX = (random.nextDouble() - 0.5) * 3.0;
                double offsetY = random.nextDouble() * 2.0;
                double offsetZ = (random.nextDouble() - 0.5) * 3.0;

                double windX = (random.nextDouble() - 0.5) * 0.2;
                double windY = -0.05 - random.nextDouble() * 0.05;
                double windZ = (random.nextDouble() - 0.5) * 0.2;

                this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                        this.getX() + offsetX,
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        windX, windY, windZ);
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
                stackFreezeStacks(entity,20);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        this.idleAnimationState.startIfNotRunning(this.age);
    }

    private static class GlazeSpecialAttacksGoal extends Goal {
        private final GlazeEntity glaze;
        private int frostballsHailFired;
        private int frostballHailCooldown;
        private int frostStormCooldown;
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
                if (d < 4.0) {
                    if (!bl) {
                        return;
                    }
                    this.glaze.getLookControl().lookAt(livingEntity, 10.0F, 10.0F);
                    this.glaze.tryAttack(livingEntity);
                    this.glaze.getNavigation().startMovingTo(livingEntity, 1.0);
                }

                else if(d < followRangeSquare && d > frostStormRange && bl){
                    //FROSTBALL HAIL
                    this.glaze.getNavigation().stop();
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
                                float random = this.glaze.random.nextFloat() * 0.025F;
                                float randomDivergence = this.glaze.random.nextFloat() * 0.5F;
                                float randomHeight = this.glaze.random.nextFloat() * 2.0F + 1.0F;

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
                    this.glaze.getLookControl().lookAt(livingEntity, 10.0F, 10.0F);
                    this.glaze.getNavigation().stop();
                    if (this.frostStormCooldown <= 0) {
                        if (!glaze.getWorld().isClient) {
                            HelperMethods.spawnCloudEntity(ParticleTypes.SNOWFLAKE, glaze, glaze,1,2.0F, 5, 4.0F,
                                    Effects.FREEZING.registryEntry, 3, 1,false,0,true,(float) glaze.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) *0.3F,
                                    new DamageSource(glaze.getTarget().getDamageSources().freeze().getTypeRegistryEntry()));
                            this.frostStormCooldown = 600;
                        }
                    }
                }
                else if (this.targetNotVisibleTicks < 5) {
                    this.glaze.getLookControl().lookAt(livingEntity, 10.0F, 10.0F);
                    this.glaze.getNavigation().startMovingTo(livingEntity, 1.0);
                }

                super.tick();
            }
        }

        private double getFollowRange() {
            return this.glaze.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }
    }

}
