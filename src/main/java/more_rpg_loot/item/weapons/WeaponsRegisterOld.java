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
    private static final float archers_spearAttackSpeed = -2.6F;
    private static final int archers_pullTime_shortBow = 16;
    private static final int archers_pullTime_longBow = 30;
    private static final int archers_pullTime_rapidCrossbow = 20;
    private static final int archers_pullTime_heavyCrossbow = 35;
    public static int vanilla_bow_pull_time = 20;

    private static final float weaponSpellPower = 3.0F;
    //MELEE
    private static Weapon.Entry entryMelee(String requiredMod, String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        var entry = new Weapon.Entry(RPGLoot.MOD_ID, name, material, item, defaults, null);
        if (entry.isRequiredModInstalled()) {
            meleeEntries.add(entry);
        }
        return entry;
    }
    //RANGED
    private static RangedEntry addRanged(Identifier id, Item item, RangedConfig defaults) {
        var entry = new RangedEntry(id, item, defaults);
        rangedEntries.add(entry);
        return entry;
    }
    private static RangedEntry bow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new CustomBow(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(RPGLoot.MOD_ID, name), item, defaults);
    }

    ////VANILLA WEAPONS
    //SWORD
    private static final float swordAttackDamage = 7.5F;
    private static Weapon.Entry swordElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("", "elder_guardian_sword", material, item, new ItemConfig.Weapon(swordAttackDamage, swordVanillaAttackSpeed));
    }
    private static Weapon.Entry swordDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material, settings);
        return entryMelee("", "ender_dragon_sword", material, item, new ItemConfig.Weapon(swordAttackDamage, swordVanillaAttackSpeed));
    }
    private static Weapon.Entry swordWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material, settings);
        return entryMelee("", "wither_sword", material, item, new ItemConfig.Weapon(swordAttackDamage, swordVanillaAttackSpeed));
    }
    public static final Weapon.Entry elderGuardianSword= swordElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));
    public static final Weapon.Entry enderDragonSword= swordDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, weaponSpellPower));
    public static final Weapon.Entry WitherSword= swordWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHER_STAR)))
            .attribute(ItemConfig.Attribute.bonus(SpellSchools.SOUL.id, weaponSpellPower));

    //AXE
    private static final float axeAttackDamage = 7.5F;
    private static Weapon.Entry axeElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("", "elder_guardian_axe", material, item, new ItemConfig.Weapon(axeAttackDamage, axeVanillaAttackSpeed));
    }
    private static Weapon.Entry axeDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material, settings);
        return entryMelee("", "ender_dragon_axe", material, item, new ItemConfig.Weapon(axeAttackDamage, axeVanillaAttackSpeed));
    }
    private static Weapon.Entry axeWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material, settings);
        return entryMelee("", "wither_axe", material, item, new ItemConfig.Weapon(axeAttackDamage, axeVanillaAttackSpeed));
    }
    public static final Weapon.Entry elderGuardianAxe=  axeElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));
    public static final Weapon.Entry enderDragonAxe= axeDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, weaponSpellPower));
    public static final Weapon.Entry WitherAxe= axeWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHER_STAR)))
            .attribute(ItemConfig.Attribute.bonus(SpellSchools.SOUL.id, weaponSpellPower));

    //BOWS
    //BOWS
    public static int normal_bow_velocity = 0;
    public static float enhanced_bow_velocity = 3.25F;

    private static RangedEntry elder_bow(String name, int durability, Supplier<Ingredient> repairIngredientSupplier, RangedConfig defaults) {
        var settings = new FabricItemSettings().maxDamage(durability);
        var item = new ElderGuardianRangedWeapon(settings, repairIngredientSupplier);
        ((CustomRangedWeapon)item).configure(defaults);
        return addRanged(new Identifier(RPGLoot.MOD_ID, name), item, defaults);
    }
    public static RangedEntry elder_guardian_bow = elder_bow("elder_guardian_bow", ToolMaterials.NETHERITE.getDurability(),
            () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA),
            new RangedConfig(vanilla_bow_pull_time, 11.5F, enhanced_bow_velocity));
    public static RangedEntry ender_dragon_bow = bow("ender_dragon_bow", ToolMaterials.NETHERITE.getDurability(),
            () -> Ingredient.ofItems(Items.END_CRYSTAL),
            new RangedConfig(vanilla_bow_pull_time, 11.5F, enhanced_bow_velocity));
    public static RangedEntry wither_bow = bow("wither_bow", ToolMaterials.NETHERITE.getDurability(),
            () -> Ingredient.ofItems(Items.NETHER_STAR),
            new RangedConfig(vanilla_bow_pull_time, 11.5F, enhanced_bow_velocity));



    ////MODDED WEAPONS
    ///ARCHERS
    private static final float spearAttackDamage = 7.5F;
    private static Weapon.Entry spearElder(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new ElderGuardianMeeleWeapon(material,settings);
        return entryMelee("archers", "elder_guardian_spear", material, item, new ItemConfig.Weapon(spearAttackDamage, archers_spearAttackSpeed));
    }
    public static final Weapon.Entry elderGuardianSpear=  spearElder(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.HEART_OF_THE_SEA)))
            .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, weaponSpellPower));

    private static Weapon.Entry spearDragon(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new DragonMeeleeWeapon(material,settings);
        return entryMelee("archers", "ender_dragon_spear", material, item, new ItemConfig.Weapon(spearAttackDamage, archers_spearAttackSpeed));
    }
    public static final Weapon.Entry enderDragonSpear=  spearDragon(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.END_CRYSTAL)))
            .attribute(ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, weaponSpellPower));

    private static Weapon.Entry spearWither(Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        var item = new WitherMeleeWeapon(material,settings);
        return entryMelee("archers", "wither_spear", material, item, new ItemConfig.Weapon(spearAttackDamage, archers_spearAttackSpeed));
    }
    public static final Weapon.Entry witherSpear=  spearWither(
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.WITHER_SKELETON_SKULL)))
            .attribute(ItemConfig.Attribute.bonus(SpellSchools.SOUL.id, weaponSpellPower));

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
