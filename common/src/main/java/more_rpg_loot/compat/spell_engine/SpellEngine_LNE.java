package more_rpg_loot.compat.spell_engine;

import more_rpg_loot.item.Group;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.datagen.WeaponSkills;
import net.spell_engine.rpg_series.loot.LootConfig;
import net.spell_engine.rpg_series.loot.LootHelper;
import net.tiny_config.ConfigManager;

import java.util.HashMap;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class SpellEngine_LNE {

    public static ConfigManager<LootConfig> lootEquipmentConfig = new ConfigManager<>
            ("loot_equipment_v1", Default.itemLootConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();
    public static ConfigManager<LootConfig> lootScrollsConfig = new ConfigManager<>
            ("loot_scrolls", Default.scrollLootConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();
    public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
            ("equipment_v1", new ConfigFile.Equipment())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();
    public static ConfigManager<LNE_RelicsConfig> relicsConfig = new ConfigManager<>
            ("relics", new LNE_RelicsConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static void initialize() {
        lootEquipmentConfig.refresh();
        itemConfig.refresh();
        relicsConfig.refresh();
        lootScrollsConfig.refresh();
        LootInjection.modifyChestLootTables();
        Group.registerLootItemGroup(() -> LNE_WeaponItems.ENDER_DRAGON_SWORD.item());
        SmithingTemplates.registerSmithingUpgrades();
        LNE_Relics.register(relicsConfig.value.entries);
        configureWeaponSpells();
        itemConfig.save();
        relicsConfig.save();
        LootHelper.TAG_CACHE.refresh();
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            LootHelper.configureV2(registries, key.getValue(), tableBuilder, lootEquipmentConfig.value, new HashMap<>());
            LootHelper.configureV2(registries, key.getValue(), tableBuilder, lootScrollsConfig.value, new HashMap<>());
        });
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });
    }

    private static void configureWeaponSpells() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            setContainer(context, LNE_WeaponItems.ENDER_DRAGON_SWORD,
                    SpellContainers.forMeleeWeapon()
                            .withSpellId(WeaponSkills.SWIFT_STRIKES.id())
                            .withAdditionalSpell(List.of(LNE_Abilities.dragonclaw.id().toString())));
            setContainer(context, LNE_WeaponItems.ENDER_DRAGON_AXE,
                    SpellContainers.forMeleeWeapon()
                            .withSpellId(WeaponSkills.CLEAVE.id())
                            .withAdditionalSpell(List.of(LNE_Abilities.dragonclaw.id().toString())));

            setContainer(context, LNE_WeaponItems.WITHER_SWORD,
                    SpellContainers.forMeleeWeapon()
                            .withSpellId(WeaponSkills.SWIFT_STRIKES.id())
                            .withAdditionalSpell(List.of(LNE_Abilities.wither_pulse.id().toString())));
            setContainer(context, LNE_WeaponItems.WITHER_AXE,
                    SpellContainers.forMeleeWeapon()
                            .withSpellId(WeaponSkills.CLEAVE.id())
                            .withAdditionalSpell(List.of(LNE_Abilities.wither_pulse.id().toString())));

            setContainer(context, LNE_WeaponItems.GLACIAL_SWORD,
                    SpellContainers.forMeleeWeapon()
                            .withSpellId(WeaponSkills.SWIFT_STRIKES.id())
                            .withAdditionalSpell(List.of(LNE_Abilities.avalanche.id().toString())));
            setContainer(context, LNE_WeaponItems.GLACIAL_AXE,
                    SpellContainers.forMeleeWeapon()
                            .withSpellId(WeaponSkills.CLEAVE.id())
                            .withAdditionalSpell(List.of(LNE_Abilities.avalanche.id().toString())));

            if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
                setContainer(context, LNE_WeaponItems.ELDER_GUARDIAN_SWORD,
                        SpellContainers.forMeleeWeapon()
                                .withSpellId(WeaponSkills.SWIFT_STRIKES.id())
                                .withAdditionalSpell(List.of(LNE_Abilities.waterbomb.id().toString())));
                setContainer(context, LNE_WeaponItems.ELDER_GUARDIAN_AXE,
                        SpellContainers.forMeleeWeapon()
                                .withSpellId(WeaponSkills.CLEAVE.id())
                                .withAdditionalSpell(List.of(LNE_Abilities.waterbomb.id().toString())));
            }

            if (!FabricLoader.getInstance().isModLoaded("lne_paladins") && LNE_WeaponItems.ENDER_DRAGON_MACE != null) {
                setContainer(context, LNE_WeaponItems.ENDER_DRAGON_MACE,
                        SpellContainers.forMeleeWeapon()
                                .withSpellId(WeaponSkills.SMASH.id())
                                .withAdditionalSpell(List.of(LNE_Abilities.dragonclaw.id().toString())));
                setContainer(context, LNE_WeaponItems.WITHER_MACE,
                        SpellContainers.forMeleeWeapon()
                                .withSpellId(WeaponSkills.SMASH.id())
                                .withAdditionalSpell(List.of(LNE_Abilities.wither_pulse.id().toString())));
                setContainer(context, LNE_WeaponItems.GLACIAL_MACE,
                        SpellContainers.forMeleeWeapon()
                                .withSpellId(WeaponSkills.SMASH.id())
                                .withAdditionalSpell(List.of(LNE_Abilities.avalanche.id().toString())));
                if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
                    setContainer(context, LNE_WeaponItems.ELDER_GUARDIAN_MACE,
                            SpellContainers.forMeleeWeapon()
                                    .withSpellId(WeaponSkills.SMASH.id())
                                    .withAdditionalSpell(List.of(LNE_Abilities.waterbomb.id().toString())));
                }
            }
        });
    }

    private static void setContainer(DefaultItemComponentEvents.ModifyContext context,
                                      LNE_WeaponItems.Entry entry, SpellContainer container) {
        if (entry != null) {
            context.modify(entry.item(), builder -> builder.add(SpellDataComponents.SPELL_CONTAINER, container));
        }
    }
}
