package lne.entity.frozen_depths.goal;

import com.mrpg_lib.entity.ISpellCasterEntity;
import lne.LneMod;
import lne.compat.spell_engine.LneMobSpellGoalFactory;
import lne.internal.platform.LNEPlatform;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public final class MobSpellGoals {
    private MobSpellGoals() {
    }

    public interface Factory {
        Goal weaponSkills(Options options);

        Goal spells(String spellOrTag, Options options);

        boolean hasWeaponSkills();

        boolean hasSpells(String spellOrTag);
    }

    public interface CastAnimation {
        void start(int castTicks);

        default void release() {
        }

        void stop();
    }

    public record Options(BooleanSupplier gate, IntSupplier cooldownTicks, Runnable onFinish,
                          int customCastTicks, @Nullable CastAnimation animation) {
        public static Options of(BooleanSupplier gate) {
            return new Options(gate, () -> 0, () -> {}, 0, null);
        }

        public Options cooldown(IntSupplier ticks) {
            return new Options(gate, ticks, onFinish, customCastTicks, animation);
        }

        public Options onFinish(Runnable action) {
            return new Options(gate, cooldownTicks, action, customCastTicks, animation);
        }

        public Options customCasting(int castTicks, CastAnimation animation) {
            return new Options(gate, cooldownTicks, onFinish, castTicks, animation);
        }
    }

    public static String tag(String entity, String slot) {
        return "#" + LneMod.MOD_ID + ":mob/" + entity + "/" + slot;
    }

    public static String tag(EntityType<?> type, String slot) {
        return tag(Registries.ENTITY_TYPE.getId(type).getPath(), slot);
    }

    @Nullable
    public static Factory create(ISpellCasterEntity caster) {
        if (!LNEPlatform.isModLoaded("spell_engine")) return null;
        return LneMobSpellGoalFactory.create(caster);
    }
}
