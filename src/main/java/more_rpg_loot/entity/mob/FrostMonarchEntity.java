package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.item.WeaponRegister;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FrostMonarchEntity extends SkeletonEntity {
    private final ServerBossBar bossBar;

    public FrostMonarchEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.experiencePoints += 10;
    }

    public static DefaultAttributeContainer.Builder createFrostmonarchAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_ARMOR, 8.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2505)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

    }

    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    protected void initGoals() {
        //this.goalSelector.add(2, new AvoidSunlightGoal(this));
        //this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.5));
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

    protected void mobTick() {
        if(this.isOnFire() && !this.isInLava()){
            this.extinguish();
        }
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        allowDrops = false;
        super.dropEquipment(source, lootingMultiplier, allowDrops);
    }

    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(WeaponRegister.GLACIAL_AXE));
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
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
    public boolean damage(DamageSource source, float amount) {
        if(source.isIn(DamageTypeTags.IS_FIRE)){
            return false;
        }
        return super.damage(source, amount);
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
