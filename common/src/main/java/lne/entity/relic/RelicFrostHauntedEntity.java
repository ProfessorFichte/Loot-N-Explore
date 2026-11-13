package lne.entity.relic;

import lne.fx.LNEFx;
import lne.entity.frozen_depths.mob.frost_haunted.FrostHauntedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidSunlightGoal;
import net.minecraft.entity.ai.goal.EscapeSunlightGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RelicFrostHauntedEntity extends FrostHauntedEntity implements Ownable, EmergingMinion {
    private UUID ownerId;
    private int lifeTicks = 400;

    public RelicFrostHauntedEntity(EntityType<? extends SkeletonEntity> type, World world) {
        super(type, world);
    }

    public void setOwner(LivingEntity owner, int lifeTicks) {
        this.ownerId = owner.getUuid();
        this.lifeTicks = lifeTicks;
        EmergingMinion.nameAfterOwner(this, owner);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        if (ownerId != null && getWorld() instanceof ServerWorld world) {
            return world.getEntity(ownerId);
        }
        return null;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.clear(goal -> goal instanceof EscapeSunlightGoal || goal instanceof AvoidSunlightGoal || goal instanceof WanderAroundGoal);
        goalSelector.add(2, new RelicMinionGoals.FollowOwner<>(this));
        goalSelector.add(7, new RelicMinionGoals.WanderNearOwner<>(this));
        targetSelector.clear(goal -> true);
        targetSelector.add(1, new RelicMinionGoals.ProtectOwner<>(this));
    }

    @Override
    protected boolean usesMobCompat() {
        return false;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        var data = super.initialize(world, difficulty, spawnReason, entityData);
        setPersistent();
        return data;
    }

    @Override
    public void tick() {
        super.tick();
        EmergingMinion.tick(this);
        if (!getWorld().isClient && !EmergingMinion.isEmerging(this) && --lifeTicks <= 0) {
            EmergingMinion.vanish(this, LNEFx.FROST_HAUNTED_VANISH);
        }
    }

    @Override
    public boolean isAiDisabled() {
        return EmergingMinion.isEmerging(this) || super.isAiDisabled();
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    protected boolean isAffectedByDaylight() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (EmergingMinion.isEmerging(this)) {
            return false;
        }
        if (source.getAttacker() != null && source.getAttacker().getUuid().equals(ownerId)) {
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
    }

    @Override
    public void dropXp(@Nullable Entity attacker) {
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (ownerId != null) nbt.putUuid("Owner", ownerId);
        nbt.putInt("LifeTicks", lifeTicks);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) ownerId = nbt.getUuid("Owner");
        lifeTicks = nbt.getInt("LifeTicks");
    }
}
