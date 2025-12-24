package more_rpg_loot.entity.goals.frostmonarch;

import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.effect.SpellEngineEffects;

import java.util.List;

public class ScreechGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;
    private boolean hasStarted;
    private LivingEntity cachedTarget;

    public static final float SCREECH_MIN_DISTANCE = 10.0F;
    public static final float SCREECH_RADIUS = 40.0F;

    public ScreechGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(java.util.EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if(!monarch.isPerformingAbility() && !isCasting){
            LivingEntity target = monarch.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            }
            if (monarch.screechCooldown > 0) {
                return false;
            }

            boolean canSeeTarget = monarch.getVisibilityCache().canSee(target);
            if (!canSeeTarget) {
                return false;
            }

            double distanceSq = monarch.squaredDistanceTo(target);
            if (distanceSq > SCREECH_MIN_DISTANCE * SCREECH_MIN_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.cachedTarget = monarch.getTarget();
        this.isCasting = true;
        this.channelTicks = 10;
        this.hasStarted = false;
        monarch.setScreeching(true);
        monarch.setCasting(true);

        LivingEntity target = cachedTarget;
        if (target != null) {
            double dx = target.getX() - monarch.getX();
            double dz = target.getZ() - monarch.getZ();
            float yaw = (float)(net.minecraft.util.math.MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;

            monarch.setYaw(yaw);
            monarch.setHeadYaw(yaw);
            monarch.setBodyYaw(yaw);
            monarch.prevYaw = yaw;
            monarch.prevHeadYaw = yaw;
            monarch.prevBodyYaw = yaw;
        }
    }


    private void screechAttack() {
        if (!monarch.getWorld().isClient()) {
            List<Entity> entities = monarch.getWorld().getOtherEntities(
                    monarch,
                    monarch.getBoundingBox().expand(SCREECH_RADIUS)
            );

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living && !(entity instanceof MobEntity)) {
                    monarch.setVelocity(Vec3d.ZERO);

                    float baseDamage = (float) monarch.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.1F;
                    float scaledDamage = monarch.getScaledDamage(baseDamage);

                    living.damage(
                            monarch.getWorld().getDamageSources().indirectMagic(monarch, monarch),
                            scaledDamage
                    );
                    double dx = monarch.getX() - entity.getX();
                    double dz = monarch.getZ() - entity.getZ();
                    float knockbackResistanceLiving = (float) living.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                    living.takeKnockback(1.5F - knockbackResistanceLiving, dx, dz);

                    if (!entity.getWorld().isClient() && entity.getWorld() instanceof ServerWorld serverWorld) {
                        double radius = 5.0;
                        int count  = 3;
                        Vec3d pos = monarch.getPos();

                        for (int i = 0; i < count; i++) {
                            double angle = 2 * Math.PI * i / count;
                            double x = pos.x + radius * Math.cos(angle);
                            double z = pos.z + radius * Math.sin(angle);
                            double y = pos.y + 1.0;
                            serverWorld.spawnParticles(
                                    ParticleTypes.SONIC_BOOM,
                                    x, y, z, 1, 0, 0, 0, 0);
                        }
                    }
                    if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
                        living.addStatusEffect(new StatusEffectInstance(SpellEngineEffects.STUN.entry, 40, 0));
                    } else {
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80, 1));
                    }
                }
            }

            monarch.getWorld().playSound(
                    null,
                    monarch.getX(), monarch.getY(), monarch.getZ(),
                    ModSounds.FROSTMONARCH_SCREECH.soundEvent(),
                    SoundCategory.PLAYERS,
                    2.5f, 1.0f
            );
        }
    }


    @Override
    public void tick() {
        if (!hasStarted) {
            hasStarted = true;
        }

        if (!monarch.isScreeching()) {
            monarch.setScreeching(true);
        }
        if (!monarch.isCasting()) {
            monarch.setCasting(true);
        }

        if(this.isCasting){
            monarch.setVelocity(Vec3d.ZERO);

            LivingEntity target = this.cachedTarget;
            if (target != null && target.isAlive()) {
                double dx = target.getX() - monarch.getX();
                double dz = target.getZ() - monarch.getZ();
                float yaw = (float)(net.minecraft.util.math.MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;

                monarch.setYaw(yaw);
                monarch.setHeadYaw(yaw);
                monarch.setBodyYaw(yaw);
            }
        }
        if (monarch.screechCooldown > 0) {
            monarch.screechCooldown--;
        }

        if (channelTicks == 5) {
            screechAttack();
        }

        channelTicks--;
    }

    @Override
    public boolean shouldContinue() {
        return !hasStarted || channelTicks > 0;
    }

    @Override
    public void stop() {
        this.isCasting = false;
        this.channelTicks = 0;
        this.hasStarted = false;
        this.cachedTarget = null;
        monarch.setScreeching(false);
        monarch.setCasting(false);

        monarch.screechCooldown = monarch.getScaledCooldown(300);
        monarch.globalAbilityCooldown = 20;
    }
}
