package more_rpg_loot.compat.spell_engine;

import more_rpg_loot.item.Group;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.datagen.WeaponSkills;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Weapon;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class LNE_Weapons {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();
    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType weaponType) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, weaponType);
        entries.add(entry);
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    private static Weapon.Entry sword(String name, Weapon.CustomMaterial material, float damage) {
        var entry = entry(name, material, SpellSwordItem::new, new WeaponConfig(damage, -2.4F), Equipment.WeaponType.SWORD)
                .spellContainer(SpellContainers.forMeleeWeapon().withSpellId(WeaponSkills.SWIFT_STRIKES.id()));
        entry.weaponAttributesPreset = "sword";
        return entry;
    }
    private static Weapon.Entry axe(String name, Weapon.CustomMaterial material, float damage) {
        var entry = entry(name, material, SpellSwordItem::new, new WeaponConfig(damage, -3F), Equipment.WeaponType.SWORD)
                .spellContainer(SpellContainers.forMeleeWeapon().withSpellId(WeaponSkills.CLEAVE.id()));
        entry.weaponAttributesPreset = "axe";
        return entry;
    }
    static float sword_damage = 8.0F;
    static float axe_damage = 10.0F;
    static float weapon_spell_power = 2.0F;

    public static final Weapon.Entry ender_dragon_sword = sword("ender_dragon_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), sword_damage)
            .withAdditionalSpell(LNE_Abilities.dragonclaw.id().toString())
            .translatedName("Dragon Slayer")
            .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, weapon_spell_power));
    public static final Weapon.Entry ender_dragon_axe = axe("ender_dragon_axe",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), axe_damage)
            .withAdditionalSpell(LNE_Abilities.dragonclaw.id().toString())
            .translatedName("End Conqueror")
            .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, weapon_spell_power));

    public static final Weapon.Entry wither_sword = sword("wither_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)), sword_damage)
            .withAdditionalSpell(LNE_Abilities.wither_pulse.id().toString())
            .translatedName("Withered Sword")
            .attribute(AttributeModifier.bonus(SpellSchools.SOUL.id, weapon_spell_power));
    public static final Weapon.Entry wither_axe = axe("wither_axe",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)), axe_damage)
            .withAdditionalSpell(LNE_Abilities.wither_pulse.id().toString())
            .translatedName("Withered Axe")
            .attribute(AttributeModifier.bonus(SpellSchools.SOUL.id, weapon_spell_power));

    public static final Weapon.Entry glacial_sword = sword("glacial_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.ICE)), sword_damage)
            .withAdditionalSpell(LNE_Abilities.avalanche.id().toString())
            .translatedName("Glacial Sword")
            .attribute(AttributeModifier.bonus(SpellSchools.FROST.id, weapon_spell_power));
    public static final Weapon.Entry glacial_axe = axe("glacial_axe",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.ICE)), axe_damage)
            .translatedName("Glacial Axe")
            .withAdditionalSpell(LNE_Abilities.avalanche.id().toString())
            .attribute(AttributeModifier.bonus(SpellSchools.FROST.id, weapon_spell_power));


    public static void register(Map<String, WeaponConfig> configs) {
        // Conditional Elder Guardian weapons (only loaded when more_rpg_classes mod is present)
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            var repair_elder_guardian = ingredient("minecraft:prismarine_shard", FabricLoader.getInstance().isModLoaded("more_rpg_classes"), Items.NETHERITE_INGOT);
            var elderGuardianSword = sword("elder_guardian_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair_elder_guardian), sword_damage)
                    .withAdditionalSpell(LNE_Abilities.waterbomb.id().toString())
                    .translatedName("Leviathan")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.WATER.id, weapon_spell_power));
            var elderGuardianAxe = axe("elder_guardian_axe",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair_elder_guardian), axe_damage)
                    .withAdditionalSpell(LNE_Abilities.waterbomb.id().toString())
                    .translatedName("Nautilus")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.WATER.id, weapon_spell_power));
        }
        entries.forEach(entry -> entry.rarity = Rarity.RARE);
        Weapon.register(configs, entries, Group.RPG_LOOT_KEY);
    }
}
