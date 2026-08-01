package more_rpg_loot.item.weapons;

import more_rpg_loot.item.Group;
import more_rpg_loot.item.weapons.frozen_depths.GlacialShieldItem;
import more_rpg_loot.item.weapons.generic.ElderGuardianShieldItem;
import more_rpg_loot.item.weapons.generic.EnderDragonShieldItem;
import more_rpg_loot.item.weapons.generic.WitherShieldItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainers;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

// Shields are only registered here if "lne_paladins" is not installed, mirroring how it registers its own themed shields.
public class LNE_ShieldItems {

    public record Entry(String name, Item item) {
        public Identifier id() { return Identifier.of(MOD_ID, name); }
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(String name, Item item) {
        var e = new Entry(name, item);
        entries.add(e);
        return e;
    }

    public static Item.Settings shieldSettings(Identifier relicSpellId) {
        var attributes = AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "shield_hp"), 6.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.ANY)
                .build();
        var settings = new Item.Settings().rarity(Rarity.RARE).maxDamage(504).attributeModifiers(attributes);
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            settings = settings.component(SpellDataComponents.SPELL_CONTAINER, SpellContainers.forShield(List.of(relicSpellId)));
        }
        return settings;
    }

    public static Entry ENDER_DRAGON_SHIELD;
    public static Entry WITHER_SHIELD;
    public static Entry GLACIAL_SHIELD;
    public static Entry ELDER_GUARDIAN_SHIELD;

    public static void register() {
        ENDER_DRAGON_SHIELD   = add("ender_dragon_shield",   new EnderDragonShieldItem());
        WITHER_SHIELD         = add("wither_shield",         new WitherShieldItem());
        GLACIAL_SHIELD        = add("glacial_shield",        new GlacialShieldItem());
        ELDER_GUARDIAN_SHIELD = add("elder_guardian_shield", new ElderGuardianShieldItem());
        for (var entry : entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register(content -> {
            for (var entry : entries) content.add(entry.item());
        });
    }
}
