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
import net.minecraft.entity.player.PlayerEntity;
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

    public ScreechGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
    }

    public float screechRange = 7.5F;
    public float screechImpactRange = 15.0F;

    @Override
    public boolean canStart() {
        if(monarch.getInvulnerableTimer() <= 0){
            LivingEntity target = monarch.getTarget();
            return monarch.screechCooldown <= 0 && !isCasting && target != null && target.isAlive() && !monarch.canHeal() &&
                    monarch.squaredDistanceTo(target) > screechRange * screechRange;
        }
        return false;
    }

    @Override
    public void start() {
        this.isCasting = true;
        this.channelTicks = 20;
        monarch.setScreeching(true);
    }


    private void screechAttack() {
        if (!monarch.getWorld().isClient()) {
            List<Entity> entities = monarch.getWorld().getOtherEntities(
                    monarch,
                    monarch.getBoundingBox().expand(screechImpactRange)
            );

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living && !(entity instanceof MobEntity)) {
                    monarch.setVelocity(Vec3d.ZERO);
                    living.damage(
                            monarch.getWorld().getDamageSources().indirectMagic(monarch, monarch),
                            (float) monarch.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)
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
        channelTicks--;
        if(this.isCasting){
            monarch.setVelocity(Vec3d.ZERO);
            List<PlayerEntity> playerEntities = monarch.getWorld().getNonSpectatingEntities(
                    PlayerEntity.class,
                    monarch.getBoundingBox().expand(screechRange)
            );
            for(Entity entities : playerEntities){
                monarch.getLookControl().lookAt(entities, 10.0F, 10.0F);
            }

        }
        if (monarch.screechCooldown > 0) {
            monarch.screechCooldown--;
        }
        if (channelTicks % 5 == 0) {
            screechAttack();
        }
    }

    @Override
    public boolean shouldContinue() {
        return channelTicks > 0 && monarch.screechCooldown <= 0;
    }

    @Override
    public void stop() {
        this.isCasting = false;
        this.channelTicks = 0;
        monarch.setScreeching(false);
        monarch.screechCooldown = 300;
    }
}
