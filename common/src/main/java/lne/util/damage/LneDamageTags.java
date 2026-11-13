package lne.util.damage;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public final class LneDamageTags {
    public static final TagKey<DamageType> CLOSES_ETERNAL_WINTER_PORTAL = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MOD_ID, "closes_eternal_winter_portal"));
    public static final Identifier SPELL_POWER_FIRE = Identifier.of("spell_power", "fire");

    private LneDamageTags() {
    }
}
