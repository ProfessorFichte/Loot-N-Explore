package more_rpg_loot.config;

public class TweaksConfig {
    public float boss_relic_dropchance = 1.0F;
    public float chest_relic_dropchance = 0.4F;
    public float archaeology_relic_dropchance = 0.7F;
    public float block_relic_dropchance = 0.025F;
    public float entity_relic_dropchance = 0.025F;
    // 1.20.1: the frozen trial spawner and frozen vault do not exist on this game version, so the
    // four `trial_spawner_*` / `vault_*` chances are replaced by the two chest tables that took over
    // their relic slots (see LootInjection).
    public float glaze_tower_relic_dropchance = 0.35F;
    public float glacial_tomb_relic_dropchance = 0.75F;
}
