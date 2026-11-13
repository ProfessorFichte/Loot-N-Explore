package lne.mixin;

import lne.blocks.themed.ThemedTrialSpawnerBlock;
import net.minecraft.block.spawner.TrialSpawnerConfig;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.loot.LootTables;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TrialSpawnerData.class)
public class TrialSpawnerDataMixin {

    @ModifyVariable(method = "getItemsToDropWhenOminous", at = @At("HEAD"), argsOnly = true, require = 0)
    private TrialSpawnerConfig lne$themedOminousItems(TrialSpawnerConfig config, ServerWorld world, TrialSpawnerConfig sameConfig, BlockPos pos) {
        if (!(world.getBlockState(pos).getBlock() instanceof ThemedTrialSpawnerBlock spawner)
                || !spawner.theme().replacesOminousItems()
                || !config.itemsToDropWhenOminous().equals(LootTables.TRIAL_CHAMBER_ITEMS_TO_DROP_WHEN_OMINOUS_SPAWNER)) {
            return config;
        }
        return new TrialSpawnerConfig(config.spawnRange(), config.totalMobs(), config.simultaneousMobs(),
                config.totalMobsAddedPerPlayer(), config.simultaneousMobsAddedPerPlayer(), config.ticksBetweenSpawn(),
                config.spawnPotentialsDefinition(), config.lootTablesToEject(), spawner.theme().ominousItems());
    }
}
