package more_rpg_loot.entity.goals.frostmonarch;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.entity.mob.FrostMonarchServantEntity;
import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;

import java.util.EnumSet;
import java.util.List;

public class StayStillWhenServantsAliveGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int tickCounter = 0;

    public StayStillWhenServantsAliveGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return monarch.canHeal();
    }

    @Override
    public boolean shouldContinue() {
        return monarch.canHeal();
    }

    @Override
    public void start() {
        if (!monarch.getWorld().isClient) {
            monarch.getWorld().playSound(
                    null,
                    monarch.getX(), monarch.getY(), monarch.getZ(),
                    ModSounds.FROSTMONARCH_HEALING.soundEvent(),
                    SoundCategory.PLAYERS,
                    1.0f, 1.0f
            );
        }
        monarch.getNavigation().stop();
        ItemStack stack = ModBlocks.SOULFROST_LANTERN.item().getDefaultStack();
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            stack = LNE_Relics.FROZEN_SOUL.item().get().getDefaultStack();
        }
        monarch.setStackInHand(Hand.OFF_HAND, new ItemStack(stack.getItem()));
        monarch.setCurrentHand(Hand.OFF_HAND);
        tickCounter = 0;
    }

    @Override
    public void tick() {
        tickCounter++;
        List<FrostMonarchServantEntity> servants = monarch.getWorld().getNonSpectatingEntities(
                FrostMonarchServantEntity.class,
                monarch.getBoundingBox().expand(32.0)
        );
        for(Entity entities : servants){
            monarch.getLookControl().lookAt(entities, 10.0F, 10.0F);
        }


        if (monarch.getWorld().isClient) {
            double jitter = 0.25;
            double offsetX = (monarch.getRandom().nextDouble() - 0.5) * jitter;
            double offsetZ = (monarch.getRandom().nextDouble() - 0.5) * jitter;
            monarch.setPos(monarch.getX() + offsetX, monarch.getY(), monarch.getZ() + offsetZ);
        }

        if (!monarch.getWorld().isClient && tickCounter % 5 == 0) {
            ServerWorld serverWorld = (ServerWorld) monarch.getWorld();
            serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    monarch.getX(),
                    monarch.getY() + 1.5,
                    monarch.getZ(),
                    6,
                    0.4, 0.6, 0.4,
                    0.01
            );
        }
        monarch.setGlowing(true);
    }

    @Override
    public void stop() {
        monarch.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        monarch.setCurrentHand(Hand.MAIN_HAND);
        monarch.setGlowing(false);
    }
}