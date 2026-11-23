package lne.datagen;

import lne.blocks.FrozenDepthsBlocks;
import lne.util.LneItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.block.VaultBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityTypePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import com.mojang.serialization.JsonOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    @FunctionalInterface
    public interface Criteria {
        Map<String, AdvancementCriterion<?>> resolve(RegistryWrapper.WrapperLookup lookup);
    }

    public record Entry(
        Identifier id,
        String title,
        String description,
        @Nullable Identifier parent,
        String iconItemName,
        AdvancementFrame frame,
        boolean showToast,
        boolean announceToChat,
        boolean hidden,
        @Nullable String background,
        Criteria criteria,
        @Nullable Integer experienceReward
    ) {
        public String titleKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".title";
        }

        public String descriptionKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".description";
        }
    }

    public static final List<Entry> equipmentEntries = new ArrayList<>();
    public static final List<Entry> explorationEntries = new ArrayList<>();
    public static final List<Entry> frozenDepthsEntries = new ArrayList<>();

    private static Entry addEquipment(Entry entry) {
        equipmentEntries.add(entry);
        return entry;
    }

    private static Entry addExploration(Entry entry) {
        explorationEntries.add(entry);
        return entry;
    }

    private static Entry addFrozenDepths(Entry entry) {
        frozenDepthsEntries.add(entry);
        return entry;
    }

    private static Identifier id(String path) {
        return Identifier.of("loot_n_explore", path);
    }

    private static Criteria single(AdvancementCriterion<?> criterion) {
        return lookup -> Map.of("criterion", criterion);
    }

    private static Item item(String itemName) {
        String fullName = itemName.contains(":") ? itemName : "loot_n_explore:" + itemName;
        var itemId = Identifier.tryParse(fullName);
        var item = itemId != null ? Registries.ITEM.get(itemId) : Items.AIR;
        if (item == Items.AIR) {
            throw new IllegalStateException("Item not found for criterion: " + fullName);
        }
        return item;
    }

    private static Criteria hasItem(String itemName) {
        return single(InventoryChangedCriterion.Conditions.items(item(itemName)));
    }

    private static Criteria hasItemTag(TagKey<Item> tag) {
        return single(InventoryChangedCriterion.Conditions.items(
            ItemPredicate.Builder.create().tag(tag).build()
        ));
    }

    private static Criteria atStructures(String... structurePaths) {
        return lookup -> {
            var structures = lookup.getWrapperOrThrow(RegistryKeys.STRUCTURE);
            RegistryEntryOwner<Structure> owner = lookup.getOps(JsonOps.INSTANCE).getOwner(RegistryKeys.STRUCTURE).orElse(structures);
            Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
            for (String path : structurePaths) {
                RegistryKey<Structure> key = RegistryKey.of(RegistryKeys.STRUCTURE, id(path));
                // The datagen registry only holds code-registered structures; ours are datapack JSON, so encode the bare id.
                RegistryEntry<Structure> structure = structures.getOptional(key)
                        .<RegistryEntry<Structure>>map(entry -> entry)
                        .orElseGet(() -> RegistryEntry.Reference.standAlone(owner, key));
                criteria.put(path.substring(path.lastIndexOf('/') + 1),
                        TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createStructure(structure)));
            }
            return criteria;
        };
    }

    private static Criteria killedAny(String... entityTypes) {
        Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
        for (String entityType : entityTypes) {
            var type = Registries.ENTITY_TYPE.getOrEmpty(id(entityType))
                    .orElseThrow(() -> new IllegalStateException("Entity not found for criterion: " + entityType));
            criteria.put(entityType, OnKilledCriterion.Conditions.createPlayerKilledEntity(
                    EntityPredicate.Builder.create().type(EntityTypePredicate.create(type))));
        }
        return lookup -> criteria;
    }

    private static Criteria interactedWithVillager(String profession) {
        NbtCompound villagerData = new NbtCompound();
        villagerData.putString("profession", profession);
        NbtCompound nbt = new NbtCompound();
        nbt.put("VillagerData", villagerData);
        return single(PlayerInteractedWithEntityCriterion.Conditions.create(ItemPredicate.Builder.create(),
                Optional.of(EntityPredicate.contextPredicateFromEntityPredicate(EntityPredicate.Builder.create()
                        .type(EntityTypePredicate.create(EntityType.VILLAGER))
                        .nbt(new NbtPredicate(nbt))))));
    }

    private static Criteria consumedItemTag(TagKey<Item> tag) {
        return single(ConsumeItemCriterion.Conditions.predicate(ItemPredicate.Builder.create().tag(tag)));
    }

    private static Criteria usedKeyOnFrozenVault(String keyItem, boolean ominous) {
        return single(ItemCriterion.Conditions.createItemUsedOnBlock(
                LocationPredicate.Builder.create().block(BlockPredicate.Builder.create()
                        .blocks(FrozenDepthsBlocks.FROZEN_VAULT.block())
                        .state(StatePredicate.Builder.create().exactMatch(VaultBlock.OMINOUS, ominous))),
                ItemPredicate.Builder.create().items(item(keyItem))));
    }

    private static Criteria tick() {
        return single(TickCriterion.Conditions.createTick());
    }

    static {
        addEquipment(new Entry(
            id("equipment/root"),
            "Loot Epic Equipment!",
            "Start your journey and explore the world!",
            null,
            "loot_n_explore:ender_dragon_axe",
            AdvancementFrame.TASK,
            false, false, false,
            "minecraft:textures/block/vault_bottom.png",
            tick(),
            null
        ));

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
            hasItemTag(LneItemTags.WEAPON_THEME_ENDER_DRAGON),
            null
        ));

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
            hasItemTag(LneItemTags.WEAPON_THEME_ELDER_GUARDIAN),
            null
        ));

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
            hasItemTag(LneItemTags.WEAPON_THEME_WITHER),
            null
        ));

        addEquipment(new Entry(
            id("equipment/find_eternal_snowflake"),
            "Eternal Snowflake",
            "This snowflake cannot melt, they're compressed by the cold breath of the Glaze",
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
            "Reach the top of the Frost Tower to get this shard of eternal ice.",
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
            "Master the obstacles in the glacial tomb to obtain the frozen ribs of the Frost Haunted from their frozen vaults.",
            id("equipment/find_glacier_shard"),
            "frozen_rib",
            AdvancementFrame.TASK,
            true, true, false, null,
            hasItem("frozen_rib"),
            null
        ));

        addEquipment(new Entry(
            id("equipment/craft_frostmonarch_upgrade_smithing_template"),
            "Frost Monarch Upgrade",
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
            "Take the Frozen Soul from the Frost Monarch to craft a powerful weapon!",
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
            hasItemTag(LneItemTags.WEAPON_THEME_GLACIAL),
            null
        ));

        addExploration(new Entry(
            id("exploration/root"),
            "Explore new Structures!",
            "Start your journey and explore the world!",
            null,
            "empty_beer_mug",
            AdvancementFrame.TASK,
            false, false, false,
            "minecraft:textures/block/stripped_spruce_log.png",
            tick(),
            null
        ));

        addExploration(new Entry(
            id("exploration/find_inn"),
            "Take a rest",
            "Find a Inn, take a drink and rest!",
            id("exploration/root"),
            "malt_extract",
            AdvancementFrame.TASK,
            true, true, false, null,
            atStructures("overworld_inns/desert", "overworld_inns/plains", "overworld_inns/badlands"),
            20
        ));

        addExploration(new Entry(
            id("exploration/innkeeper"),
            "The Innkeeper",
            "Meet your first Innkeeper and fill up your supplies!",
            id("exploration/find_inn"),
            "innkeeper_shelf",
            AdvancementFrame.TASK,
            true, true, false, null,
            interactedWithVillager("loot_n_explore:innkeeper"),
            null
        ));

        addExploration(new Entry(
            id("exploration/nutritious_provisions"),
            "Nutritious Provisions",
            "Sample the various drinks and dishes that the innkeeper sells.",
            id("exploration/innkeeper"),
            "potato_soup",
            AdvancementFrame.TASK,
            true, true, false, null,
            consumedItemTag(LneItemTags.INNKEEPER_FOOD_AND_DRINKS),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/root"),
            "Frozen Depths",
            "Will you survive the structures & entities cursed by the eternal winter?",
            null,
            "monarchs_crown",
            AdvancementFrame.TASK,
            false, false, false,
            "loot_n_explore:textures/block/frozen_depths/cryotic_block.png",
            tick(),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/haunted_by_the_eternal_winter"),
            "Haunted by the eternal Winter",
            "Kill the Frost Haunted servants, that got cursed by the Monarchs greed.",
            id("frozen_depths/root"),
            "cryo_essence",
            AdvancementFrame.TASK,
            true, true, false, null,
            killedAny("frost_haunted", "monarchs_soldier", "monarchs_guard", "frozen_ranger", "frozen_magician", "frost_hound"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/the_freezing_glaze"),
            "The Freezing Glaze",
            "Kill the Glaze, beware it will hail frost balls!",
            id("frozen_depths/root"),
            "frostball",
            AdvancementFrame.TASK,
            true, true, false, null,
            killedAny("glaze"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/too_cold_to_handle"),
            "Too cold to handle",
            "Unlock any Frozen Vault with a Frozen Key",
            id("frozen_depths/haunted_by_the_eternal_winter"),
            "frozen_key",
            AdvancementFrame.TASK,
            true, true, false, null,
            usedKeyOnFrozenVault("frozen_key", false),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/monarchs_treasure"),
            "Monarch's Treasure",
            "Unlock a Ominous Frozen Vault with the Monarchs Key",
            id("frozen_depths/too_cold_to_handle"),
            "monarchs_key",
            AdvancementFrame.GOAL,
            true, true, false, null,
            usedKeyOnFrozenVault("monarchs_key", true),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/cursed_generals"),
            "Cursed Generals",
            "Kill one of the generals of the Frost Monarch",
            id("frozen_depths/haunted_by_the_eternal_winter"),
            "frozen_soul",
            AdvancementFrame.GOAL,
            true, true, false, null,
            killedAny("general_frozen_magician", "general_frozen_ranger", "general_monarchs_soldier", "general_monarchs_guard"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/the_king_cursed_by_winter"),
            "The king cursed by winter",
            "Be victorious against the Frost Monarch!",
            id("frozen_depths/cursed_generals"),
            "monarchs_crown",
            AdvancementFrame.CHALLENGE,
            true, true, false, null,
            killedAny("frost_monarch"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/abandoned_outpost"),
            "Abandoned outpost",
            "Visit a frozen outpost.",
            id("frozen_depths/root"),
            "blue_ice_bricks",
            AdvancementFrame.TASK,
            true, true, false, null,
            atStructures("frozen_depths/outpost"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/high_peak"),
            "High peak",
            "Find the frost tower",
            id("frozen_depths/abandoned_outpost"),
            "snowy_blue_ice_bricks",
            AdvancementFrame.TASK,
            true, true, false, null,
            atStructures("frozen_depths/frost_tower"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/the_castle_covered_in_ice"),
            "The castle covered in ice",
            "Visit the Monarchs Castle",
            id("frozen_depths/high_peak"),
            "frozen_kingdom_flag",
            AdvancementFrame.TASK,
            true, true, false, null,
            atStructures("frozen_depths/monarchs_castle"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/icy_mass_grave"),
            "Icy Mass Grave",
            "Explore, survive and master the glacial tomb!",
            id("frozen_depths/the_castle_covered_in_ice"),
            "frozen_trial_spawner",
            AdvancementFrame.TASK,
            true, true, false, null,
            atStructures("frozen_depths/glacial_tomb"),
            null
        ));

        addFrozenDepths(new Entry(
            id("frozen_depths/sealed_temple"),
            "Sealed Temple",
            "Find the Monarchs Temple",
            id("frozen_depths/the_castle_covered_in_ice"),
            "frozen_soul_block",
            AdvancementFrame.TASK,
            true, true, false, null,
            atStructures("frozen_depths/monarchs_temple"),
            null
        ));
    }


    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        for (Entry entry : getAllEntries()) {
            generateAdvancementEntry(entry, wrapperLookup, consumer);
        }
    }

    private void generateAdvancementEntry(Entry entry, RegistryWrapper.WrapperLookup lookup, Consumer<AdvancementEntry> consumer) {
        String fullItemName = entry.iconItemName().contains(":") ? entry.iconItemName() : "loot_n_explore:" + entry.iconItemName();
        var iconIdentifier = Identifier.tryParse(fullItemName);
        var iconItem = iconIdentifier != null ? Registries.ITEM.get(iconIdentifier) : Items.AIR;
        if (iconItem == Items.AIR) {
            throw new IllegalStateException("Item not found for advancement " + entry.id() + ": " + fullItemName);
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
            );

        Map<String, AdvancementCriterion<?>> criteria = entry.criteria().resolve(lookup);
        criteria.forEach(builder::criterion);
        builder.requirements(AdvancementRequirements.anyOf(criteria.keySet()));

        if (entry.parent() != null) {
            @SuppressWarnings("deprecation")
            var builderWithParent = builder.parent(entry.parent());
            builder = builderWithParent;
        }

        if (entry.experienceReward() != null) {
            builder.rewards(AdvancementRewards.Builder.experience(entry.experienceReward()));
        }

        consumer.accept(builder.build(consumer, entry.id().toString()));
    }

    public static List<Entry> getAllEntries() {
        List<Entry> all = new ArrayList<>();
        all.addAll(equipmentEntries);
        all.addAll(explorationEntries);
        all.addAll(frozenDepthsEntries);
        return all;
    }
}
