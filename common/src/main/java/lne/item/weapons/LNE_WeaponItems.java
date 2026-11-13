package lne.item.weapons;

import lne.config.TweaksConfig;
import lne.ability.TriggerType;
import lne.platform.LNEEvents;


import lne.entity.generic.projectile.LNEAbilityArrowEntity;
import lne.item.Group;
import lne.item.weapons.frozen_depths.GlacialAxeItem;
import lne.item.weapons.frozen_depths.GlacialMaceItem;
import lne.item.weapons.frozen_depths.GlacialSwordItem;
import lne.item.weapons.generic.ElderGuardianAxeItem;
import lne.item.weapons.generic.ElderGuardianMaceItem;
import lne.item.weapons.generic.ElderGuardianSwordItem;
import lne.item.weapons.generic.EnderDragonAxeItem;
import lne.item.weapons.generic.EnderDragonMaceItem;
import lne.item.weapons.generic.EnderDragonSwordItem;
import lne.item.weapons.generic.WitherAxeItem;
import lne.item.weapons.generic.WitherMaceItem;
import lne.item.weapons.generic.WitherSwordItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.*;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static lne.LneMod.MOD_ID;

public class LNE_WeaponItems {

    public record Entry(String name, Item item, String translatedName) {
        public Identifier id() { return Identifier.of(MOD_ID, name); }
    }

    public static final List<Entry> entries = new ArrayList<>();
    public static final List<Entry> rangedEntries = new ArrayList<>();
    public static final List<Entry> maceEntries = new ArrayList<>();

    private static Entry add(String name, Item item, String translatedName) {
        var e = new Entry(name, item, translatedName);
        entries.add(e);
        return e;
    }

    private static Entry addRanged(String name, Item item, String translatedName) {
        var e = new Entry(name, item, translatedName);
        rangedEntries.add(e);
        return e;
    }

    private static Entry addMace(String name, Item item, String translatedName) {
        var e = new Entry(name, item, translatedName);
        maceEntries.add(e);
        return e;
    }

    public static Item.Settings meleeSettings(float bonusDamage, float attackSpeed) {
        return new Item.Settings()
                .rarity(Rarity.RARE)
                .attributeModifiers(SwordItem.createAttributeModifiers(
                        ToolMaterials.NETHERITE, (int) bonusDamage, attackSpeed));
    }

    // MaceItem(Settings) skips applySettings, so maxDamage has to be set by hand
    public static Item.Settings maceSettings(float bonusDamage, float attackSpeed) {
        return new Item.Settings()
                .rarity(Rarity.RARE)
                .maxDamage(2031)
                .attributeModifiers(SwordItem.createAttributeModifiers(
                        ToolMaterials.NETHERITE, (int) bonusDamage, attackSpeed));
    }

    public static boolean vanillaAbilitiesActive() {
        return TweaksConfig.weaponAbilities() && !TweaksConfig.spellEngineAbilities();
    }

    public static void addAbilityTooltip(WeaponAbility ability, TriggerType trigger, List<Text> tooltip) {
        if (vanillaAbilitiesActive()) {
            ability.appendTooltip(trigger, tooltip);
        }
    }

    public static ProjectileEntity toAbilityArrow(World world, LivingEntity shooter, byte abilityId, ProjectileEntity vanilla) {
        if (!(vanilla instanceof PersistentProjectileEntity)) return vanilla;
        NbtCompound nbt = vanilla.writeNbt(new NbtCompound());
        nbt.putByte("AbilityId", abilityId);
        LNEAbilityArrowEntity arrow = new LNEAbilityArrowEntity(world, shooter, abilityId);
        arrow.readNbt(nbt);
        return arrow;
    }

    public static class AbilityBowItem extends BowItem {
        private final byte abilityId;

        public AbilityBowItem(byte abilityId) {
            super(new Item.Settings().rarity(Rarity.RARE).maxDamage(384));
            this.abilityId = abilityId;
        }

        @Override
        protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
            ProjectileEntity vanilla = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
            return vanillaAbilitiesActive() ? toAbilityArrow(world, shooter, abilityId, vanilla) : vanilla;
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            super.appendTooltip(stack, context, tooltip, type);
            addAbilityTooltip(LNEAbilityArrowEntity.weaponAbility(abilityId), TriggerType.ARROW_HIT, tooltip);
        }
    }

    public static class AbilityCrossbowItem extends CrossbowItem {
        private final byte abilityId;

        public AbilityCrossbowItem(byte abilityId) {
            super(new Item.Settings().rarity(Rarity.RARE).maxDamage(465));
            this.abilityId = abilityId;
        }

        @Override
        protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
            ProjectileEntity vanilla = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
            return vanillaAbilitiesActive() ? toAbilityArrow(world, shooter, abilityId, vanilla) : vanilla;
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            super.appendTooltip(stack, context, tooltip, type);
            addAbilityTooltip(LNEAbilityArrowEntity.weaponAbility(abilityId), TriggerType.ARROW_HIT, tooltip);
        }
    }

    public static Entry ENDER_DRAGON_SWORD;
    public static Entry ENDER_DRAGON_AXE;
    public static Entry WITHER_SWORD;
    public static Entry WITHER_AXE;
    public static Entry GLACIAL_SWORD;
    public static Entry GLACIAL_AXE;
    public static Entry ELDER_GUARDIAN_SWORD;
    public static Entry ELDER_GUARDIAN_AXE;

    public static Entry ENDER_DRAGON_MACE;
    public static Entry WITHER_MACE;
    public static Entry GLACIAL_MACE;
    public static Entry ELDER_GUARDIAN_MACE;

    public static Entry ENDER_DRAGON_BOW;
    public static Entry ENDER_DRAGON_CROSSBOW;
    public static Entry WITHER_BOW;
    public static Entry WITHER_CROSSBOW;
    public static Entry GLACIAL_BOW;
    public static Entry GLACIAL_CROSSBOW;
    public static Entry ELDER_GUARDIAN_BOW;
    public static Entry ELDER_GUARDIAN_CROSSBOW;



    public static void register() {
        ENDER_DRAGON_SWORD   = add("ender_dragon_sword",   new EnderDragonSwordItem(4, -2.4f),   "Dragon Slayer");
        ENDER_DRAGON_AXE     = add("ender_dragon_axe",     new EnderDragonAxeItem(6, -3.0f),     "End Conqueror");
        WITHER_SWORD         = add("wither_sword",         new WitherSwordItem(4, -2.4f),         "Withered Sword");
        WITHER_AXE           = add("wither_axe",           new WitherAxeItem(6, -3.0f),           "Withered Axe");
        GLACIAL_SWORD        = add("glacial_sword",        new GlacialSwordItem(4, -2.4f),        "Frostsoul Blade");
        GLACIAL_AXE          = add("glacial_axe",          new GlacialAxeItem(6, -3.0f),          "Glacial Axe");
        ELDER_GUARDIAN_SWORD = add("elder_guardian_sword", new ElderGuardianSwordItem(4, -2.4f), "Leviathan");
        ELDER_GUARDIAN_AXE   = add("elder_guardian_axe",   new ElderGuardianAxeItem(6, -3.0f),   "Nautilus");
        for (var entry : entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        LNEEvents.get().modifyItemGroup(Group.RPG_LOOT_KEY, content -> {
            for (var entry : entries) content.add(entry.item());
        });
    }

    public static void registerMaces() {
        ENDER_DRAGON_MACE   = addMace("ender_dragon_mace",   new EnderDragonMaceItem(3, -3.2f),   "Dragon's Maw");
        WITHER_MACE         = addMace("wither_mace",         new WitherMaceItem(3, -3.2f),         "Withered Mace");
        GLACIAL_MACE        = addMace("glacial_mace",        new GlacialMaceItem(3, -3.2f),        "Icicle Crusher");
        ELDER_GUARDIAN_MACE = addMace("elder_guardian_mace", new ElderGuardianMaceItem(3, -3.2f), "Sea Star");
        for (var entry : maceEntries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        LNEEvents.get().modifyItemGroup(Group.RPG_LOOT_KEY, content -> {
            for (var entry : maceEntries) content.add(entry.item());
        });
    }

    public static void registerRanged() {
        ENDER_DRAGON_BOW        = addRanged("ender_dragon_bow",        new AbilityBowItem(LNEAbilityArrowEntity.ENDER_DRAGON),  "End-Crystal Bow");
        ENDER_DRAGON_CROSSBOW   = addRanged("ender_dragon_crossbow",   new AbilityCrossbowItem(LNEAbilityArrowEntity.ENDER_DRAGON), "Dragon's Jaw");
        WITHER_BOW              = addRanged("wither_bow",              new AbilityBowItem(LNEAbilityArrowEntity.WITHER),         "Soulstorm Bow");
        WITHER_CROSSBOW         = addRanged("wither_crossbow",         new AbilityCrossbowItem(LNEAbilityArrowEntity.WITHER),    "Witherbolt Arbalest");
        GLACIAL_BOW             = addRanged("glacial_bow",             new AbilityBowItem(LNEAbilityArrowEntity.GLACIAL),        "Glacial Shardbow");
        GLACIAL_CROSSBOW        = addRanged("glacial_crossbow",        new AbilityCrossbowItem(LNEAbilityArrowEntity.GLACIAL),   "Glacier Bolt Crossbow");
        ELDER_GUARDIAN_BOW      = addRanged("elder_guardian_bow",      new AbilityBowItem(LNEAbilityArrowEntity.ELDER_GUARDIAN), "Nemo's Fury");
        ELDER_GUARDIAN_CROSSBOW = addRanged("elder_guardian_crossbow", new AbilityCrossbowItem(LNEAbilityArrowEntity.ELDER_GUARDIAN), "Reefstriker");
        for (var entry : rangedEntries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        LNEEvents.get().modifyItemGroup(Group.RPG_LOOT_KEY, content -> {
            for (var entry : rangedEntries) content.add(entry.item());
        });
    }
}
