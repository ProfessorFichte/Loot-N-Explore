# 1.0.22+1.20.1

> ### ⚠️ Read this before updating
>
> This release is a **major technical overhaul and is not backwards compatible.**
>
> - **Requires the matching Spell Engine and More RPG Library releases.** This version will not run on
>   Spell Engine **0.9.x**, and mods built against 0.9.x will not work alongside it.
> - **Update the whole set together.** Spell Engine, More RPG Library and every RPG Series mod must be on
>   matching versions. Mixing in an older add-on will break at startup or misbehave in play.
>
> **Back up your world before updating.**

- Thanks to Daedelus for the PR!
- Ported to Minecraft 1.20.1 (Fabric). This line is Fabric-only, like the 1.21.1 branch.
- Requires Structure Pool API 1.2.1 and Fabric API 0.92; works with the matching 1.20.1 releases of
  Spell Engine (1.10.5), Spell Power (1.6.0), RangedWeaponAPI (2.3.4) and More RPG Library (2.7.2).
- Thermoo support targets the 1.20.1-compatible Thermoo 2.x line.
- Fixed: the four frost mobs' `initialize` hook now actually runs, so their cold immunity and their
  per-mob attack damage are applied on spawn (on 1.21.1 the method never matched the game's signature).

### Accepted 1.20.1 limitations

- The Frozen Vault, the Frozen Trial Spawner and their two keys are gone: both blocks wrap vanilla
  classes that only exist from 1.20.5 on. The Glaze Tower and the Small Cold Dungeon use the ordinary
  spawner-and-chest designs Fichte built for 1.20.1 instead, and the vault reward loot tables are
  replaced by relic drops from the ordinary structure chests.
- Loot injection can no longer tell a built-in loot table from one a datapack replaced, so a datapack
  that fully overrides an injected table still receives this mod's extra pool.
- The four smithing templates display vanilla's "Smithing Template" name: 1.20.1 hardcodes that
  translation key for every smithing template item.
- `frostmonarch_temple` no longer excludes Trial Chambers (1.21 content); it excludes Ancient Cities.
