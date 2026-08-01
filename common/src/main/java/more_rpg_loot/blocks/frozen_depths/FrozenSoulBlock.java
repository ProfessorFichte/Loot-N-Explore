package more_rpg_loot.blocks.frozen_depths;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.sounds.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FrozenSoulBlock extends Block {
    private static final int MIN_AMBIENT_DELAY = 300;
    private static final int MAX_AMBIENT_DELAY = 500;

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

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient && !oldState.isOf(this)) {
            world.scheduleBlockTick(pos, this, nextAmbientDelay(world.random));
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);

        world.playSound(null, pos, ModSounds.BLOCK_FROZEN_SOUL_AMBIENT.soundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.spawnParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, 0.3, 0.3, 0.3, 0.01);
        world.spawnParticles(Particles.FREEZING_SNOWFLAKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, 0.3, 0.3, 0.3, 0.01);

        world.scheduleBlockTick(pos, this, nextAmbientDelay(random));
    }

    private static int nextAmbientDelay(Random random) {
        return MIN_AMBIENT_DELAY + random.nextBetween(0, MAX_AMBIENT_DELAY - MIN_AMBIENT_DELAY);
    }
}
