package more_rpg_loot.item.relics;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.platform.LNEEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.tiny_config.ConfigManager;

public final class RelicLootInjection {
    private static ConfigManager<RelicLootConfig> config;

    private RelicLootInjection() {}

    public static void init() {
        try {
            config = new ConfigManager<RelicLootConfig>("relic_loot", RelicLootConfig.defaults())
                    .builder()
                    .setDirectory(RPGLoot.MOD_ID)
                    .sanitize(true)
                    .build();
            config.refresh();
            config.save();
        } catch (NoClassDefFoundError e) {
            RPGLoot.LOGGER.warn("[LootNExplore] tiny_config not found, relic loot injection disabled: {}", e.getMessage());
            return;
        }
        LNEEvents.get().onLootTableModify(RelicLootInjection::configure);
    }

    private static void configure(LNEEvents.LootTableModifyContext ctx) {
        if (config == null || !ctx.isBuiltin()) return;
        RelicLootConfig.Entry entry = config.value.entries.get(ctx.tableId().toString());
        if (entry == null || entry.items().isEmpty()) return;

        float rolls = entry.rolls() > 0 ? entry.rolls() : 1.0F;
        int attempts = (int) Math.ceil(rolls);
        float chance = rolls / attempts;

        LootPool.Builder pool = LootPool.builder()
                .rolls(BinomialLootNumberProvider.create(attempts, chance))
                .bonusRolls(ConstantLootNumberProvider.create(0));

        int added = 0;
        for (RelicLootConfig.Item item : entry.items()) {
            if (item.itemId() == null || item.itemId().isEmpty()) continue;
            Identifier itemId = Identifier.tryParse(item.itemId());
            if (itemId == null || !Registries.ITEM.containsId(itemId)) continue;
            pool.with(ItemEntry.builder(Registries.ITEM.get(itemId)).weight(Math.max(1, item.weight())));
            added++;
        }
        if (added > 0) {
            ctx.addPool(pool.build());
        }
    }
}
