package lne.entity.relic;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.fx.LNEFx;
import lne.ability.RelicTargets;
import lne.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RelicWitherSkullEntity extends WitherSkullEntity {
    private static final int MAX_AGE = 80;

    private float damage;
    private double radius = 3.5;
    private int witherTicks = 100;
    private int witherAmplifier = 1;

    public RelicWitherSkullEntity(EntityType<? extends RelicWitherSkullEntity> type, World world) {
        super(type, world);
    }

    public RelicWitherSkullEntity(World world, LivingEntity owner, Vec3d direction, float damage, double radius, int witherTicks, int witherAmplifier) {
        super(ModEntities.RELIC_WITHER_SKULL, world);
        this.damage = damage;
        this.radius = radius;
        this.witherTicks = witherTicks;
        this.witherAmplifier = witherAmplifier;
        setOwner(owner);
        Vec3d normalized = direction.normalize();
        setPosition(owner.getX() + normalized.x * 0.8, owner.getEyeY() - 0.2 + normalized.y * 0.8, owner.getZ() + normalized.z * 0.8);
        setVelocity(normalized.multiply(0.6));
        accelerationPower = 0.08;
        velocityDirty = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient && age > MAX_AGE) {
            discard();
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!(getWorld() instanceof ServerWorld world)) return;
        if (getOwner() instanceof LivingEntity owner) {
            Vec3d center = getPos();
            for (var enemy : RelicTargets.enemiesAround(world, owner, center, radius)) {
                RelicTargets.magicDamage(world, owner, enemy, damage);
                enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, witherTicks, witherAmplifier), owner);
            }
            MrpgFx.play(world, LNEFx.SPINE_BLAST, FxParams.at(center).radius((float) radius));
        }
        discard();
    }

    @Override
    public boolean damage(net.minecraft.entity.damage.DamageSource source, float amount) {
        return false;
    }
}
