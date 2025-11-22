package more_rpg_loot.client.entity.renderers.frostmonarch;

import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class FrostmonarchAnimatior extends AzEntityAnimator<FrostMonarchEntity> {
    private static final Identifier ANIMATIONS = Identifier.of(
            MOD_ID,
            "animations/entity/frostmonarch.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<FrostMonarchEntity> animationControllerContainer) {
        animationControllerContainer.add(
                AzAnimationController.builder(this, "base_controller")
                        .build()
        );
        animationControllerContainer.add(
                AzAnimationController.builder(this, "attack_controller")
                        .build()
        );
    }

    @Override
    public @NotNull Identifier getAnimationLocation(FrostMonarchEntity animatable) {
        return ANIMATIONS;
    }
}
