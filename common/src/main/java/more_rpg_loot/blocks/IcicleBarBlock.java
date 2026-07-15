package more_rpg_loot.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IcicleBarBlock extends PaneBlock {
    public IcicleBarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (!world.isClient && entity instanceof LivingEntity living) {
            if (entity instanceof PlayerEntity player && player.getAbilities().invulnerable) return;
            if (!living.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                living.setFrozenTicks(living.getFrozenTicks() + 2);
            }
        }
    }
}
