package more_rpg_loot.blocks.frozen_depths;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class FrozenChainBlock extends ChainBlock {
    public FrozenChainBlock(Settings settings) {
        super(settings);
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && world.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity instanceof PlayerEntity player && player.getAbilities().invulnerable) return;
            if (entity instanceof LivingEntity livingEntity) {
                if (!livingEntity.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                    livingEntity.setFrozenTicks(livingEntity.getFrozenTicks() + 1);
                }
            }
        }
    }
}
