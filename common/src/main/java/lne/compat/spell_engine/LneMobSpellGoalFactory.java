package lne.compat.spell_engine;

import net.spell_engine.spellbinding.spellchoice.SpellChoices;
import com.mrpg_lib.compat.spell_engine.CustomCasting;
import com.mrpg_lib.compat.spell_engine.MobSpellCastGoal;
import com.mrpg_lib.entity.ISpellCasterEntity;
import lne.entity.frozen_depths.goal.MobFacing;
import lne.entity.frozen_depths.goal.MobSpellGoals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_engine.api.spell.registry.SpellRegistry;

import java.util.ArrayList;
import java.util.List;

public final class LneMobSpellGoalFactory implements MobSpellGoals.Factory {
    private final ISpellCasterEntity caster;
    private final List<MobSpellCastGoal> group = new ArrayList<>();

    private LneMobSpellGoalFactory(ISpellCasterEntity caster) {
        this.caster = caster;
    }

    public static MobSpellGoals.Factory create(ISpellCasterEntity caster) {
        return new LneMobSpellGoalFactory(caster);
    }

    @Override
    public Goal weaponSkills(MobSpellGoals.Options options) {
        return add(new GatedSpellCastGoal(caster, "", group, options).withWeaponSkills(true));
    }

    @Override
    public Goal spells(String spellOrTag, MobSpellGoals.Options options) {
        return add(new GatedSpellCastGoal(caster, spellOrTag, group, options));
    }

    @Override
    public boolean hasWeaponSkills() {
        MobEntity mob = caster.asMobEntity();
        if (SpellChoices.from(mob.getMainHandStack()) != null) return true;
        SpellContainer container = SpellContainerHelper.containerFromItemStack(mob.getMainHandStack());
        if (container == null || container.spell_ids() == null) return false;
        var registry = SpellRegistry.from(mob.getWorld());
        for (String spellId : container.spell_ids()) {
            Identifier id = Identifier.tryParse(spellId);
            if (id != null && registry.getEntry(id).filter(entry -> entry.value().type == Spell.Type.ACTIVE).isPresent()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasSpells(String spellOrTag) {
        var world = caster.asMobEntity().getWorld();
        if (spellOrTag.startsWith("#")) {
            return !SpellRegistry.entries(world, spellOrTag.substring(1)).isEmpty();
        }
        Identifier id = Identifier.tryParse(spellOrTag);
        return id != null && SpellRegistry.from(world).getEntry(id).isPresent();
    }

    private Goal add(MobSpellCastGoal goal) {
        group.add(goal);
        return goal;
    }

    private static final class GatedSpellCastGoal extends MobSpellCastGoal {
        private final MobEntity mob;
        private final MobSpellGoals.Options options;
        private long readyAt;
        private boolean started;

        GatedSpellCastGoal(ISpellCasterEntity caster, String spellOrTag, List<MobSpellCastGoal> group, MobSpellGoals.Options options) {
            super(caster, spellOrTag, group);
            this.mob = caster.asMobEntity();
            this.options = options;
            if (options.animation() != null) {
                withCustomCasting(new AnimatedCasting(options.customCastTicks(), options.animation()));
            } else {
                withAnimations(true);
            }
        }

        @Override
        public boolean canStart() {
            return options.gate().getAsBoolean() && mob.getWorld().getTime() >= readyAt && seesTarget() && super.canStart();
        }

        private boolean seesTarget() {
            LivingEntity target = mob.getTarget();
            return target != null && target.isAlive() && mob.getVisibilityCache().canSee(target);
        }

        @Override
        public boolean canStop() {
            return !isActive();
        }

        @Override
        public void start() {
            started = true;
            super.start();
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = mob.getTarget();
            if (isActive() && target != null && target.isAlive()) {
                MobFacing.face(mob, target);
            }
        }

        @Override
        public void stop() {
            super.stop();
            if (started) {
                started = false;
                readyAt = mob.getWorld().getTime() + Math.max(0, options.cooldownTicks().getAsInt());
                options.onFinish().run();
            }
        }
    }

    private record AnimatedCasting(int ticks, MobSpellGoals.CastAnimation animation) implements CustomCasting {
        @Override
        public int castTicks(MobEntity mob, Identifier spellId, int defaultTicks) {
            return ticks > 0 ? ticks : defaultTicks;
        }

        @Override
        public void onCastStart(MobEntity mob, Identifier spellId, int castTicks) {
            animation.start(castTicks);
        }

        @Override
        public void onRelease(MobEntity mob, Identifier spellId) {
            animation.release();
        }

        @Override
        public void onCastStop(MobEntity mob, Identifier spellId) {
            animation.stop();
        }
    }
}
