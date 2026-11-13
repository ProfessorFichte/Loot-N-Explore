package lne.util;

import lne.LneMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public final class LneTags {
    public static final TagKey<Biome> ICY_BIOMES = TagKey.of(RegistryKeys.BIOME, LneMod.id("icy_biomes"));
    public static final TagKey<Biome> FROSTSIEGE_BIOMES = TagKey.of(RegistryKeys.BIOME, LneMod.id("frostsiege_biomes"));
    public static final TagKey<StatusEffect> BLOCKED_BY_FROST_RESISTANCE = TagKey.of(RegistryKeys.STATUS_EFFECT, LneMod.id("blocked_by_frost_resistance"));
    public static final TagKey<EntityType<?>> GENERALS = TagKey.of(RegistryKeys.ENTITY_TYPE, LneMod.id("generals"));

    private LneTags() {
    }
}
