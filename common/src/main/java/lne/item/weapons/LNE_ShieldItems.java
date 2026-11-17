package lne.item.weapons;

import lne.internal.platform.LNEEvents;


import lne.item.Group;
import lne.item.weapons.frozen_depths.GlacialShieldItem;
import lne.item.weapons.generic.ElderGuardianShieldItem;
import lne.item.weapons.generic.EnderDragonShieldItem;
import lne.item.weapons.generic.WitherShieldItem;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;

import static lne.LneMod.MOD_ID;

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

    public static Item.Settings shieldSettings(RegistryEntry<EntityAttribute> bonus, double bonusValue,
                                               EntityAttributeModifier.Operation bonusOperation) {
        var attributes = AttributeModifiersComponent.builder()
                .add(bonus,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "shield_bonus"), bonusValue, bonusOperation),
                        AttributeModifierSlot.ANY)
                .add(EntityAttributes.GENERIC_MAX_HEALTH,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "shield_hp"), 6.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.ANY)
                .build();
        return new Item.Settings().rarity(Rarity.RARE).maxDamage(504).attributeModifiers(attributes);
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
        LNEEvents.get().modifyItemGroup(Group.RPG_LOOT_KEY, content -> {
            for (var entry : entries) content.add(entry.item());
        });
    }
}
