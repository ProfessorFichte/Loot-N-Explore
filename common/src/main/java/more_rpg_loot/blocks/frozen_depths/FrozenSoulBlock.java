package more_rpg_loot.blocks.frozen_depths;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.particle.Particles;
import more_rpg_loot.sounds.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
        var held = player.getMainHandStack();
        if (!held.isOf(ModBlocks.MONARCHS_CROWN.item()) && !(held.getItem() instanceof BlockItem)) {
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
        world.scheduleBlockTick(pos, this, nextAmbientDelay(random));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(4) != 0) {
            return;
        }
        Direction direction = Direction.random(random);
        BlockPos neighbor = pos.offset(direction);
        if (world.getBlockState(neighbor).isOpaqueFullCube(world, neighbor)) {
            return;
        }
        double x = pos.getX() + 0.5 + direction.getOffsetX() * 0.6 + (direction.getAxis() == Direction.Axis.X ? 0.0 : random.nextDouble() - 0.5);
        double y = pos.getY() + 0.5 + direction.getOffsetY() * 0.6 + (direction.getAxis() == Direction.Axis.Y ? 0.0 : random.nextDouble() - 0.5);
        double z = pos.getZ() + 0.5 + direction.getOffsetZ() * 0.6 + (direction.getAxis() == Direction.Axis.Z ? 0.0 : random.nextDouble() - 0.5);
        double vx = direction.getOffsetX() * 0.01 + (random.nextDouble() - 0.5) * 0.01;
        double vy = 0.015 + direction.getOffsetY() * 0.01;
        double vz = direction.getOffsetZ() * 0.01 + (random.nextDouble() - 0.5) * 0.01;
        world.addParticle(random.nextInt(3) == 0 ? ParticleTypes.SOUL : Particles.FROZEN_SOUL_MOTE, x, y, z, vx, vy, vz);
    }

    private static int nextAmbientDelay(Random random) {
        return MIN_AMBIENT_DELAY + random.nextBetween(0, MAX_AMBIENT_DELAY - MIN_AMBIENT_DELAY);
    }
}
