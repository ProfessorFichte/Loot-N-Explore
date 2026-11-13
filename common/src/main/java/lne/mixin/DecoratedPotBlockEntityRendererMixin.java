package lne.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import lne.blocks.frozen_depths.FrozenDecoratedPotBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.DecoratedPotBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.EnumMap;
import java.util.Map;

import static lne.LneMod.MOD_ID;

@Mixin(DecoratedPotBlockEntityRenderer.class)
public abstract class DecoratedPotBlockEntityRendererMixin {
    @Unique
    private static final String RENDER = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V";
    @Unique
    private static final String SHERD_TEXTURE = "Lnet/minecraft/client/render/block/entity/DecoratedPotBlockEntityRenderer;getTextureIdFromSherd(Ljava/util/Optional;)Lnet/minecraft/client/util/SpriteIdentifier;";
    @Unique
    private static final SpriteIdentifier LNE_FROZEN_BASE = new SpriteIdentifier(TexturedRenderLayers.DECORATED_POT_ATLAS_TEXTURE,
            Identifier.of(MOD_ID, "entity/decorated_pot/frozen_decorated_pot_base"));
    @Unique
    private static final Map<FrozenDecoratedPotBlock.Pattern, SpriteIdentifier> LNE_FROZEN_SIDES = new EnumMap<>(FrozenDecoratedPotBlock.Pattern.class);

    static {
        for (var pattern : FrozenDecoratedPotBlock.Pattern.values()) {
            if (pattern.texture == null) continue;
            LNE_FROZEN_SIDES.put(pattern, new SpriteIdentifier(TexturedRenderLayers.DECORATED_POT_ATLAS_TEXTURE,
                    Identifier.of(MOD_ID, "entity/decorated_pot/" + pattern.texture)));
        }
    }

    @ModifyExpressionValue(method = RENDER,
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/TexturedRenderLayers;DECORATED_POT_BASE:Lnet/minecraft/client/util/SpriteIdentifier;"))
    private SpriteIdentifier lne$frozenPotBase(SpriteIdentifier original, @Local(argsOnly = true) DecoratedPotBlockEntity pot) {
        return pot.getCachedState().getBlock() instanceof FrozenDecoratedPotBlock ? LNE_FROZEN_BASE : original;
    }

    @ModifyExpressionValue(method = RENDER, at = @At(value = "INVOKE", target = SHERD_TEXTURE, ordinal = 0))
    private SpriteIdentifier lne$frozenPotSide0(SpriteIdentifier original, @Local(argsOnly = true) DecoratedPotBlockEntity pot) {
        return lne$frozenSide(original, pot, 0);
    }

    @ModifyExpressionValue(method = RENDER, at = @At(value = "INVOKE", target = SHERD_TEXTURE, ordinal = 1))
    private SpriteIdentifier lne$frozenPotSide1(SpriteIdentifier original, @Local(argsOnly = true) DecoratedPotBlockEntity pot) {
        return lne$frozenSide(original, pot, 1);
    }

    @ModifyExpressionValue(method = RENDER, at = @At(value = "INVOKE", target = SHERD_TEXTURE, ordinal = 2))
    private SpriteIdentifier lne$frozenPotSide2(SpriteIdentifier original, @Local(argsOnly = true) DecoratedPotBlockEntity pot) {
        return lne$frozenSide(original, pot, 2);
    }

    @ModifyExpressionValue(method = RENDER, at = @At(value = "INVOKE", target = SHERD_TEXTURE, ordinal = 3))
    private SpriteIdentifier lne$frozenPotSide3(SpriteIdentifier original, @Local(argsOnly = true) DecoratedPotBlockEntity pot) {
        return lne$frozenSide(original, pot, 3);
    }

    @Unique
    private static SpriteIdentifier lne$frozenSide(SpriteIdentifier original, DecoratedPotBlockEntity pot, int side) {
        BlockState state = pot.getCachedState();
        if (original != TexturedRenderLayers.DECORATED_POT_SIDE || !(state.getBlock() instanceof FrozenDecoratedPotBlock)) return original;
        return LNE_FROZEN_SIDES.get(state.get(FrozenDecoratedPotBlock.PATTERN).forSide(pot.getPos(), side));
    }
}
