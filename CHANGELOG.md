# 1.1.0 - 1.21.1

# 1.0.16 - 1.21.1
- fix crash without the trinkets mod

# 1.0.15 - 1.21.1
- Fix Missing Dettlaffs Blood Effect Texture 
- Spell Engine & Optional Dependencies Update

# 1.0.14 - 1.21.1
- Removed Dehydration Compat (Extra Datapack will be released)
- fix crash with Frost Monarchs Screech Goal
- nerf LNE-Swords & Axe's Damage

# 1.0.13 - 1.21.1
- Spell Engine 1.7
- hide tooltip of weapon passives
- forgot to add tier3 buff items to item tag
- internal changes with villager trading code
- changed Villager Trades
- nerf lifesteal potion

# 1.0.12 - 1.21.1
- fix trinkets not giving attribute modifiers
- Compatibility with Lithostitched 1.4
- reduce weight of Inn injection via structure pool api

# 1.0.11 - 1.21.1
- Update Loot Injection Config
- Fix Glaze Tower Trial Spawner Loot Table

# 1.0.10- 1.21.1
- fix issue with fabric-mod.json

# 1.0.9 - 1.21.1
- Update License
- Update Mod Icon

# 1.0.8 - 1.21.1
- The Frost Monarch now has its own Java Model with custom animations
- Some internal technical changes for his special effects
- When the Frost Monarch is not on fire, does not have full hp and his servants are around, he heals and is invulnerable to damage
- In his Off-Hand is a Frozen Soul, indicating he sucks out the life of his servants
- removed Rainbow Coral From Archeology Loot table (Injection Does not Work, only one item can drop from a brush-able block)
- you can now find the Rainbow Coral from Buried Treasure Chests

# 1.0.7 - 1.21.1
- improve glaze snowflake particles
- improve Frostmonarch Freezing Snowflake particles, there is now a small soul particle beam to his servants, indicating they heal the boss
- Removed the Quest Maps from the Innkeeper, these will be reintroduced with a new Villager Type
- fix Issue with map pricing that causes server crashing #5
- The Innkeeper now Sells cooked Meat on Level 3
- Fix Glacier Shard not being loot-able

# 1.0.6 - 1.21.1
- fix Tier 1 Damage foods give 50% Bonus
- Rebalance some passive spell damage modifiers

# 1.0.5 - 1.21.1
- fixed weird eye rendering for frostmonarch & frosthaunt

# 1.0.4 - 1.21.1
- fix status effect multiplication
- fix and tweak passive spells

# 1.0.3 - 1.21.1
# BUG FIXES & CHANGES
- fix Glacial Tomb Advancement
- Update some passives for the newest Spell Engine API
- fix some target modifiers in Passive Spell Impact
- Nerf Dragonclaw passive Heal
- fix frozen vaults not accepting any keys (unfortunately only works with new generated structures)
- nerfed and changed some Buf Consumables Attribute Values
- nerf effect duration
# NEW CONTENT
- separating exploring and equipment advancements
- Inject Loot & Explore Buff Drinks & Soups in other Loot Tables
- Add new T1 Drink -> Cactus Juice, giving the Damage Reflect Attribute
- LNE Buff Items now show the Attribute Modifiers in the tooltip
- added 9x new T3 Buff Consumables
- These epic Consumables give great buffs and absorption like an enchanted golden apple, they also heal you completely when consumed
- You can only have one of these buffs active
- These Consumables can only be looted

# 1.0.2 - 1.21.1
- fix waterbomb passive lang formatting
- fix Frost Resistance Status Effect
- add Wither's Curse Status Effect

# 1.0.1 - 1.21.1
- tweak some passive spells
- fix -> GlazeEntity (ClassCastException with FreezingEffect on 1.21.1) #3

# 1.0.0 - 1.21.1
# Official 1.21.1 Release!
# NEW STUFF
- Added Status Effects for each Boosting Drink & Bowl
- Added Honey Met, T3-Drink giving the Rage Attribute for the Berserker
- Added More Loot Items, which you'll need to craft the special weapons
- Added advancements, that guide you through the crafting progression
- All Loot Items give Attributes and some give passives or active spells
- Added Frosthaunt & Frostmonarch themed Vaults & Trial Spawners
- Passive Spells For the Weapons are now handled with the new Spell Engine Passive API
- they're all configurable with datapacks
# CHANGES
- Changed the boosting Soups & Drinks, you cant stack the effect amplifier anymore
- But you can combine all the effects freely
- Removed the status effect innkeepers proviant
- Drinks with a higher quality have a higher status effect duration and are stronger
- All Attribute Modifiers are configurable via Effects Config file
- Rebalanced Trading Costs
- Glacial Tomb, Cold Monster Rooms, Monarchs Temple & Glaze Tower received some Trial Spawners
# Even More Will come soon!