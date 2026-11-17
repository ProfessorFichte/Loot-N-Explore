package lne.internal.ability;

import com.mrpg_lib.util.AllyHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class RelicTargets {
    private RelicTargets() {
    }

    public static boolean isEnemy(LivingEntity user, LivingEntity target) {
        return target.isAlive() && isEnemyType(user, target);
    }

    public static boolean isEnemyType(LivingEntity user, LivingEntity target) {
        if (target == user || target instanceof ArmorStandEntity || target instanceof PlayerEntity) {
            return false;
        }
        if (!AllyHelper.canHurt(user, target)) {
            return false;
        }
        return target instanceof Monster || target instanceof MobEntity mob && mob.getTarget() == user;
    }

    public static List<LivingEntity> enemiesAround(ServerWorld world, LivingEntity user, Vec3d center, double radius) {
        var box = new Box(center.x - radius, center.y - radius, center.z - radius, center.x + radius, center.y + radius, center.z + radius);
        double radiusSquared = radius * radius;
        return world.getEntitiesByClass(LivingEntity.class, box,
                entity -> entity.squaredDistanceTo(center) <= radiusSquared && isEnemy(user, entity));
    }

    public static void magicDamage(ServerWorld world, LivingEntity user, LivingEntity target, float amount) {
        RelicTriggers.runSuppressed(() -> target.damage(world.getDamageSources().indirectMagic(user, user), amount));
    }
}
