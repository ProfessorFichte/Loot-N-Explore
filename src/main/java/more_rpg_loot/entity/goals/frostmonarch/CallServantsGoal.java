package more_rpg_loot.entity.goals.frostmonarch;

import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.entity.mob.FrostMonarchServantEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;
import java.util.List;

public class CallServantsGoal extends Goal {
    private final FrostMonarchEntity monarch;

    public CallServantsGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.TARGET, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if(monarch.getInvulnerableTimer() <= 0){
            LivingEntity target = monarch.getTarget();
            if (target == null || !monarch.getVisibilityCache().canSee(target)) return false;

            float healthPct = monarch.getHealth() / monarch.getMaxHealth();
            if (healthPct > 0.75F) return false;

            if (monarch.callServantsCooldown > 0) return false;

            List<FrostMonarchServantEntity> servants = monarch.getWorld().getNonSpectatingEntities(
                    FrostMonarchServantEntity.class,
                    monarch.getBoundingBox().expand(32.0)
            );
            return servants.stream().filter(FrostMonarchServantEntity::isAlive).count() < 5 && monarch.callServantsMax > 0;
        }
        return false;
    }

    @Override
    public void start() {
        monarch.startCallingServants();
        spawnServant();

    }

    @Override
    public void tick() {
        if (monarch.callServantsCooldown > 0) {
            monarch.callServantsCooldown--;
        }

        List<FrostMonarchServantEntity> servants = monarch.getWorld().getNonSpectatingEntities(
                FrostMonarchServantEntity.class,
                monarch.getBoundingBox().expand(32.0)
        );

        long aliveServants = servants.stream().filter(FrostMonarchServantEntity::isAlive).count();

        if (aliveServants == 0 && monarch.callServantsMax <= 0) {
            monarch.callServantsMax = 5;
        }

        if (monarch.callServantsCooldown <= 0 && aliveServants < 5 && monarch.callServantsMax > 0) {
            spawnServant();
        }
    }

    private void spawnServant() {
        float healthPct = monarch.getHealth() / monarch.getMaxHealth();
        ServerWorld world = (ServerWorld) monarch.getWorld();

        Random rand = monarch.getRandom();
        float offsetX = (rand.nextFloat() - 0.5F) * 3.0F;
        float offsetZ = (rand.nextFloat() - 0.5F) * 3.0F;

        FrostMonarchServantEntity servant = ModEntities.MONARCH_SERVANT.create(world);
        if (servant != null) {
            servant.setPosition(monarch.getX() + offsetX, monarch.getY(), monarch.getZ() + offsetZ);
            servant.equipStack(EquipmentSlot.MAINHAND, monarch.getWeaponForDifficulty());
            world.spawnEntity(servant);

            monarch.callServantsMax--;

            if (healthPct <= 0.75F && healthPct > 0.5F && monarch.callServantsMax == 0) {
                monarch.callServantsCooldown = 300;
                monarch.callServantsMax = 3;
            } else if (healthPct <= 0.5F && healthPct > 0.25F&& monarch.callServantsMax == 0) {
                monarch.callServantsCooldown = 250;
                monarch.callServantsMax = 1;
            } else if (healthPct <= 0.25F && monarch.callServantsMax == 0) {
                monarch.callServantsCooldown = 200;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        long aliveServants = monarch.getWorld().getNonSpectatingEntities(FrostMonarchServantEntity.class, monarch.getBoundingBox().expand(32.0))
                .stream().filter(FrostMonarchServantEntity::isAlive).count();

        return monarch.callServantsCooldown <= 0
                && monarch.callServantsMax > 0
                && aliveServants < 5;
    }
}