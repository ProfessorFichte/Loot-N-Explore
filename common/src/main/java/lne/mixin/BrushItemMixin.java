package lne.mixin;

import lne.blocks.generic.SuspiciousBlock;
import lne.particle.Particles;
import net.minecraft.block.BlockState;
import net.minecraft.item.BrushItem;
import net.minecraft.util.Arm;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public abstract class BrushItemMixin {

    @Inject(method = "addDustParticles", at = @At("HEAD"), cancellable = true)
    private void lne$bookshelfDust(World world, BlockHitResult hitResult, BlockState state, Vec3d userRotation, Arm arm, CallbackInfo ci) {
        if (!(state.getBlock() instanceof SuspiciousBlock.Sideways)) return;
        Random random = world.getRandom();
        Direction side = hitResult.getSide();
        Vec3d pos = hitResult.getPos();
        int count = random.nextBetweenExclusive(4, 8);
        for (int i = 0; i < count; i++) {
            double x = pos.x + side.getOffsetX() * 0.02 + (random.nextDouble() - 0.5) * 0.3;
            double y = pos.y + (random.nextDouble() - 0.5) * 0.3;
            double z = pos.z + side.getOffsetZ() * 0.02 + (random.nextDouble() - 0.5) * 0.3;
            double speed = 0.02 + random.nextDouble() * 0.03;
            world.addParticle(Particles.BOOK_DUST, x, y, z,
                    side.getOffsetX() * speed + (random.nextDouble() - 0.5) * 0.02,
                    side.getOffsetY() * speed + random.nextDouble() * 0.01,
                    side.getOffsetZ() * speed + (random.nextDouble() - 0.5) * 0.02);
        }
        ci.cancel();
    }
}
