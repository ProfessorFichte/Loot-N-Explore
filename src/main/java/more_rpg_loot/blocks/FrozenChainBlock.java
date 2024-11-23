package more_rpg_loot.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
            if (entity instanceof LivingEntity livingEntity) {
                EntityType<?> type = entity.getType();
                if (!type.isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                    livingEntity.setFrozenTicks(livingEntity.getFrozenTicks() + 3);
                }
            }

        }
    }
}
