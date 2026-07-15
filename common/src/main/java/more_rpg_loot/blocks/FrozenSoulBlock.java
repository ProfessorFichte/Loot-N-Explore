package more_rpg_loot.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FrozenSoulBlock extends Block {
    public FrozenSoulBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getMainHandStack().isOf(ModBlocks.MONARCHS_CROWN.item())) {
            if (!world.isClient) {
                player.sendMessage(Text.translatable("block.loot_n_explore.frozen_soul_block.hint"), true);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
