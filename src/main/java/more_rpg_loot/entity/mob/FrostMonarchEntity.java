package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.WeaponRegister;
import more_rpg_loot.sounds.ModSounds;
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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.more_rpg_classes.effect.MRPGCEffects;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
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
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2605)
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
        List<FrostMonarchServantEntity> list = this.getWorld().getNonSpectatingEntities(FrostMonarchServantEntity.class, this.getBoundingBox().expand(32.0));
        int servantsCount = list.size();
        if (this.getWorld().isClient) {
            if(!list.isEmpty() && !this.isOnFire()){
                for(int i = 0; i < servantsCount; ++i) {
                    this.getWorld().addParticle(ParticleTypes.SOUL,
                            this.getParticleX(3.5), this.getY(), this.getParticleZ(3.5),
                            0, 0.15, 0);
                }
            }else{
                for(int i = 0; i < 2; ++i) {
                    this.getWorld().addParticle(Particles.FREEZING_SNOWFLAKE,
                            this.getParticleX(1.5), this.getRandomBodyY(), this.getParticleZ(1.5),
                            -0.1, -0.1, -0.1);
                }
            }

        }
        super.tickMovement();
    }

    protected void mobTick() {
        List<FrostMonarchServantEntity> list = this.getWorld().getNonSpectatingEntities(FrostMonarchServantEntity.class, this.getBoundingBox().expand(32.0));
        int servantsCount = list.size();
        if(!list.isEmpty() && !this.isOnFire()){
            this.heal(servantsCount*0.2F);
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
        }else if(source.isOf(DamageTypes.GENERIC) && source.isOf(DamageTypes.PLAYER_ATTACK) && source.isOf(DamageTypes.ARROW)){
            return super.damage(source, amount * 0.5F);
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
        private int callServantsCooldown;
        private int callServantsMax = 12;
        private int screechCoolDown;
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
            this.screechCoolDown = 0;
            this.callServantsCooldown = 0;
        }

        public void stop() {
            this.targetNotVisibleTicks = 0;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            --this.callServantsCooldown;
            --this.screechCoolDown;
            LivingEntity livingEntity = this.monarch.getTarget();
            List<FrostMonarchServantEntity> list = this.monarch.getWorld().getNonSpectatingEntities(FrostMonarchServantEntity.class, this.monarch.getBoundingBox().expand(32.0));
            int servantsCount = list.size();
            double distanceTarget = this.monarch.squaredDistanceTo(livingEntity);
            float healthPercentage = this.monarch.getHealth()/this.monarch.getMaxHealth();
            ServerWorld serverWorld = (ServerWorld) this.monarch.getWorld();
            float healthPhase1 = 0.75F;
            float healthPhase2 = 0.5F;
            float healthPhase3 = 0.25F;
            if (livingEntity != null) {
                boolean bl = this.monarch.getVisibilityCache().canSee(livingEntity);
                if (bl) {
                    this.targetNotVisibleTicks = 0;
                } else {
                    ++this.targetNotVisibleTicks;
                }

                if(distanceTarget > 20.0){
                    if (this.screechCoolDown <= 0) {
                        this.screechCoolDown = 300;
                        if (!monarch.getWorld().isClient ) {
                            List<Entity> entities = monarch.getWorld().getOtherEntities(monarch, monarch.getBoundingBox().expand(getFollowRange()));
                            for (Entity entity : entities) {
                                if (entity instanceof LivingEntity livingEntity2 && !(entity instanceof MobEntity)) {
                                    this.monarch.setVelocity(Vec3d.ZERO);
                                    livingEntity2.damage(this.monarch.getWorld().getDamageSources().indirectMagic(this.monarch, this.monarch), (float) getAttackDamage());
                                    double d = this.monarch.getX() - entity.getX();
                                    double e;
                                    for(e = this.monarch.getZ() - livingEntity2.getZ(); d * d + e * e < 1.0E-4; e = (Math.random() - Math.random()) * 0.01) {
                                        d = (Math.random() - Math.random()) * 0.01;
                                    }
                                    livingEntity2.takeKnockback(1.5F, d, e);
                                    if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
                                        livingEntity2.addStatusEffect(new StatusEffectInstance(MRPGCEffects.STUNNED,80, 0));
                                    }else{
                                        livingEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,80, 1));
                                        livingEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,80, 1));
                                    }

                                }
                            }
                            monarch.getWorld().playSound(null, livingEntity.getX(),livingEntity.getY(), livingEntity.getZ(), ModSounds.FROSTMONARCH_SCREECH_EVENT,
                                    SoundCategory.PLAYERS, 2.5f, 1.0f);
                        }
                    }
                }
                if(callServantsCooldown <= 0 && servantsCount != 5 && callServantsMax != 0){
                    Random rand = new Random();
                    float randomPlacement = rand.nextFloat() * (-1.5F - 1.5F) + 1.5F;
                    if(healthPercentage <= healthPhase1 && healthPercentage > healthPhase2) {
                        FrostMonarchServantEntity servantEntity = ModEntities.MONARCH_SERVANT.create(serverWorld);
                            servantEntity.setPosition(this.monarch.getX() + randomPlacement, this.monarch.getY(), this.monarch.getZ() + randomPlacement);
                            servantEntity.equipStack(EquipmentSlot.MAINHAND, Items.STONE_AXE.getDefaultStack());
                            this.monarch.getWorld().spawnEntity(servantEntity);
                            --callServantsMax;
                            if(servantsCount==1){
                                this.callServantsCooldown = 600;
                            }
                        }else if(healthPercentage <= healthPhase2 && healthPercentage > healthPhase3) {
                        FrostMonarchServantEntity servantEntity = ModEntities.MONARCH_SERVANT.create(serverWorld);
                            servantEntity.setPosition(this.monarch.getX() + randomPlacement, this.monarch.getY(), this.monarch.getZ() + randomPlacement);
                            servantEntity.equipStack(EquipmentSlot.MAINHAND, Items.STONE_AXE.getDefaultStack());
                            this.monarch.getWorld().spawnEntity(servantEntity);
                            --callServantsMax;
                            if(servantsCount==2){
                                this.callServantsCooldown = 500;
                            }
                        }
                        else if(healthPercentage <= healthPhase3){
                        FrostMonarchServantEntity servantEntity = ModEntities.MONARCH_SERVANT.create(serverWorld);
                            servantEntity.setPosition(this.monarch.getX() + randomPlacement, this.monarch.getY(), this.monarch.getZ() + randomPlacement);
                            servantEntity.equipStack(EquipmentSlot.MAINHAND, Items.STONE_AXE.getDefaultStack());
                            this.monarch.getWorld().spawnEntity(servantEntity);
                            --callServantsMax;
                            if(servantsCount==4){
                                this.callServantsCooldown = 400;
                            }
                        }
                    }

                super.tick();
            }
        }
        private double getFollowRange() {
            return this.monarch.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }
        private double getAttackDamage() {
            return this.monarch.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        }
    }

}
