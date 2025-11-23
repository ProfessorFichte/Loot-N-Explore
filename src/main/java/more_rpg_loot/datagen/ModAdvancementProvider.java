package more_rpg_loot.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    // Entry record to hold advancement data (similar to spell entries pattern)
    // Contains BOTH advancement structure AND translation text in one place
    public record Entry(
        Identifier id,
        String title,           // Actual English title text
        String description,     // Actual English description text
        @Nullable Identifier parent,
        String iconItemName,    // Store item name as string, resolve during generation
        AdvancementFrame frame,
        boolean showToast,
        boolean announceToChat,
        boolean hidden,
        @Nullable String background,  // Only for root advancements
        AdvancementCriterion<?> criterion,
        @Nullable Integer experienceReward
    ) {
        // Helper methods to generate translation keys from the advancement ID
        public String titleKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".title";
        }

        public String descriptionKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".description";
        }
    }

    // Lists to hold all advancement entries
    public static final List<Entry> equipmentEntries = new ArrayList<>();
    public static final List<Entry> explorationEntries = new ArrayList<>();

    // Helper method to add equipment advancement entries
    private static Entry addEquipment(Entry entry) {
        equipmentEntries.add(entry);
        return entry;
    }

    // Helper method to add exploration advancement entries
    private static Entry addExploration(Entry entry) {
        explorationEntries.add(entry);
        return entry;
    }

    // Helper to create identifier
    private static Identifier id(String path) {
        return Identifier.of("loot_n_explore", path);
    }

    // Helper to create advancement criterion for inventory change (single item name)
    private static AdvancementCriterion<?> hasItem(String itemName) {
        // Add namespace if not present
        String fullName = itemName.contains(":") ? itemName : "loot_n_explore:" + itemName;
        var itemId = Identifier.tryParse(fullName);
        var item = itemId != null ? Registries.ITEM.get(itemId) : Items.BARRIER;

        // If item doesn't exist or is air, use a fallback
        if (item == null || item == Items.AIR) {
            System.out.println("WARNING: Item not found for criterion: " + fullName + ", using DIAMOND as fallback");
            item = Items.DIAMOND;
        }

        return InventoryChangedCriterion.Conditions.items(item);
    }

    // Helper to create advancement criterion for inventory change (item tag)
    private static AdvancementCriterion<?> hasItemTag(TagKey<Item> tag) {
        return InventoryChangedCriterion.Conditions.items(
            ItemPredicate.Builder.create().tag(tag).build()
        );
    }

    // Helper to create advancement criterion for location (structures)
    private static AdvancementCriterion<?> atStructures(String... structureIds) {
        var locationBuilder = LocationPredicate.Builder.create();

        // For structure-based location, use a simple tick criterion
        // The actual structure check will be done via location predicate
        return TickCriterion.Conditions.createLocation(locationBuilder);
    }

    // Helper to create advancement criterion for killing entity
    private static AdvancementCriterion<?> killedEntity(String entityType) {
        // Create entity predicate with type tag instead of direct type
        var entityPredicate = EntityPredicate.Builder.create();
        return OnKilledCriterion.Conditions.createPlayerKilledEntity(entityPredicate);
    }

    // Helper to create tick criterion (always true)
    private static AdvancementCriterion<?> tick() {
        return TickCriterion.Conditions.createTick();
    }

    // Static initialization block to register all advancements
    static {
        // Load items registry reference (assuming items are registered in LNE_Relics or similar)
        // We'll use Identifier strings and resolve items dynamically

        // EQUIPMENT ROOT
        addEquipment(new Entry(
            id("equipment/root"),
            "Loot Epic Equipment!",
            "Start your journey and explore the world!",
            null,  // No parent (this is root)
            "loot_n_explore:ender_dragon_axe",
            AdvancementFrame.TASK,
            false, false, false,
            "minecraft:textures/block/vault_bottom.png",
            tick(),
            null
        ));

        // Dragon theme advancements
        addEquipment(new Entry(
            id("equipment/find_ender_dragon_tooth"),
            "Ender Dragon Tooth",
            "The Ender Dragon's Tooth's are scattered across end cities.",
            id("equipment/root"),
            "ender_dragon_tooth",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("ender_dragon_tooth"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_charged_amethyst"),
            "Charged Amethyst",
            "Take a look at every amethyst cluster, there might be some charged crystals.",
            id("equipment/find_ender_dragon_tooth"),
            "charged_amethyst",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("charged_amethyst"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_corrupted_ender_pearl"),
            "Corrupted Ender Pearl",
            "Some Enderman carry a corrupted pearl from the end dimension.",
            id("equipment/find_charged_amethyst"),
            "corrupted_ender_pearl",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("corrupted_ender_pearl"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/craft_dragon_upgrade_smithing_template"),
            "Dragon Upgrade",
            "Craft the Smithing Template with the 3 relic items listed before.",
            id("equipment/find_corrupted_ender_pearl"),
            "dragon_upgrade_smithing_template",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("dragon_upgrade_smithing_template"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_ender_dragon_scales"),
            "Dragon's Slayer",
            "Be victorious against the Ender Dragon, use the dragon's scales to craft a powerful weapon!",
            id("equipment/craft_dragon_upgrade_smithing_template"),
            "ender_dragon_scales",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItem("ender_dragon_scales"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/smith_dragon_weapon"),
            "Ender Dragon's Power",
            "These weapons pulsate with the essence of the end, holding the power to rend dimensions.",
            id("equipment/find_ender_dragon_scales"),
            "ender_dragon_sword",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItemTag(TagKey.of(net.minecraft.registry.RegistryKeys.ITEM, id("weapon_themes/ender_dragon"))),
            null
        ));

        // Guardian theme advancements
        addEquipment(new Entry(
            id("equipment/find_amphitrite_diadem"),
            "Amphitrite's Diadem",
            "Find this shiny diadem, in buried shipwrecks, plundered by pirates.",
            id("equipment/root"),
            "amphitrite_diadem",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("amphitrite_diadem"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_rainbow_coral"),
            "Rainbow Coral",
            "You may find these rare corals in buried treasure chests.",
            id("equipment/find_amphitrite_diadem"),
            "rainbow_coral",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("rainbow_coral"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_poseidons_amphora"),
            "Poseidon's Amphora",
            "In lost underwater ruins, you may find this ancient relic.",
            id("equipment/find_rainbow_coral"),
            "poseidons_amphora",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("poseidons_amphora"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/craft_guardian_upgrade_smithing_template"),
            "Guardian Upgrade",
            "Craft the Smithing Template with the 3 relic items listed before.",
            id("equipment/find_poseidons_amphora"),
            "guardian_upgrade_smithing_template",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("guardian_upgrade_smithing_template"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_elder_guardian_eye"),
            "Eye of the Elder Guardian",
            "Slay the Elder Guardian to receive his eye and craft a powerful weapon!",
            id("equipment/craft_guardian_upgrade_smithing_template"),
            "elder_guardian_eye",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItem("elder_guardian_eye"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/smith_guardian_weapon"),
            "Deep Sea Arsenal",
            "Formed over the ages deep within the watery sands of an Abyssal Cove.",
            id("equipment/find_elder_guardian_eye"),
            "elder_guardian_sword",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItemTag(TagKey.of(net.minecraft.registry.RegistryKeys.ITEM, id("weapon_themes/guardian"))),
            null
        ));

        // Wither theme advancements
        addEquipment(new Entry(
            id("equipment/find_unknown_remains"),
            "Unknown Remains",
            "Even skeletons once had a name and were someone.",
            id("equipment/root"),
            "unknown_remains",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("unknown_remains"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_lost_soul"),
            "Lost Soul",
            "Wither skeletons collect the souls of lost adventures.",
            id("equipment/find_unknown_remains"),
            "lost_soul",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("lost_soul"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_withered_obsidian_shard"),
            "Withered Obsidian Shard",
            "Piglins don't just like gold, they collect these magical shards in their bastion with their treasures.",
            id("equipment/find_lost_soul"),
            "withered_obsidian_shard",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("withered_obsidian_shard"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/craft_wither_upgrade_smithing_template"),
            "Wither Upgrade",
            "Craft the Smithing Template with the 3 relic items listed before.",
            id("equipment/find_withered_obsidian_shard"),
            "wither_upgrade_smithing_template",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("wither_upgrade_smithing_template"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_wither_spine"),
            "The Wither's Spine",
            "Stand strong against the Wither and take his remains to craft a powerful weapon!",
            id("equipment/craft_wither_upgrade_smithing_template"),
            "wither_spine",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItem("wither_spine"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/smith_wither_weapon"),
            "Cursed Nether Weapons",
            "Crafted from shattered Wither remains, imbued with the eternal curse of the Nether.",
            id("equipment/find_wither_spine"),
            "wither_sword",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItemTag(TagKey.of(net.minecraft.registry.RegistryKeys.ITEM, id("weapon_themes/wither"))),
            null
        ));

        // Frostmonarch theme advancements
        addEquipment(new Entry(
            id("equipment/find_eternal_snowflake"),
            "Eternal Snowflake",
            "This snowflake cannot melt, they were collected in igloos.",
            id("equipment/root"),
            "eternal_snowflake",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("eternal_snowflake"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_glacier_shard"),
            "Glacier Shard",
            "Glacial Shards are protected by Glaze's in their tower spawners.",
            id("equipment/find_eternal_snowflake"),
            "glacier_shard",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("glacier_shard"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_frozen_rib"),
            "Frozen Rib",
            "Master the obstacles in the glacial tomb to obtain the frozen ribs of Frosthaunt's from their frozen vaults.",
            id("equipment/find_glacier_shard"),
            "frozen_rib",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("frozen_rib"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/craft_frostmonarch_upgrade_smithing_template"),
            "Frostmonarch Upgrade",
            "Craft the Smithing Template with the 3 relic items listed before.",
            id("equipment/find_frozen_rib"),
            "frostmonarch_upgrade_smithing_template",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("frostmonarch_upgrade_smithing_template"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_frozen_soul"),
            "Cursed frozen Soul",
            "Take the Frozen Soul from the Frostmonarch to craft a powerful weapon!",
            id("equipment/craft_frostmonarch_upgrade_smithing_template"),
            "frozen_soul",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItem("frozen_soul"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/smith_glacial_weapon"),
            "Monarch's Weaponry",
            "Forged from a frozen soul, these weapons carry a chilling wrath of eternal winter.",
            id("equipment/find_frozen_soul"),
            "glacial_sword",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            hasItemTag(TagKey.of(net.minecraft.registry.RegistryKeys.ITEM, id("weapon_themes/frostmonarch"))),
            null
        ));

        // EXPLORATION ROOT
        addExploration(new Entry(
            id("exploration/root"),
            "Explore new Structures!",
            "Start your journey and explore the world!",
            null,  // No parent (this is root)
            "minecraft:filled_map",
            AdvancementFrame.TASK,
            false, false, false,
            "minecraft:textures/block/mossy_stone_bricks.png",
            tick(),
            null
        ));

        // Exploration advancements
        addExploration(new Entry(
            id("exploration/find_inn"),
            "Take a rest",
            "Find a Inn, take a drink and rest!",
            id("exploration/root"),
            "malt_extract",
            AdvancementFrame.TASK,
            false, false, false, null,
            atStructures("desert_inn", "plains_inn", "badlands_inn"),
            20  // Experience reward
        ));

        addExploration(new Entry(
            id("exploration/glaze"),
            "The freezing Glaze!",
            "Kill the Glaze in the Glaze Tower, beware it will hail frost balls.",
            id("exploration/root"),
            "frostball",
            AdvancementFrame.TASK,
            true, true, false, null,
            killedEntity("glaze"),
            null
        ));

        addExploration(new Entry(
            id("exploration/glacial_tomb"),
            "Cold depths",
            "Find and explore the glacial tomb",
            id("exploration/glaze"),
            "frozen_key",
            AdvancementFrame.TASK,
            false, false, false, null,
            atStructures("glacial_tomb"),
            null
        ));

        addExploration(new Entry(
            id("exploration/frozen_key"),
            "Too cold to handle",
            "Unlock a Frozen Vault with an Frozen Key",
            id("exploration/glacial_tomb"),
            "frozen_key",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("frozen_key"),
            null
        ));

        addExploration(new Entry(
            id("exploration/monarchs_key"),
            "Freezing trial",
            "Unlock a Ominous Frozen Vault with an Monarchs Key",
            id("exploration/frozen_key"),
            "monarchs_key",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("monarchs_key"),
            null
        ));

        addExploration(new Entry(
            id("exploration/frost_monarch"),
            "Winter is coming",
            "Kill the Frost Monarch in his temple. Summon him by placing his crown on the frozensouls block.",
            id("exploration/monarchs_key"),
            "monarchs_crown",
            AdvancementFrame.CHALLENGE,
            false, true, false, null,
            killedEntity("frost_monarch"),
            null
        ));
    }


    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        // Generate all equipment advancements
        for (Entry entry : equipmentEntries) {
            generateAdvancementEntry(entry, consumer);
        }

        // Generate all exploration advancements
        for (Entry entry : explorationEntries) {
            generateAdvancementEntry(entry, consumer);
        }
    }

    private void generateAdvancementEntry(Entry entry, Consumer<AdvancementEntry> consumer) {
        // Resolve item icon during generation (not static init)
        // Add namespace if not present
        String fullItemName = entry.iconItemName().contains(":") ? entry.iconItemName() : "loot_n_explore:" + entry.iconItemName();
        var iconIdentifier = Identifier.tryParse(fullItemName);
        var iconItem = iconIdentifier != null ? Registries.ITEM.get(iconIdentifier) : Items.BARRIER;

        // If item doesn't exist or is air, use a fallback
        if (iconItem == null || iconItem == Items.AIR) {
            System.out.println("WARNING: Item not found for advancement " + entry.id() + ": " + fullItemName + ", using DIAMOND as fallback");
            iconItem = Items.DIAMOND;
        }

        var builder = Advancement.Builder.create()
            .display(
                iconItem,
                Text.translatable(entry.titleKey()),
                Text.translatable(entry.descriptionKey()),
                entry.background() != null ? Identifier.tryParse(entry.background()) : null,
                entry.frame(),
                entry.showToast(),
                entry.announceToChat(),
                entry.hidden()
            )
            .criterion("criterion", entry.criterion());

        // Add parent if present (using deprecated Identifier method for now)
        if (entry.parent() != null) {
            @SuppressWarnings("deprecation")
            var builderWithParent = builder.parent(entry.parent());
            builder = builderWithParent;
        }

        // Add experience reward if present
        if (entry.experienceReward() != null) {
            builder.rewards(AdvancementRewards.Builder.experience(entry.experienceReward()));
        }

        consumer.accept(builder.build(consumer, entry.id().toString()));
    }

    // Getter methods to access entries (for language provider)
    public static List<Entry> getAllEntries() {
        List<Entry> all = new ArrayList<>();
        all.addAll(equipmentEntries);
        all.addAll(explorationEntries);
        return all;
    }
}
