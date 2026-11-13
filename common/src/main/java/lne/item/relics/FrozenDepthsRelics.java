package lne.item.relics;

import lne.item.relics.LNE_RelicItems.Entry;

import java.util.List;

import static lne.item.relics.LNE_RelicItems.ARMOR;
import static lne.item.relics.LNE_RelicItems.ATTACK_SPEED;
import static lne.item.relics.LNE_RelicItems.MAX_HEALTH;
import static lne.item.relics.LNE_RelicItems.SPELL_FROST;
import static lne.item.relics.LNE_RelicItems.SPELL_HASTE;
import static lne.item.relics.LNE_RelicItems.SPELL_SOUL;
import static lne.item.relics.LNE_RelicItems.TOUGHNESS;
import static lne.item.relics.LNE_RelicItems.flat;
import static lne.item.relics.LNE_RelicItems.percent;

public class FrozenDepthsRelics {
    public static final Entry ETERNAL_SNOWFLAKE = new Entry(2, "eternal_snowflake")
            .title("Eternal Snowflake")
            .lore("This snowflake cannot melt, they were collected in igloos.")
            .bonuses(flat(ARMOR, 1), percent(SPELL_FROST, 0.05));
    public static final Entry GLACIER_SHARD = new Entry(2, "glacier_shard")
            .title("Glacier Shard")
            .lore("Glacial Shards are protected by Glaze's in their tower spawners.")
            .bonuses(flat(ARMOR, 2), flat(TOUGHNESS, 1));
    public static final Entry FROZEN_RIB = new Entry(3, "frozen_rib")
            .title("Frozen Rib")
            .lore("Master the obstacles in the glacial tomb to obtain the frozen ribs of the Frost Haunted from their frozen vaults.")
            .bonuses(percent(ATTACK_SPEED, 0.05), percent(SPELL_FROST, 0.07), percent(SPELL_HASTE, 0.05));
    public static final Entry FROZEN_SOUL = new Entry(4, "frozen_soul")
            .title("Frozen Soul")
            .lore("Take the Frozen Soul from the Frostmonarch to craft a powerful weapon!")
            .bonuses(flat(MAX_HEALTH, 4), percent(SPELL_FROST, 0.10), percent(SPELL_SOUL, 0.10))
            .spell("frozen_soul");

    public static void register() {
        int after = LNE_RelicItems.entries.indexOf(LNE_RelicItems.WITHER_SPINE) + 1;
        LNE_RelicItems.entries.addAll(after, List.of(ETERNAL_SNOWFLAKE, GLACIER_SHARD, FROZEN_RIB, FROZEN_SOUL));
    }
}
