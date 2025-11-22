package more_rpg_loot.client.entity.renderers.frosthaunt;

import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import more_rpg_loot.entity.mob.FrosthauntEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class FrosthauntAnimatior extends AzEntityAnimator<FrosthauntEntity> {
    private static final Identifier ANIMATIONS = Identifier.of(
            MOD_ID,
            "animations/entity/frosthaunt.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<FrosthauntEntity> animationControllerContainer) {
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
    public @NotNull Identifier getAnimationLocation(FrosthauntEntity animatable) {
        return ANIMATIONS;
    }
}
