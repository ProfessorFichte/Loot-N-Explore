package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.projectile.FrostballEntity;
import more_rpg_loot.item.WeaponRegister;
import more_rpg_loot.sounds.ModSounds;
import more_rpg_loot.util.HelperMethods;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

public class FrostMonarchEntity extends SkeletonEntity {
    public FrostMonarchEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints += 10;
    }

    public static DefaultAttributeContainer.Builder createFrostmonarchAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2505)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 2.5));
        //this.goalSelector.add(4, new FrostMonarchEntity.MonarchSpecialAttacksGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));
    }


    public void tickMovement() {
        if (this.getWorld().isClient) {
            for(int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                        this.getParticleX(1.5), this.getRandomBodyY(), this.getParticleZ(1.5),
                        -0.1, -0.1, -0.1);
            }
        }
        super.tickMovement();
    }

    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(WeaponRegister.GLACIAL_AXE));
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        if(FabricLoader.getInstance().isModLoaded("thermoo")){
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(6.0);
        this.updateAttackType();
        return entityData2;
    }

    public boolean tryAttack(Entity target) {
        if (super.tryAttack(target)) {
            if (target instanceof LivingEntity entity) {
                entity.setFrozenTicks(entity.getFrozenTicks() + 40);
            }
            return true;
        } else {
            return false;
        }
    }

    private static class MonarchSpecialAttacksGoal extends Goal {
        private final FrostMonarchEntity monarch;
        private int frostballsHailFired;
        private int frostballHailCooldown;
        private int frostStormCooldown;
        private int frostStormFired;
        private int targetNotVisibleTicks;

        public MonarchSpecialAttacksGoal(FrostMonarchEntity monarch) {
            this.monarch = monarch;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            LivingEntity livingEntity = this.monarch.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.monarch.canTarget(livingEntity);
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
            LivingEntity livingEntity = this.monarch.getTarget();
            if (livingEntity != null) {
                boolean bl = this.monarch.getVisibilityCache().canSee(livingEntity);
                if (bl) {
                    this.targetNotVisibleTicks = 0;
                } else {
                    ++this.targetNotVisibleTicks;
                }
                double d = this.monarch.squaredDistanceTo(livingEntity);

                if (this.targetNotVisibleTicks < 5) {
                    this.monarch.getMoveControl().moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0);
                }

                super.tick();
            }
        }

        private double getFollowRange() {
            return this.monarch.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }
    }

}
