# 1.21.1 Rewrite - What's New
## NeoForge Support
- Loot & Explore now runs on **NeoForge** as well as Fabric
- Relic trinkets use **Curios** on NeoForge and **Trinkets** on Fabric
## New Weapons
- Every themed weapon set is now a full arsenal - **Sword, Axe, Mace, Shield, Bow and Crossbow**
  - Ender Dragon, Elder Guardian (Deep Sea), Wither and Glacial (Monarch's) themes
  - Maces & Shields are not added if LNE Paladins is installed, Bows & Crossbows are not added if LNE Archers is installed - those mods bring their own
- New Frozen Depths weapons, wielded by the Frozen Depths mobs: **Frost Haunt Axe**, **Frozen Bow**, **Guard's Frost Lance** and **Monarch's Frost Staff**
## The Frozen Depths
- New enemies: **Undead Frozen Mage**, **Frosted Ranger**, **Monarch's Soldier**, **Monarch's Guard** and the **Frost Hound**, each with its own model, animations and abilities
- The Frosted Ranger turns to face its target while drawing and its bow visibly draws back before each shot
- The Undead Frozen Mage keeps its distance: it closes in when its target is far away and backs off when approached
- The Undead Frozen Mage's main attack is a single icicle shot straight at its target
- The Undead Frozen Mage's long-range spell conjures a formation of floating icicles around it (8 for the General) that launch one after another at the target
- The Undead Frozen Mage, Frosted Ranger and Monarch's Soldier have a tougher "**General**" elite variant
- Melee attacks of the Frozen Depths mobs wind up first and deal damage on the animation's impact frame - a target that steps away in time is missed
- The **Frost Monarch** and **Frosthaunt** have been fully remodeled with new animations and reworked behavior
  - New Frost Monarch abilities: Icicle attacks, Hailstorm and a limited-use **Ice Barrier** (2 / 3 / 4 uses per fight on Easy / Normal / Hard)
  - The Frost Monarch does not despawn when players leave, keeps its health and remaining Ice Barrier uses, and only one can be summoned at a time
  - A spawn overlay announces the Frost Monarch's awakening
- **Glacial Tomb** generation reworked - larger, adapts to the terrain and is decorated with icicles
- Buy a **Frost Monarch Temple map** from an Expert Cartographer villager
- New **Frozen Depths Music Disc**, plus ambient music that plays while you are inside the Glacial Tomb, and dedicated Frost Monarch battle music
- Frozen Depths mobs are immune to freezing and can walk on powder snow
## New Blocks
- Decorative frozen set: **Frozen Torch**, **Frozen Bones**, **Frozen Adventurer**, **Pointed Icicles**, **Icicle Bars** and **Fragile Ice**
- Pointed Icicles fall as a chain when disturbed
- **Oak / Spruce / Acacia Hanging Inn Signs**
- **Monarch's Crown** and **Frozen Soul Block** - lore blocks for the Frost Monarch questline (Shift + Right Click the Crown to listen to the souls); the Frozen Soul Block is craftable
## New Consumables
- **Critical Strike** mod support - Blackpepper Bread, Inferno Chilli Oil, Grilled Eel Sushi and the Phantomstrike Tonic
- **Mossflower Tea** - boosts Nature Spell Power (More RPG Library)
## Creative Menu
- Loot & Explore content is now organised into the mod's own tabs
- The old "LNE Items & Blocks" tab is split in two:
  - **LNE - Generic Items & Blocks** - the Innkeeper Shelf and Hanging Inn Signs
  - **LNE Frozen Depths Items & Blocks** - all the frozen blocks, keys, spawn eggs, the music disc and the Frozen Depths weapons
- **LNE Equipment** and **LNE Drinks & Food** are unchanged
## Config
- Relic drops are now fully data-driven in **`config/loot_n_explore/relic_loot.json`**
  - Every entry is a loot table with its own drop chance (`rolls`) and item list, so you can retune drop rates, point relics at different loot tables, add your own drops, or disable them completely
  - This replaces the relic drop-chance values that used to live in `tweaks_v1.json` (that file is no longer used - defaults are generated on first launch)
- Frozen Depths mobs can be rebalanced in **`config/loot_n_explore/entity_attributes_v1.json`**
  - `attributes`: health, damage, armor, spell power and more, with separate Normal / Hard multipliers
  - `custom`: ability values such as cooldowns (in ticks), arrow / icicle / hailstone counts, ranges, damage multipliers, the Frost Monarch's Ice Barrier uses and the mob it summons
  - `equipment`: the items each mob spawns with (main hand, off hand, helmet, chestplate, leggings, boots), as an item or a random pick from an item tag, with spawn, enchant and drop chances
  - Changes apply to newly spawned mobs (ability values also to mobs already in the world). Keys that are missing from an older file use the built-in defaults - delete the file to regenerate it with every key
## Compatibility
- Relics also work as standalone items without Spell Engine / More RPG Library, with identical loot injection
- Built for Spell Engine 1.10.9, More RPG Library 2.8.0 and Ranged Weapon API 3.0
- More RPG Library's mod id changed from `more_rpg_classes` to `mrpg_lib` in 2.8.0 - if you maintain a modpack override or datapack that references the old `more_rpg_classes:` namespace (spells, particles, tags), update it to `mrpg_lib:`
- Frozen Depths mobs are tagged for **Frostiful** (benefits from cold)
