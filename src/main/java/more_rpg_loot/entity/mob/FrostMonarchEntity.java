package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.projectile.FrostballEntity;
import more_rpg_loot.item.CommonItems;
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
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;

public class FrostMonarchEntity extends SkeletonEntity {
    private final ServerBossBar bossBar;

    public FrostMonarchEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.experiencePoints += 50;
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
        this.goalSelector.add(5, new FrostMonarchEntity.MonarchSpecialAttacksGoal(this));
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
                this.getWorld().addParticle(Particles.FREEZING_SNOWFLAKE,
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
        ItemEntity itemEntity = this.dropItem(CommonItems.FROZEN_SOUL);
        if (itemEntity != null) {
            itemEntity.setCovetedItem();
        }
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
                applyStatusEffect(entity,0,7, Effects.FREEZING,1,
                        true,true,true,1);
            }
            return true;
        } else {
            return false;
        }
    }
    public boolean damage(DamageSource source, float amount) {
        if(source.isIn(DamageTypeTags.IS_FIRE) && !this.isInLava()){
            return false;
        }
        if (!this.getWorld().isClient) {
            if(!source.isIn(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !source.isOf(DamageTypes.THORNS)){
                Entity attacker = source.getSource();
                if (attacker instanceof LivingEntity livingEntity) {
                    applyStatusEffect(livingEntity,0,7, Effects.FREEZING,1,
                            true,true,true,1);
                }
            }
        }
        return super.damage(source, amount);
    }


    private static class MonarchSpecialAttacksGoal extends Goal {
        private final FrostMonarchEntity monarch;
        private int callServants;
        private int callServantsCooldown;
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
            this.callServants = 5;
            this.callServantsCooldown = 0;
        }

        public void stop() {
            this.targetNotVisibleTicks = 0;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.monarch.getTarget();
            --this.callServantsCooldown;

            if (livingEntity != null) {
                float actualHealth = this.monarch.getHealth();
                float maxHealth = this.monarch.getMaxHealth();
                ServerWorld serverWorld = (ServerWorld) this.monarch.getWorld();
                Random rand = new Random();
                float randomPlacement = rand.nextFloat() * (-5.0F - 5.0F) + 5.0F;
                if(callServants!= 0){
                    if(actualHealth/maxHealth <= 0.75F && actualHealth/maxHealth > 0.75F && this.callServants >=4 && callServantsCooldown <= 0) {
                        FrosthauntEntity servantEntity = ModEntities.FROST_HAUNT.create(serverWorld);
                        servantEntity.setPosition(this.monarch.getX() + randomPlacement, this.monarch.getY(), this.monarch.getZ() + randomPlacement);
                        this.monarch.getWorld().spawnEntity(servantEntity);
                        --this.callServants;
                        this.callServantsCooldown = 800;
                    }else if(actualHealth/maxHealth <= 0.5F && actualHealth/maxHealth > 0.25F && this.callServants >= 3 && callServantsCooldown <= 0){
                        FrosthauntEntity servantEntity = ModEntities.FROST_HAUNT.create(serverWorld);
                        servantEntity.setPosition(this.monarch.getX() + randomPlacement, this.monarch.getY(), this.monarch.getZ() + randomPlacement);
                        this.monarch.getWorld().spawnEntity(servantEntity);
                        --this.callServants;
                        this.callServantsCooldown = 600;
                    }
                    else if(actualHealth/maxHealth <= 0.25F && this.callServants >= 1 && callServantsCooldown <= 0){
                        FrosthauntEntity servantEntity = ModEntities.FROST_HAUNT.create(serverWorld);
                        servantEntity.setPosition(this.monarch.getX() + randomPlacement, this.monarch.getY(), this.monarch.getZ() + randomPlacement);
                        this.monarch.getWorld().spawnEntity(servantEntity);
                        --this.callServants;
                        this.callServantsCooldown = 400;
                    }
                }
                super.tick();
            }
        }
    }

}
