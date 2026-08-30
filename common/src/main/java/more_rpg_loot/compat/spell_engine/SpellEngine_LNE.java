package more_rpg_loot.compat.spell_engine;

import more_rpg_loot.platform.LNEEvents;
import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.minecraft.util.Identifier;
import net.spell_engine.rpg_series.config.ConfigFile;
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
        Group.registerLootItemGroup(() -> LNE_WeaponItems.ENDER_DRAGON_SWORD.item());
        configureWeaponSpells();
        itemConfig.save();
        relicsConfig.save();
        LootHelper.TAG_CACHE.refresh();
        LNEEvents.get().onLootTableModify(ctx -> {
            LootHelper.configure(ctx.registries(), ctx.tableId(), ctx::addPool, lootEquipmentConfig.value, new HashMap<>());
            LootHelper.configure(ctx.registries(), ctx.tableId(), ctx::addPool, lootScrollsConfig.value, new HashMap<>());
        });
        LNEEvents.get().onServerStarted(server -> LootHelper.updateTagCache(lootEquipmentConfig.value));
        LNEEvents.get().onDataPackReload(() -> LootHelper.updateTagCache(lootEquipmentConfig.value));
    }

    private static void configureWeaponSpells() {
        LNEEvents.get().modifyItemComponents(context -> {
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

            if (LNEPlatform.isModLoaded("more_rpg_classes")) {
                setContainer(context, LNE_WeaponItems.ELDER_GUARDIAN_SWORD,
                        SpellContainers.forMeleeWeapon()
                                .withSpellId(WeaponSkills.SWIFT_STRIKES.id())
                                .withAdditionalSpell(List.of(LNE_Abilities.waterbomb.id().toString())));
                setContainer(context, LNE_WeaponItems.ELDER_GUARDIAN_AXE,
                        SpellContainers.forMeleeWeapon()
                                .withSpellId(WeaponSkills.CLEAVE.id())
                                .withAdditionalSpell(List.of(LNE_Abilities.waterbomb.id().toString())));
            }

            if (!LNEPlatform.isModLoaded("lne_paladins") && LNE_WeaponItems.ENDER_DRAGON_MACE != null) {
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
                if (LNEPlatform.isModLoaded("more_rpg_classes")) {
                    setContainer(context, LNE_WeaponItems.ELDER_GUARDIAN_MACE,
                            SpellContainers.forMeleeWeapon()
                                    .withSpellId(WeaponSkills.SMASH.id())
                                    .withAdditionalSpell(List.of(LNE_Abilities.waterbomb.id().toString())));
                }
            }

            context.modify(CommonItems.MONARCHS_FROST_STAFF.item(), builder -> builder.add(SpellDataComponents.SPELL_CONTAINER,
                    SpellContainers.forMagicWeapon().withSpellId(Identifier.of("wizards", "frostbolt"))));
            context.modify(CommonItems.GUARDS_FROST_LANCE.item(), builder -> builder.add(SpellDataComponents.SPELL_CONTAINER,
                    SpellContainers.forMeleeWeapon().withSpellId(WeaponSkills.IMPALE.id())));
        });
    }

    private static void setContainer(LNEEvents.ItemComponentContext context,
                                      LNE_WeaponItems.Entry entry, SpellContainer container) {
        if (entry != null) {
            context.modify(entry.item(), builder -> builder.add(SpellDataComponents.SPELL_CONTAINER, container));
        }
    }
}
