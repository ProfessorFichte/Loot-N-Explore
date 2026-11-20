package lne.compat;

import lne.internal.platform.LNEEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;

import java.util.Map;
import java.util.Set;

public final class CompatPotionLoot {
    private static final Set<String> POTION_MODS = Set.of("spell_power", "critical_strike", "ranged_weapon");
    private static final Map<String, Float> CHANCES = Map.of(
            "loot_n_explore:vaults/frozen_depths/t2/rare", 0.15F,
            "loot_n_explore:vaults/frozen_depths/t3/rare", 0.25F,
            "loot_n_explore:vaults/frozen_depths/t2/ominous_rare", 0.3F,
            "loot_n_explore:vaults/frozen_depths/t3/ominous_rare", 0.4F,
            "loot_n_explore:spawners/frozen_depths/t3/consumables", 0.15F,
            "loot_n_explore:spawners/frozen_depths/ominous/consumables", 0.4F
    );

    private CompatPotionLoot() {
    }

    public static void init() {
        LNEEvents.get().onLootTableModify(CompatPotionLoot::inject);
    }

    // Spell Power only registers its potions when its config allows it, so the list is read from the registry.
    private static void inject(LNEEvents.LootTableModifyContext ctx) {
        Float chance = CHANCES.get(ctx.tableId().toString());
        if (chance == null || !ctx.isBuiltin()) {
            return;
        }
        LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance));
        int added = 0;
        for (var potion : Registries.POTION.streamEntries().toList()) {
            if (POTION_MODS.contains(potion.registryKey().getValue().getNamespace())) {
                pool.with(ItemEntry.builder(Items.POTION).apply(SetPotionLootFunction.builder(potion)));
                added++;
            }
        }
        if (added > 0) {
            ctx.addPool(pool.build());
        }
    }
}
