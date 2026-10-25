package more_rpg_loot.util.damage;

import com.mrpg_lib.util.damage.SpellDamageProfile;
import net.minecraft.util.Identifier;

public final class FrostDamageProfiles {
    public static final Identifier FROST = Identifier.of("spell_power", "frost");

    public static final SpellDamageProfile GROUND_ICICLE = SpellDamageProfile.of(FROST, 0.75, 6.0);
    public static final SpellDamageProfile BARRIER_CLOUD = SpellDamageProfile.of(FROST, 0.5, 4.0);
    public static final SpellDamageProfile FLYING_ICICLE = SpellDamageProfile.of(FROST, 0.5, 5.0);
    public static final SpellDamageProfile FROSTBALL_HIT = SpellDamageProfile.of(FROST, 0.25, 0.0)
            .withFlat(2.0)
            .withAttackWeightWithoutSpellPower(0.0);
    public static final SpellDamageProfile FROSTBALL_IMPACT = SpellDamageProfile.of(FROST, 0.25, 0.0)
            .withFlat(4.0)
            .withAttackWeightWithoutSpellPower(0.0);
    public static final SpellDamageProfile FROSTBALL_CLOUD = SpellDamageProfile.of(FROST, 0.2, 1.0);

    private FrostDamageProfiles() {
    }
}
