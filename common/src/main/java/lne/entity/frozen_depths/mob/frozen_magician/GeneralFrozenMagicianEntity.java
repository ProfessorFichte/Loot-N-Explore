package lne.entity.frozen_depths.mob.frozen_magician;

import lne.entity.frozen_depths.goal.MobSpellGoals;
import com.mrpg_lib.entity.config.CustomValues;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import com.mrpg_lib.util.damage.SpellDamage;
import com.mrpg_lib.util.damage.SpellDamageProfile;
import lne.entity.frozen_depths.conjured.FrostDecoyEntity;
import lne.internal.fx.LNEFx;
import lne.util.damage.FrostDamageProfiles;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import lne.internal.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import net.spell_power.api.SpellSchools;

public class GeneralFrozenMagicianEntity extends FrozenMagicianEntity {
    private static final double BLINK_MIN_TARGET_DISTANCE = 9.0;
    private static final double[] BLINK_DISTANCES = {8.0, 6.0};
    private static final float[] BLINK_TURNS = {0.0F, 0.5F, -0.5F, 1.0F, -1.0F, 1.5F, -1.5F};
    private static final int[] BLINK_HEIGHTS = {0, 1, -1, 2, -2, -3};
    private static final int BLINK_RETRY_TICKS = 20;
    private static final int BLINK_COOLDOWN_SPREAD = 80;

    int portalCooldown = 0;
    private int blinkCooldown = 0;

    public GeneralFrozenMagicianEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected String primarySpellTag() {
        return MobSpellGoals.tag("general_frozen_magician", "primary");
    }

    @Override
    protected String secondarySpellTag() {
        return MobSpellGoals.tag("general_frozen_magician", "secondary");
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new WinterPortalGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            if (portalCooldown > 0) portalCooldown--;
            if (blinkCooldown > 0) blinkCooldown--;
        }
    }

    @Override
    protected boolean tryEvade(LivingEntity threat) {
        if (blinkCooldown > 0 || !(this.getWorld() instanceof ServerWorld world)) {
            return false;
        }
        CustomValues tuning = tuning();
        if (!tuning.getBoolean(FrozenDepthsTuning.FROST_BLINK_ENABLED, true)) {
            return false;
        }
        Vec3d destination = findBlinkSpot(threat);
        if (destination == null) {
            blinkCooldown = BLINK_RETRY_TICKS;
            return false;
        }
        Vec3d departure = this.getPos();
        float damage = SpellDamage.calculate(this, SpellDamageProfile.of(FrostDamageProfiles.FROST,
                tuning.getDouble(FrozenDepthsTuning.FROST_DECOY_DAMAGE_MULTIPLIER, 1.25), 4.0));
        world.spawnEntity(new FrostDecoyEntity(world, this, departure, this.getBodyYaw(), this.getScale(),
                tuning.getInt(FrozenDepthsTuning.FROST_DECOY_FUSE, 40),
                tuning.getDouble(FrozenDepthsTuning.FROST_DECOY_RADIUS, 3.0), damage));
        MrpgFx.play(world, LNEFx.FROST_BLINK, FxParams.at(departure.add(0.0, this.getHeight() * 0.5, 0.0)));
        this.getNavigation().stop();
        this.setVelocity(Vec3d.ZERO);
        this.requestTeleport(destination.x, destination.y, destination.z);
        this.faceTarget(threat);
        MrpgFx.play(world, LNEFx.FROST_BLINK, FxParams.at(destination.add(0.0, this.getHeight() * 0.5, 0.0)));
        blinkCooldown = Math.max(0, tuning.getInt(FrozenDepthsTuning.FROST_BLINK_COOLDOWN, 240)) + this.random.nextInt(BLINK_COOLDOWN_SPREAD);
        postActionCooldown = Math.max(postActionCooldown, 10);
        return true;
    }

    @Nullable
    private Vec3d findBlinkSpot(LivingEntity threat) {
        Vec3d away = this.getPos().subtract(threat.getPos()).multiply(1.0, 0.0, 1.0);
        if (away.lengthSquared() < 1.0E-4) {
            away = new Vec3d(this.random.nextDouble() - 0.5, 0.0, this.random.nextDouble() - 0.5);
        }
        Vec3d direction = away.normalize();
        for (double distance : BLINK_DISTANCES) {
            for (float turn : BLINK_TURNS) {
                Vec3d column = this.getPos().add(direction.rotateY(turn).multiply(distance));
                for (int dy : BLINK_HEIGHTS) {
                    Vec3d spot = standingSpot(column, this.getBlockY() + dy);
                    if (spot != null && spot.squaredDistanceTo(threat.getPos()) >= BLINK_MIN_TARGET_DISTANCE * BLINK_MIN_TARGET_DISTANCE) {
                        return spot;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    private Vec3d standingSpot(Vec3d column, int y) {
        World world = this.getWorld();
        BlockPos feet = BlockPos.ofFloored(column.x, y, column.z);
        BlockPos ground = feet.down();
        if (!world.getBlockState(ground).isSideSolidFullSquare(world, ground, Direction.UP)) {
            return null;
        }
        Vec3d spot = new Vec3d(column.x, feet.getY(), column.z);
        Box box = this.getDimensions(this.getPose()).getBoxAt(spot);
        return world.isSpaceEmpty(this, box) && !world.containsFluid(box) ? spot : null;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("PortalCooldown", portalCooldown);
        nbt.putInt("FrostBlinkCooldown", blinkCooldown);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        portalCooldown = nbt.getInt("PortalCooldown");
        blinkCooldown = nbt.getInt("FrostBlinkCooldown");
    }

    @Override
    protected float voicePitch() {
        return 0.7F;
    }

    @Override
    protected float getSoundVolume() {
        return 1.3F;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.26)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_SCALE, 1.5);
        if (LNEPlatform.isModLoaded("spell_power")) {
            builder = builder.add(SpellSchools.FROST.attributeEntry, 4.5);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            builder = builder.add(ThermooAttributes.MIN_TEMPERATURE, 0.0)
                    .add(ThermooAttributes.FROST_RESISTANCE, 0.0);
        }
        return builder;
    }

    @Override
    protected EntityAttributeConfig.Entry attributeFallback() {
        EntityAttributeConfig.Entry fallback = new EntityAttributeConfig.Entry()
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 100.0, 1.25, 1.5)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 5.0);
        if (LNEPlatform.isModLoaded("spell_power")) {
            fallback.set(SpellSchools.FROST.attributeEntry.getIdAsString(), 4.5);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.generalFrozenMagician(fallback);
    }
}
