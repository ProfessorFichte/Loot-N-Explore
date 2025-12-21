package more_rpg_loot.datagen;

import more_rpg_loot.compat.items.CompatItems;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.compat.spell_engine.LNE_Weapons;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.consumables.InnkeeperFoodItem;
import more_rpg_loot.util.LneItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    public void generateRpgSeriesWeaponTags(List<Weapon.Entry> weapons) {
        Iterator var2 = weapons.iterator();

        while(var2.hasNext()) {
            Weapon.Entry weapon = (Weapon.Entry)var2.next();
            TagKey<Item> weaponType = RPGSeriesItemTags.WeaponType.get(weapon.category());
            FabricTagProvider<Item>.FabricTagBuilder weaponTag = this.getOrCreateTagBuilder(weaponType);
            weaponTag.addOptional(weapon.id());
            int tier = weapon.lootProperties().tier();
            if (tier >= 0) {
                FabricTagProvider<Item>.FabricTagBuilder tierTag = this.getOrCreateTagBuilder(RPGSeriesItemTags.LootTiers.get(tier, RPGSeriesItemTags.LootCategory.WEAPONS));
                tierTag.addOptional(weapon.id());
            }

            String lootTheme = weapon.lootProperties().theme();
            if (lootTheme != null && !lootTheme.isEmpty()) {
                FabricTagProvider<Item>.FabricTagBuilder themeTag = this.getOrCreateTagBuilder(RPGSeriesItemTags.LootThemes.get(lootTheme));
                themeTag.addOptional(weapon.id());
            }
        }

    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        for (var entry : LNE_Relics.entries) {
            getOrCreateTagBuilder(LneItemTags.RELICS)
                    .addOptional(entry.id());
        }

        for (var entry : LNE_Weapons.entries) {
            String name = entry.id().getPath();
            if (name.contains("ender_dragon")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_ENDER_DRAGON)
                        .addOptional(entry.id());
            } else if (name.contains("wither")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_WITHER)
                        .addOptional(entry.id());
            } else if (name.contains("glacial")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_GLACIAL)
                        .addOptional(entry.id());
            } else if (name.contains("elder_guardian")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_ELDER_GUARDIAN)
                        .addOptional(entry.id());
            }
        }
        generateRpgSeriesWeaponTags(LNE_Weapons.entries);

        for (var entry : CommonItems.all) {
            Item item = entry.item();
            int quality = getInnkeeperItemQuality(item);
            if (quality >= 0) {
                getOrCreateTagBuilder(getInnkeeperTagForTier(quality))
                        .add(item);
            }
        }

        for (var entry : CompatItems.ALL) {
            int quality = entry.getQuality();
            if (quality >= 0) {
                getOrCreateTagBuilder(getInnkeeperTagForTier(quality))
                        .addOptional(more_rpg_loot.RPGLoot.id(entry.name()));
            }
        }
    }

    private int getInnkeeperItemQuality(Item item) {
        if (item instanceof InnkeeperDrinkItem drinkItem) {
            return drinkItem.getQuality();
        } else if (item instanceof InnkeeperBowlItem bowlItem) {
            return bowlItem.getQuality();
        } else if (item instanceof InnkeeperFoodItem foodItem) {
            return foodItem.getQuality();
        }
        return -1;
    }

    private TagKey<Item> getInnkeeperTagForTier(int tier) {
        return switch (tier) {
            case 0 -> LneItemTags.INNKEEPER_BUFFS_0;
            case 1 -> LneItemTags.INNKEEPER_BUFFS_1;
            case 2 -> LneItemTags.INNKEEPER_BUFFS_2;
            case 3 -> LneItemTags.INNKEEPER_BUFFS_3;
            default -> throw new IllegalArgumentException("Invalid innkeeper item tier: " + tier);
        };
    }
}
