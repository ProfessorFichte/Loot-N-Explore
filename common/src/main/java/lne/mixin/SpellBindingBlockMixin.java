package lne.mixin;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.spell_engine.spellbinding.SpellBindingBlock")
public class SpellBindingBlockMixin {
    @Inject(method = "canAccessBookshelf", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void loot_n_explore$anyPowerProvider(World world, BlockPos tablePos, BlockPos offset, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantingTableBlock.canAccessPowerProvider(world, tablePos, offset));
    }
}
