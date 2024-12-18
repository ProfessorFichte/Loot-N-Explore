package more_rpg_loot.item.weapons;

public class WeaponsRegisterOld {
   /* public static final ArrayList<RangedEntry> rangedEntries = new ArrayList<>();
    public static final ArrayList<Weapon.Entry> meleeEntries = new ArrayList<>();
    public record RangedEntry(Identifier id, Item item, RangedConfig defaults) { }
    ///ATTACKSPEED_VALUES
    private static final float swordVanillaAttackSpeed = -2.4F;
    private static final float axeVanillaAttackSpeed = -3.0F;
    private static final float rogues_sickleAttackSpeed = -2.0F;
    private static final float rogues_daggerAttackSpeed = -1.6F;
    private static final float rogues_glaiveAttackSpeed = -2.6F;
    private static final float rogues_doubleAxeAttackSpeed = -2.8F;
    private static final float paladins_claymoreAttackSpeed = -3F;
    private static final float paladins_greatHammerAttackSpeed = -3.2F;
    private static final float paladins_maceAttackSpeed = -2.8F;
    public static final float forcemaster_knuckleAttackSpeed = -2.2f;
    public static final float berserker_raidAxeAttackSpeed = -3.1f;



    private static final float weaponSpellPower = 3.0F;
    //MELEE
    private static Weapon.Entry entryMelee(String requiredMod, String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        var entry = new Weapon.Entry(RPGLoot.MOD_ID, name, material, item, defaults, null);
        if (entry.isRequiredModInstalled()) {
            meleeEntries.add(entry);
        }
        return entry;
    }

    ////MODDED WEAPONS
    ///ARCHERS


    ///ROGUES
    //DAGGER
    private static final float daggerAttackDamage = 7.5F;
    private static Weapon.Entry daggerElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("rogues", "elder_guardian_dagger", material, item, new ItemConfig.Weapon(daggerAttackDamage, rogues_daggerAttackSpeed));
    }
    public static final Weapon.Entry elderGuardianDagger=  daggerElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry daggerDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material,settings);
        return entryMelee("rogues", "ender_dragon_dagger", material, item, new ItemConfig.Weapon(daggerAttackDamage, rogues_daggerAttackSpeed));
    }
    public static final Weapon.Entry enderDragonDagger=  daggerDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry daggerWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material,settings);
        return entryMelee("rogues", "wither_dagger", material, item, new ItemConfig.Weapon(daggerAttackDamage, rogues_daggerAttackSpeed));
    }
    public static final Weapon.Entry witherDagger=  daggerWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.WITHER_SKELETON_SKULL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    //SICKLE
    private static final float sickleAttackDamage = 7.5F;
    private static Weapon.Entry sickleElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("rogues", "elder_guardian_sickle", material, item, new ItemConfig.Weapon(sickleAttackDamage, rogues_sickleAttackSpeed));
    }
    public static final Weapon.Entry elderGuardiansickle=  sickleElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry sickleDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material,settings);
        return entryMelee("rogues", "ender_dragon_sickle", material, item, new ItemConfig.Weapon(sickleAttackDamage, rogues_sickleAttackSpeed));
    }
    public static final Weapon.Entry enderDragonsickle=  sickleDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry sickleWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material,settings);
        return entryMelee("rogues", "wither_sickle", material, item, new ItemConfig.Weapon(sickleAttackDamage, rogues_sickleAttackSpeed));
    }
    public static final Weapon.Entry withersickle=  sickleWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.WITHER_SKELETON_SKULL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));


    //GLAIVE
    private static final float glaiveAttackDamage = 7.5F;
    private static Weapon.Entry glaiveElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("rogues", "elder_guardian_glaive", material, item, new ItemConfig.Weapon(glaiveAttackDamage, rogues_glaiveAttackSpeed));
    }
    public static final Weapon.Entry elderGuardianGlaive=  glaiveElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry glaiveDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material,settings);
        return entryMelee("rogues", "ender_dragon_glaive", material, item, new ItemConfig.Weapon(glaiveAttackDamage, rogues_glaiveAttackSpeed));
    }
    public static final Weapon.Entry enderDragonGlaive=  glaiveDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry glaiveWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material,settings);
        return entryMelee("rogues", "wither_glaive", material, item, new ItemConfig.Weapon(glaiveAttackDamage, rogues_glaiveAttackSpeed));
    }
    public static final Weapon.Entry witherglaive=  glaiveWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.WITHER_SKELETON_SKULL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    //DOUBLEAXE
    private static final float doubleAxeAttackDamage = 7.5F;
    private static Weapon.Entry doubleAxeElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("rogues", "elder_guardian_double_axe", material, item, new ItemConfig.Weapon(doubleAxeAttackDamage, rogues_doubleAxeAttackSpeed));
    }
    public static final Weapon.Entry elderGuardiandoubleAxe=  doubleAxeElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry doubleAxeDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material,settings);
        return entryMelee("rogues", "ender_dragon_double_axe", material, item, new ItemConfig.Weapon(doubleAxeAttackDamage, rogues_doubleAxeAttackSpeed));
    }
    public static final Weapon.Entry enderDragondoubleAxe=  doubleAxeDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry doubleAxeWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material,settings);
        return entryMelee("rogues", "wither_double_axe", material, item, new ItemConfig.Weapon(doubleAxeAttackDamage, rogues_doubleAxeAttackSpeed));
    }
    public static final Weapon.Entry witherdoubleAxe=  doubleAxeWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.WITHER_SKELETON_SKULL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));


    ///PALADINS
    //CLAYMORE
    private static final float claymoreAttackDamage = 7.5F;
    private static Weapon.Entry claymoreElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("paladins", "elder_guardian_claymore", material, item, new ItemConfig.Weapon(claymoreAttackDamage, paladins_claymoreAttackSpeed));
    }
    public static final Weapon.Entry elderGuardianClaymore=  claymoreElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry claymoreDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material,settings);
        return entryMelee("paladins", "ender_dragon_claymore", material, item, new ItemConfig.Weapon(claymoreAttackDamage, paladins_claymoreAttackSpeed));
    }
    public static final Weapon.Entry enderDragonClaymore=  claymoreDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry claymoreWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material,settings);
        return entryMelee("paladins", "wither_claymore", material, item, new ItemConfig.Weapon(claymoreAttackDamage, paladins_claymoreAttackSpeed));
    }
    public static final Weapon.Entry witherClaymore=  claymoreWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.WITHER_SKELETON_SKULL)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));


    //Registration
    public static void register(Map<String, RangedConfig> rangedConfig, Map<String, ItemConfig.Weapon> meleeConfig) {





        Weapon.register(meleeConfig, meleeEntries, Group.RPG_LOOT_KEY);
        for (var entry: rangedEntries) {
            var config = rangedConfig.get(entry.id.toString());
            if (config == null) {
                config = entry.defaults;
                rangedConfig.put(entry.id.toString(), config);
            }
            ((CustomRangedWeapon)entry.item).configure(config);
            Registry.register(Registries.ITEM, entry.id, entry.item);
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register((content) -> {
            for (var entry: rangedEntries) {
                content.add(entry.item);
            }
        });
    }*/
}
