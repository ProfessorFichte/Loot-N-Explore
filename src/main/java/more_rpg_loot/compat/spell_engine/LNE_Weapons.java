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
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_engine.api.item.weapon.SpellSwordItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class LNE_Weapons {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();
    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType weaponType) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, weaponType);
        entry.castSpell();
        entry.loot(Equipment.LootProperties.of(5));
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
        var entry = entry(name, material, SpellSwordItem::new, new WeaponConfig(damage, -2.4F), Equipment.WeaponType.SWORD);
        entry.weaponAttributesPreset = "sword";
        return entry;
    }
    private static Weapon.Entry axe(String name, Weapon.CustomMaterial material, float damage) {
        var entry = entry(name, material, SpellSwordItem::new, new WeaponConfig(damage, -3F), Equipment.WeaponType.SWORD);
        entry.weaponAttributesPreset = "axe";
        return entry;
    }
    static float sword_damage = 9.0F;
    static float axe_damage = 10.5F;
    public static Identifier dragonclaw = Identifier.of(MOD_ID, "dragonclaw");
    public static Identifier avalanche = Identifier.of(MOD_ID, "avalanche");
    public static Identifier waterbomb = Identifier.of(MOD_ID, "waterbomb");
    public static Identifier wither_pulse = Identifier.of(MOD_ID, "wither_pulse");

    //SWORDS
    public static final Weapon.Entry ender_dragon_sword = sword("ender_dragon_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), sword_damage)
            .spell(dragonclaw);
    public static final Weapon.Entry wither_sword = sword("wither_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), sword_damage)
            .spell(wither_pulse);
    public static final Weapon.Entry glacial_sword = sword("glacial_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), sword_damage)
            .spell(avalanche);
    //AXE
    public static final Weapon.Entry ender_dragon_axe = axe("ender_dragon_axe",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), axe_damage)
            .spell(dragonclaw);
    public static final Weapon.Entry wither_axe = axe("wither_axe",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), axe_damage)
            .spell(wither_pulse);
    public static final Weapon.Entry glacial_axe = axe("glacial_axe",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)), axe_damage)
            .spell(avalanche);

    static {
        entries.forEach(entry -> entry.rarity = Rarity.RARE);
    }

    public static void register(Map<String, WeaponConfig> configs) {
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            var repair_elder_guardian = ingredient("loot_n_explore:elder_guardian_eye", FabricLoader.getInstance().isModLoaded("more_rpg_classes"), Items.NETHERITE_INGOT);
            sword("elder_guardian_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair_elder_guardian), sword_damage)
                    .spell(waterbomb);
            axe("elder_guardian_axe",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair_elder_guardian), axe_damage)
                    .spell(waterbomb);
        }
        Weapon.register(configs, entries, Group.RPG_LOOT_KEY);
    }
}
