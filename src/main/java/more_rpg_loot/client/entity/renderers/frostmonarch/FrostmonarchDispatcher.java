package more_rpg_loot.client.entity.renderers.frostmonarch;

import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;
import more_rpg_loot.entity.mob.FrostMonarchEntity;

public class FrostmonarchDispatcher {

    private final FrostMonarchEntity frosthaunt;

    // Commands for animations
    private static final AzCommand SPAWN_COMMAND = AzCommand.create("base_controller", "spawn", AzPlayBehaviors.PLAY_ONCE);
    private static final AzCommand DEATH_COMMAND = AzCommand.create("base_controller", "death", AzPlayBehaviors.PLAY_ONCE);
    private static final AzCommand IDLE_COMMAND = AzCommand.create("base_controller", "idle", AzPlayBehaviors.LOOP);
    private static final AzCommand WALK_COMMAND = AzCommand.create("base_controller", "walk", AzPlayBehaviors.LOOP);
    private static final AzCommand ATTACK_COMMAND = AzCommand.create("attack_controller", "attack", AzPlayBehaviors.PLAY_ONCE);
    private static final AzCommand SCREECH_COMMAND = AzCommand.create("attack_controller", "screech", AzPlayBehaviors.PLAY_ONCE);
    private static final AzCommand SUMMON_COMMAND = AzCommand.create("attack_controller", "summon", AzPlayBehaviors.PLAY_ONCE);

    public FrostmonarchDispatcher(FrostMonarchEntity frosthaunt) {
        this.frosthaunt = frosthaunt;
    }

    // Trigger idle animation
    public void spawn() {
        SPAWN_COMMAND.sendForEntity(frosthaunt);
    }
    public void death() {
        DEATH_COMMAND.sendForEntity(frosthaunt);
    }
    public void idle() {
        IDLE_COMMAND.sendForEntity(frosthaunt);
    }

    // Trigger walk animation
    public void walk() {
        WALK_COMMAND.sendForEntity(frosthaunt);
    }

    // Trigger attack animation
    public void attack() {
        ATTACK_COMMAND.sendForEntity(frosthaunt);
    }
    public void screech() {
        SCREECH_COMMAND.sendForEntity(frosthaunt);
    }
    public void summon() {
        SUMMON_COMMAND.sendForEntity(frosthaunt);
    }

    // Optional: helper to choose correct animation based on entity state
    public void updateAnimation() {
        if (frosthaunt.isAttacking()) {
            attack();
        } else if (frosthaunt.getVelocity().horizontalLengthSquared() > 0.01) {
            walk();
        } else {
            idle();
        }
    }
}