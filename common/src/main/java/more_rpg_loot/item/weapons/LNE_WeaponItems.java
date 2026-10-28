package more_rpg_loot.item.weapons;

import more_rpg_loot.ability.TriggerType;
import more_rpg_loot.platform.LNEEvents;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.entity.frozen_depths.projectile.FrostballEntity;
import more_rpg_loot.entity.generic.projectile.LNEAbilityArrowEntity;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.weapons.frozen_depths.GlacialAxeItem;
import more_rpg_loot.item.weapons.frozen_depths.GlacialMaceItem;
import more_rpg_loot.item.weapons.frozen_depths.GlacialSwordItem;
import more_rpg_loot.item.weapons.generic.ElderGuardianAxeItem;
import more_rpg_loot.item.weapons.generic.ElderGuardianMaceItem;
import more_rpg_loot.item.weapons.generic.ElderGuardianSwordItem;
import more_rpg_loot.item.weapons.generic.EnderDragonAxeItem;
import more_rpg_loot.item.weapons.generic.EnderDragonMaceItem;
import more_rpg_loot.item.weapons.generic.EnderDragonSwordItem;
import more_rpg_loot.item.weapons.generic.WitherAxeItem;
import more_rpg_loot.item.weapons.generic.WitherMaceItem;
import more_rpg_loot.item.weapons.generic.WitherSwordItem;
import com.mrpg_lib.util.CloudMethods;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.*;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

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

    public static void spawnDragonParticles(World world, LivingEntity target) {
        if (world instanceof ServerWorld sw) {
            double cx = target.getX(), cy = target.getY() + target.getHeight() / 2.0, cz = target.getZ();
            WeaponFx.dragon(sw, new Vec3d(cx, cy, cz));
        }
    }

    public static void spawnWitherSkulls(World world, LivingEntity attacker, LivingEntity target) {
        double dx = target.getX() - attacker.getX();
        double dy = target.getEyeY() - attacker.getEyeY();
        double dz = target.getZ() - attacker.getZ();
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len > 0) { dx /= len; dy /= len; dz /= len; }
        double[] spreadX = {0, 0.15, -0.15};
        double[] spreadZ = {0, -0.1, 0.1};
        for (int i = 0; i < 3; i++) {
            WitherSkullEntity skull = new WitherSkullEntity(world, attacker,
                    new Vec3d(dx + spreadX[i], dy, dz + spreadZ[i]));
            skull.setPosition(attacker.getX(), attacker.getEyeY(), attacker.getZ());
            world.spawnEntity(skull);
        }
        if (world instanceof ServerWorld sw) {
            WeaponFx.wither(sw, new Vec3d(attacker.getX(), attacker.getEyeY(), attacker.getZ()), target.getPos().add(0, target.getHeight() / 2.0, 0));
        }
    }

    public static void spawnFrostballs(World world, LivingEntity attacker) {
        double bx = attacker.getX(), by = attacker.getY() + 6.0, bz = attacker.getZ();
        double r = 1.5;
        for (int i = 0; i < 5; i++) {
            double angle = (2 * Math.PI / 5) * i;
            FrostballEntity ball = new FrostballEntity(world,
                    bx + r * Math.cos(angle), by, bz + r * Math.sin(angle));
            ball.setVelocity(0, -1, 0);
            world.spawnEntity(ball);
        }
        if (world instanceof ServerWorld sw) {
            WeaponFx.glacial(sw, new Vec3d(bx, by, bz), attacker.getPos().add(0, 0.2, 0));
        }
    }

    public static void spawnRainCloud(LivingEntity attacker, LivingEntity target) {
        float dmg = (float)(attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * WeaponAbility.ELDER_GUARDIAN.damageMultiplier());
        CloudMethods.spawnCustomCloud(ParticleTypes.RAIN, attacker, target, 1,
                5.0f, 5, 5.0f, null, 0, 0, false, 0,
                true, dmg, target.getDamageSources().magic());
        if (target.getWorld() instanceof ServerWorld sw) {
            WeaponFx.guardian(sw, target.getPos().add(0, target.getHeight() / 2.0, 0));
        }
    }

    public static boolean vanillaAbilitiesActive() {
        return !LNEPlatform.isModLoaded("spell_engine");
    }

    public static void addAbilityTooltip(WeaponAbility ability, TriggerType trigger, List<Text> tooltip) {
        if (vanillaAbilitiesActive()) {
            ability.appendTooltip(trigger, tooltip);
        }
    }

    private static ProjectileEntity toAbilityArrow(World world, LivingEntity shooter, byte abilityId, ProjectileEntity vanilla) {
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
        GLACIAL_SWORD        = add("glacial_sword",        new GlacialSwordItem(4, -2.4f),        "Glacial Sword");
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
        ENDER_DRAGON_MACE   = addMace("ender_dragon_mace",   new EnderDragonMaceItem(3, -3.2f),   "Dragon's Mace");
        WITHER_MACE         = addMace("wither_mace",         new WitherMaceItem(3, -3.2f),         "Withered Mace");
        GLACIAL_MACE        = addMace("glacial_mace",        new GlacialMaceItem(3, -3.2f),        "Glacial Mace");
        ELDER_GUARDIAN_MACE = addMace("elder_guardian_mace", new ElderGuardianMaceItem(3, -3.2f), "Tide Mace");
        for (var entry : maceEntries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        LNEEvents.get().modifyItemGroup(Group.RPG_LOOT_KEY, content -> {
            for (var entry : maceEntries) content.add(entry.item());
        });
    }

    public static void registerRanged() {
        ENDER_DRAGON_BOW        = addRanged("ender_dragon_bow",        new AbilityBowItem(LNEAbilityArrowEntity.ENDER_DRAGON),  "Dragon's Bow");
        ENDER_DRAGON_CROSSBOW   = addRanged("ender_dragon_crossbow",   new AbilityCrossbowItem(LNEAbilityArrowEntity.ENDER_DRAGON), "Dragon's Crossbow");
        WITHER_BOW              = addRanged("wither_bow",              new AbilityBowItem(LNEAbilityArrowEntity.WITHER),         "Wither Bow");
        WITHER_CROSSBOW         = addRanged("wither_crossbow",         new AbilityCrossbowItem(LNEAbilityArrowEntity.WITHER),    "Wither Crossbow");
        GLACIAL_BOW             = addRanged("glacial_bow",             new AbilityBowItem(LNEAbilityArrowEntity.GLACIAL),        "Glacial Bow");
        GLACIAL_CROSSBOW        = addRanged("glacial_crossbow",        new AbilityCrossbowItem(LNEAbilityArrowEntity.GLACIAL),   "Glacial Crossbow");
        ELDER_GUARDIAN_BOW      = addRanged("elder_guardian_bow",      new AbilityBowItem(LNEAbilityArrowEntity.ELDER_GUARDIAN), "Elder Guardian's Bow");
        ELDER_GUARDIAN_CROSSBOW = addRanged("elder_guardian_crossbow", new AbilityCrossbowItem(LNEAbilityArrowEntity.ELDER_GUARDIAN), "Elder Guardian's Crossbow");
        for (var entry : rangedEntries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item());
        }
        LNEEvents.get().modifyItemGroup(Group.RPG_LOOT_KEY, content -> {
            for (var entry : rangedEntries) content.add(entry.item());
        });
    }
}
