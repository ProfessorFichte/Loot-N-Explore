package more_rpg_loot.datagen;

import more_rpg_loot.compat.items.CompatItems;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.consumables.InnkeeperFoodItem;
import more_rpg_loot.util.LneItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        for (var entry : LNE_Relics.entries) {
            getOrCreateTagBuilder(LneItemTags.RELICS)
                    .addOptional(entry.id());
        }

        var allWeaponEntries = new java.util.ArrayList<LNE_WeaponItems.Entry>();
        allWeaponEntries.addAll(LNE_WeaponItems.entries);
        allWeaponEntries.addAll(LNE_WeaponItems.rangedEntries);
        allWeaponEntries.addAll(LNE_WeaponItems.maceEntries);
        for (var entry : allWeaponEntries) {
            String name = entry.name();
            if (name.contains("ender_dragon")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_ENDER_DRAGON).addOptional(entry.id());
            } else if (name.contains("wither")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_WITHER).addOptional(entry.id());
            } else if (name.contains("glacial")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_GLACIAL).addOptional(entry.id());
            } else if (name.contains("elder_guardian")) {
                getOrCreateTagBuilder(LneItemTags.WEAPON_THEME_ELDER_GUARDIAN).addOptional(entry.id());
            }
        }

        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            TagKey<Item> swordTag = RPGSeriesItemTags.WeaponType.get(Equipment.WeaponType.SWORD);
            for (var entry : LNE_WeaponItems.entries) {
                getOrCreateTagBuilder(swordTag).addOptional(entry.id());
            }
        }

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
