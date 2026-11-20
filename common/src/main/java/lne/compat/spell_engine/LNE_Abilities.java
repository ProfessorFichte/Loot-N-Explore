package lne.compat.spell_engine;

import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellSchool;
import com.mrpg_lib.compat.spell_power.DynamicSpellSchool;
import com.mrpg_lib.compat.spell_power.MoreSpellSchools;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import lne.effects.Effects;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.Easing;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static lne.LneMod.MOD_ID;

public class LNE_Abilities {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable net.spell_engine.client.gui.SpellTooltip.DescriptionMutator mutator) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static ParticleGroup pg(String id, ParticleGroup.Shape shape, float verticalOrigin, float count, float minSpeed, float maxSpeed) {
        return ParticleGroupBuilder.of(id).batch(b -> b.shape(shape)
                .verticalOrigin(verticalOrigin).count(count).speed(minSpeed, maxSpeed));
    }

    private static Spell passiveSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 7;

        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        return spell;
    }


    private static Spell.Impact.TargetModifier createDenyModifier(String entityTypeTag) {
        var modifier = new Spell.Impact.TargetModifier();
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityTypeTag;
        modifier.conditions = List.of(condition);
        modifier.execute = TriState.DENY;
        return modifier;
    }

    public static Entry frostbite_melee = add(frostbite(Spell.Trigger.Type.MELEE_IMPACT, "frostbite_melee", "On melee hit"));
    public static Entry frostbite_ranged = add(frostbite(Spell.Trigger.Type.ARROW_IMPACT, "frostbite_ranged", "On arrow hit"));
    private static Entry frostbite(Spell.Trigger.Type type, String name, String prefix) {
        var id = Identifier.of(MOD_ID, name);
        var title = "Frostbite";
        var description = prefix + ": inflicts Freezing for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        spell.school = SpellSchools.FROST;

        var trigger = new Spell.Trigger();
        trigger.type = type;
        trigger.chance = 1.0F;
        if (type == Spell.Trigger.Type.MELEE_IMPACT) {
            trigger.equipment_condition = EquipmentSlot.MAINHAND;
        }
        spell.passive.triggers = List.of(trigger);
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var freezing = SpellBuilder.Impacts.effectSet("loot_n_explore:freezing", 3.0F, 0);
        freezing.target_modifiers = List.of(createDenyModifier("#minecraft:freeze_immune_entity_types"));
        freezing.sound = Sound.withVolume(Identifier.of("spell_engine", "generic_frost_impact"), 0.5F);
        freezing.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("snowflake")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                                .count(8).speed(0.1F, 0.3F)));
        spell.impacts = List.of(freezing);
        return new Entry(id, spell, title, description, null);
    }

    public static final Identifier VOID_RIFT = Identifier.of(MOD_ID, "void_rift");
    public static final Identifier ICICLE_BARRAGE = Identifier.of(MOD_ID, "icicle_barrage");
    public static final Identifier MONARCHS_STAFF_SPELLS = Identifier.of(MOD_ID, "weapon/monarchs_frost_staff");
    public static final int ICICLE_BARRAGE_COUNT = 3;
    public static final Identifier SUMMON_SKELETON = Identifier.of(MOD_ID, "summon_skeleton");
    public static final Identifier SUMMON_FROST_HAUNTED = Identifier.of(MOD_ID, "summon_frost_haunted");
    public static final Identifier RAINBOW_CORAL = Identifier.of(MOD_ID, "rainbow_coral");
    public static final Identifier POSEIDONS_AMPHORA = Identifier.of(MOD_ID, "poseidons_amphora");

    private static Spell activeRelicSpell(SpellSchool school, float cooldownSeconds) {
        var spell = SpellBuilder.createSpellActive();
        spell.school = school;
        SpellBuilder.Cost.cooldown(spell, cooldownSeconds);
        return spell;
    }

    private static Spell relicPassive(SpellSchool school) {
        var spell = passiveSpellBase();
        spell.school = school;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.deliver = new Spell.Delivery();
        return spell;
    }

    private static Spell.Trigger withChance(Spell.Trigger trigger, float chance) {
        trigger.chance = chance;
        trigger.chance_batching = true;
        return trigger;
    }

    private static List<Spell.Trigger> damageDealt(float chance) {
        var triggers = new ArrayList<>(SpellBuilder.Triggers.meleeImpact());
        triggers.add(SpellBuilder.Triggers.arrowHit());
        triggers.add(spellDamageDealt());
        triggers.forEach(trigger -> withChance(trigger, chance));
        return triggers;
    }

    private static Spell.Trigger spellDamageDealt() {
        var spellHit = new Spell.Trigger();
        spellHit.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        spellHit.spell = new Spell.Trigger.SpellCondition();
        spellHit.spell.type = Spell.Type.ACTIVE;
        spellHit.impact = new Spell.Trigger.ImpactCondition();
        spellHit.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        return spellHit;
    }

    private static List<Spell.Trigger> kills(float chance) {
        var triggers = new ArrayList<>(SpellBuilder.Triggers.meleeKills());
        triggers.addAll(SpellBuilder.Triggers.rangedKill());
        triggers.add(SpellBuilder.Triggers.spellKill());
        triggers.forEach(trigger -> withChance(trigger, chance));
        return triggers;
    }

    private static void aroundCaster(Spell spell, float radius, boolean includeCaster) {
        spell.range = radius;
        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.include_caster = includeCaster;
    }

    private static void visibleCooldown(Spell spell, float seconds) {
        SpellBuilder.Cost.cooldown(spell, seconds);
        spell.cost.cooldown.haste_affected = false;
        spell.tooltip.show_header = true;
        spell.tooltip.show_activation = false;
        spell.tooltip.show_range = false;
    }

    private static Spell selfPassive(SpellSchool school) {
        var spell = relicPassive(school);
        spell.target.type = Spell.Target.Type.CASTER;
        return spell;
    }

    private static Spell.Impact customImpact(Identifier handler) {
        var impact = new Spell.Impact();
        impact.action = new Spell.Impact.Action();
        impact.action.type = Spell.Impact.Action.Type.CUSTOM;
        impact.action.custom = new Spell.Impact.Action.Custom();
        impact.action.custom.handler = handler.toString();
        impact.action.custom.intent = SpellTarget.Intent.HELPFUL;
        return impact;
    }

    private static Spell.Impact freezing(float seconds) {
        var freezing = SpellBuilder.Impacts.effectAdd("loot_n_explore:freezing", seconds, 1, 3);
        freezing.action.status_effect.refresh_duration = false;
        freezing.action.status_effect.show_particles = true;
        freezing.target_modifiers = List.of(createDenyModifier("#minecraft:freeze_immune_entity_types"));
        return freezing;
    }

    private static ParticleGroup burst(SpellEngineParticles.Entry particle, Color color, float count) {
        return ParticleGroupBuilder.magic(particle, ParticleGroup.Motion.BURST, color)
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                        .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                        .count(count).speed(0.2F, 0.5F));
    }

    public static Entry ender_dragon_scales = add(ender_dragon_scales());
    private static Entry ender_dragon_scales() {
        var id = Identifier.of(MOD_ID, "ender_dragon_scales");
        var title = "Dragon's Breath";
        var description = "Use: Create a growing dragon breath cloud around you for {cloud_duration} seconds. Every second it deals {damage} damage to enemies inside and heals you and your allies for {heal}.";

        var spell = activeRelicSpell(DynamicSpellSchool.DYNAMIC, 90.0F);
        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = new Sound(Identifier.of("entity.ender_dragon.growl"));
        spell.release.visuals = Fx.Visuals.of(
                pg("dragon_breath", ParticleGroup.Shape.CIRCLE, ParticleGroupBuilder.Batches.FEET, 40, 0.3F, 0.5F));
        spell.target.type = Spell.Target.Type.CASTER;

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 2.0F;
        cloud.volume.area = new Spell.Target.Area();
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.time_to_live_seconds = 6.0F;
        cloud.impact_tick_interval = 20;
        cloud.growth = new Spell.Delivery.Cloud.Growth();
        cloud.growth.radius_step = 0.5F;
        cloud.growth.step_interval = 20;
        cloud.growth.duration_ticks = 120;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 12;
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of("dragon_breath")
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .count(20F).speed(0F, 0.02F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.ASCEND, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .count(8F).speed(0.1F, 0.3F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)));
        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.4F);
        damage.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_arcane, Color.ARCANE, 10));
        var heal = SpellBuilder.Impacts.heal(0.25F);
        heal.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_heal, Color.HOLY, 6));
        spell.impacts = List.of(damage, heal);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry wither_spine = add(wither_spine());
    private static Entry wither_spine() {
        var id = Identifier.of(MOD_ID, "wither_spine");
        var title = "Skull Barrage";
        var description = "Use: Fire 3 wither skulls where you look. Each explodes in a 5 block area, dealing {damage} damage and inflicting Wither for {effect_duration} seconds.";

        var spell = activeRelicSpell(DynamicSpellSchool.DYNAMIC, 90.0F);
        spell.range = 48.0F;
        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_release");
        spell.release.sound = new Sound(Identifier.of("entity.wither.shoot"));
        spell.release.visuals = Fx.Visuals.of(
                pg("smoke", ParticleGroup.Shape.SPHERE, ParticleGroupBuilder.Batches.CENTER, 30, 0.4F, 0.6F));
        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.launch_properties.velocity = 1.2F;
        spell.deliver.projectile.launch_properties.extra_launch_count = 2;
        spell.deliver.projectile.launch_properties.extra_launch_delay = 3;
        spell.deliver.projectile.launch_properties.sound = new Sound(Identifier.of("entity.wither.shoot"));

        var projectile = new Spell.ProjectileData();
        projectile.divergence = 9.0F;
        projectile.client_data = new Spell.ProjectileData.Client();
        var witherSkullModel = SpellBuilder.ProjectileModels.model("loot_n_explore:spell_projectile/wither_skull", 1.5F, LightEmission.RADIATE);
        witherSkullModel.rotate_degrees_per_tick = 0.0F;
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(witherSkullModel);
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(2).speed(0.6F, 0.9F)));
        spell.deliver.projectile.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.8F);
        damage.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_skull, Color.RED, 12));
        damage.sound = new Sound(Identifier.of("entity.generic.explode"));
        var witherEffect = SpellBuilder.Impacts.effectSet("wither", 5.0F, 1);
        witherEffect.action.status_effect.show_particles = true;
        spell.impacts = List.of(damage, witherEffect);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 5.0F;
        spell.area_impact.area = new Spell.Target.Area();
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.area_impact.visuals = Fx.Visuals.of(
                pg("explosion", ParticleGroup.Shape.SPHERE, ParticleGroupBuilder.Batches.CENTER, 3, 0.2F, 0.4F),
                pg("smoke", ParticleGroup.Shape.SPHERE, ParticleGroupBuilder.Batches.CENTER, 30, 0.4F, 0.8F));
        return new Entry(id, spell, title, description, null);
    }

    private static final int ERUPTION_APEX_TICK = 5;

    private static final float ERUPTION_RISE = 2.0F;
    private static final int ERUPTION_RISE_TICKS = 8;
    private static final int ERUPTION_HOLD_TICKS = 4;
    private static final int ERUPTION_SINK_TICKS = 10;

    private static List<ModelEffect> eruptionIcicle() {
        int sinkStart = ERUPTION_RISE_TICKS + ERUPTION_HOLD_TICKS;
        int end = sinkStart + ERUPTION_SINK_TICKS;
        return List.of(ModelEffectBuilder.create("loot_n_explore:spell_effect/icicle_straight")
                .light(LightEmission.GLOW_TRANSLUCENT)
                .duration(end)
                .initialTranslateY(0.5F - ERUPTION_RISE)
                .translate(0.0F, ERUPTION_RISE, 0.0F, 0, ERUPTION_RISE_TICKS, Easing.EASE_OUT_QUAD)
                .translate(0.0F, -ERUPTION_RISE, 0.0F, sinkStart, end, Easing.EASE_IN_QUAD)
                .build());
    }

    public static Entry frozen_soul = add(frozen_soul());
    private static Entry frozen_soul() {
        var id = Identifier.of(MOD_ID, "frozen_soul");
        var title = "Frozen Eruption";
        var description = "Use: 20 icicles erupt from the ground around you in an X pattern, dealing {damage} damage and inflicting Freezing.";

        var spell = activeRelicSpell(DynamicSpellSchool.DYNAMIC, 60.0F);
        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.sound = new Sound(Identifier.of("spell_engine", "generic_frost_casting"));
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.DECELERATE)
                        .color(Color.FROST.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(40).speed(0.5F, 0.7F)));
        spell.target.type = Spell.Target.Type.CASTER;

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var clouds = new ArrayList<Spell.Delivery.Cloud>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        float axisStep = 1.6F / (float) Math.sqrt(2);
        for (int step = 1; step <= 5; step++) {
            for (var direction : directions) {
                boolean ringSound = direction == directions[0];
                var cloud = new Spell.Delivery.Cloud();
                cloud.volume.radius = 1.2F;
                cloud.time_to_live_seconds = (ERUPTION_APEX_TICK * 2 - 1) / 20F;
                cloud.impact_tick_interval = ERUPTION_APEX_TICK;
                cloud.delay_ticks = step * 2;
                cloud.placement = new Spell.EntityPlacement();
                cloud.placement.force_onto_ground = true;
                cloud.placement.location_offset_x = direction[0] * axisStep * step;
                cloud.placement.location_offset_z = direction[1] * axisStep * step;
                cloud.spawn = new Spell.Delivery.Cloud.Spawn();
                if (ringSound) {
                    cloud.spawn.sound = Sound.withVolume(Identifier.of("mrpg_lib", "glacial_spikes"), 0.6F);
                }
                cloud.spawn.visuals = Fx.Visuals.of(
                        ParticleGroupBuilder.of(SpellEngineParticles.frost_shard)
                                .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                        .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                        .count(12).speed(0.3F, 0.6F)),
                        ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                                .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                        .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                        .count(8).speed(0.1F, 0.3F))).models(eruptionIcicle());
                cloud.client_data = new Spell.Delivery.Cloud.ClientData();
                cloud.client_data.particles = List.of(
                        ParticleGroupBuilder.of(SpellEngineParticles.frost_shard)
                                .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                        .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                        .count(2).speed(0.1F, 0.2F)));
                clouds.add(cloud);
            }
        }
        spell.deliver.clouds = clouds;

        var damage = SpellBuilder.Impacts.damage(0.6F);
        damage.sound = new Sound(Identifier.of("spell_engine", "generic_frost_impact"));
        spell.impacts = List.of(damage, freezing(3.0F));
        return new Entry(id, spell, title, description, null);
    }

    private static final int GLACIAL_CURSE_STEPS = 4;
    private static final float GLACIAL_CURSE_DISTANCE = 5.0F;
    private static final float GLACIAL_CURSE_CLOSEST = 0.8F;
    private static final int GLACIAL_CURSE_STEP_TICKS = 3;

    public static Entry glacial_curse = add(glacial_curse());
    private static Entry glacial_curse() {
        var id = Identifier.of(MOD_ID, "glacial_curse");
        var title = "Glacial Curse";
        var description = "Spell hits: {trigger_chance} chance for icicles to close in on the target from 4 sides, dealing {damage} damage and Freezing.";

        var spell = relicPassive(SpellSchools.FROST);
        spell.tooltip.name = new Spell.Tooltip.LineOptions(true, true);
        spell.tooltip.description.show_in_compact = false;
        SpellBuilder.Cost.cooldown(spell, 15.0F);
        spell.passive.triggers = List.of(withChance(spellDamageDealt(), 0.20F));
        spell.range = 32.0F;
        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var clouds = new ArrayList<Spell.Delivery.Cloud>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int step = 0; step < GLACIAL_CURSE_STEPS; step++) {
            float distance = GLACIAL_CURSE_CLOSEST + (GLACIAL_CURSE_DISTANCE - GLACIAL_CURSE_CLOSEST) * (GLACIAL_CURSE_STEPS - 1 - step) / (GLACIAL_CURSE_STEPS - 1);
            float axis = distance / (float) Math.sqrt(2);
            for (var direction : directions) {
                var cloud = new Spell.Delivery.Cloud();
                cloud.volume.radius = 0.9F;
                cloud.time_to_live_seconds = (ERUPTION_APEX_TICK * 2 - 1) / 20F;
                cloud.impact_tick_interval = ERUPTION_APEX_TICK;
                cloud.delay_ticks = step * GLACIAL_CURSE_STEP_TICKS;
                cloud.placement = new Spell.EntityPlacement();
                cloud.placement.force_onto_ground = true;
                cloud.placement.location_offset_x = direction[0] * axis;
                cloud.placement.location_offset_z = direction[1] * axis;
                cloud.spawn = new Spell.Delivery.Cloud.Spawn();
                if (direction == directions[0]) {
                    cloud.spawn.sound = Sound.withVolume(Identifier.of("mrpg_lib", "glacial_spikes"), 0.4F);
                }
                cloud.spawn.visuals = Fx.Visuals.of(
                        ParticleGroupBuilder.of(SpellEngineParticles.frost_shard)
                                .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                        .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                        .count(8).speed(0.3F, 0.6F)),
                        ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                                .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                        .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                        .count(5).speed(0.1F, 0.3F))).models(eruptionIcicle());
                clouds.add(cloud);
            }
        }
        spell.deliver.clouds = clouds;

        var damage = SpellBuilder.Impacts.damage(0.4F);
        damage.sound = Sound.withVolume(Identifier.of("spell_engine", "generic_frost_impact"), 0.6F);
        spell.impacts = List.of(damage, freezing(3.0F));
        return new Entry(id, spell, title, description, null);
    }

    public static Entry elder_guardian_eye = add(elder_guardian_eye());
    private static Entry elder_guardian_eye() {
        var id = Identifier.of(MOD_ID, "elder_guardian_eye");
        var title = "Guardian's Gaze";
        var description = "Use: Channel a guardian beam for up to {cast_duration} seconds. Every half second it deals {damage} damage to the enemy you aim at and weakens it for {effect_duration} seconds.";

        var spell = activeRelicSpell(DynamicSpellSchool.DYNAMIC, 30.0F);
        spell.range = 16.0F;

        spell.active.cast = new Spell.Active.Cast();
        spell.active.cast.duration = 5;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:two_handed_channeling");
        spell.active.cast.sound = new Sound(Identifier.of("entity.guardian.attack"));
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 10;

        spell.target.type = Spell.Target.Type.BEAM;
        spell.target.beam = new Spell.Target.Beam();
        spell.target.beam.texture_id = "minecraft:textures/entity/guardian_beam.png";
        spell.target.beam.width = 0.08F;
        spell.target.beam.flow = 2.0F;
        spell.target.beam.block_hit = Fx.Visuals.of(
                ParticleGroupBuilder.of("bubble")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(1.5F).speed(0.1F, 0.2F)));

        var damage = SpellBuilder.Impacts.damage(0.3F);
        damage.sound = Sound.withVolume(Identifier.of("entity.guardian.attack"), 0.4F);
        var weakness = SpellBuilder.Impacts.effectSet("weakness", 3.0F, 0);
        spell.impacts = List.of(damage, weakness);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry icicle_barrage = add(icicle_barrage());
    private static Entry icicle_barrage() {
        var title = "Icicle Barrage";
        var description = "Conjure " + ICICLE_BARRAGE_COUNT + " icicles behind you. One after another they fly at the nearest enemy, each dealing {damage} damage and inflicting Freezing.";

        var spell = activeRelicSpell(SpellSchools.FROST, 0.0F);
        spell.cost.item = new Spell.Cost.Item();
        spell.cost.item.id = "runes:frost_stone";
        spell.cost.item.amount = 1;
        spell.range = 22.0F;
        spell.active.cast = new Spell.Active.Cast();
        spell.active.cast.duration = 1.8F;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_charge");
        spell.active.cast.sound = new Sound(Identifier.of("spell_engine", "generic_frost_casting"));
        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_release");
        spell.release.sound = Sound.withVolume(Identifier.of("block.amethyst_block.resonate"), 0.6F);
        spell.target.type = Spell.Target.Type.CASTER;

        spell.deliver.type = Spell.Delivery.Type.CUSTOM;
        spell.deliver.custom = new Spell.Delivery.Custom();
        spell.deliver.custom.handler = ICICLE_BARRAGE.toString();

        var damage = SpellBuilder.Impacts.damage(0.3F);
        damage.sound = Sound.withVolume(Identifier.of("spell_engine", "generic_frost_impact"), 0.6F);
        spell.impacts = List.of(damage, freezing(3.0F));
        return new Entry(ICICLE_BARRAGE, spell, title, description, null);
    }

    public static Entry corrupted_ender_pearl = add(corrupted_ender_pearl());
    private static Entry corrupted_ender_pearl() {
        var id = Identifier.of(MOD_ID, "corrupted_ender_pearl");
        var title = "Void Rift";
        var description = "Use: Throw a corrupted ender pearl and teleport to where it lands. A 7 block explosion there deals {damage} damage to enemies.";

        var spell = activeRelicSpell(DynamicSpellSchool.DYNAMIC, 60.0F);
        spell.range = 32.0F;
        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_release");
        spell.release.sound = new Sound(Identifier.of("entity.ender_pearl.throw"));
        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.CUSTOM;
        spell.deliver.custom = new Spell.Delivery.Custom();
        spell.deliver.custom.handler = VOID_RIFT.toString();

        var damage = SpellBuilder.Impacts.damage(1.0F);
        damage.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_arcane, Color.ARCANE, 15));
        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 7.0F;
        spell.area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("portal")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                                .count(80).speed(0.6F, 1.2F)),
                burst(SpellEngineParticles.magic_arcane, Color.ARCANE, 40));
        spell.area_impact.sound = new Sound(Identifier.of("entity.enderman.teleport"));
        return new Entry(id, spell, title, description, null);
    }

    public static Entry ender_dragon_tooth = add(ender_dragon_tooth());
    private static Entry ender_dragon_tooth() {
        var id = Identifier.of(MOD_ID, "ender_dragon_tooth");
        var title = "Rending Fang";
        var effect = "loot_n_explore:rending_fang";
        var description = "On melee hit: {trigger_chance_1} chance to enter a rending frenzy for {effect_duration} seconds. Every melee hit during the frenzy grants +1%% attack damage, up to " + Effects.RENDING_FANG_MAX_STACKS + " stacks.";

        var spell = relicPassive(ExternalSpellSchools.PHYSICAL_MELEE);
        var start = SpellBuilder.Triggers.meleeImpact();
        start.forEach(trigger -> {
            withChance(trigger, 0.05F);
            trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        });
        spell.passive.triggers = start;
        visibleCooldown(spell, 20.0F);

        var stacking = SpellBuilder.Triggers.meleeImpact();
        stacking.forEach(trigger -> trigger.target_override = Spell.Trigger.TargetSelector.CASTER);
        SpellBuilder.Deliver.stash(spell, effect, 10.0F, stacking);
        spell.deliver.stash_effect.consume = 0;

        var stack = SpellBuilder.Impacts.effectAdd(effect, 10.0F, 1, Effects.RENDING_FANG_MAX_STACKS - 1);
        stack.action.status_effect.refresh_duration = false;
        stack.sound = new Sound("minecraft:entity.ender_dragon.growl", 0.3F, 1.8F, 0.0F);
        stack.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_arcane, Color.ARCANE, 6));
        spell.impacts = List.of(stack);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry eternal_snowflake = add(eternal_snowflake());
    private static Entry eternal_snowflake() {
        var id = Identifier.of(MOD_ID, "eternal_snowflake");
        var title = "Eternal Blizzard";
        var description = "When you take damage: {trigger_chance} chance to summon a 4 block freezing storm around you for {cloud_duration} seconds, dealing {damage} damage every second and inflicting Freezing on enemies inside.";

        var spell = selfPassive(DynamicSpellSchool.DYNAMIC);
        spell.passive.triggers = List.of(withChance(SpellBuilder.Triggers.damageTaken(), 0.05F));
        visibleCooldown(spell, 15.0F);

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 4.0F;
        cloud.volume.area = new Spell.Target.Area();
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.time_to_live_seconds = 7.0F;
        cloud.impact_tick_interval = 20;
        cloud.presence_sound = new Sound(Identifier.of("spell_engine", "generic_frost_impact"));
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .count(14F).speed(0.05F, 0.2F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.DRIFT, Color.FROST)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .count(6F).speed(0.05F, 0.15F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)));
        spell.deliver.clouds = List.of(cloud);

        var freezing = SpellBuilder.Impacts.effectSet("loot_n_explore:freezing", 3.0F, 0);
        freezing.action.status_effect.show_particles = true;
        freezing.target_modifiers = List.of(createDenyModifier("#minecraft:freeze_immune_entity_types"));
        freezing.sound = Sound.withVolume(Identifier.of("spell_engine", "generic_frost_impact"), 0.5F);
        freezing.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_frost, Color.FROST, 8));
        spell.impacts = List.of(SpellBuilder.Impacts.damage(0.05F), freezing);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry charged_amethyst = add(charged_amethyst());
    private static Entry charged_amethyst() {
        var id = Identifier.of(MOD_ID, "charged_amethyst");
        var title = "Resonant Discharge";
        var description = "When you deal damage: {trigger_chance_1} chance of a 5 block arcane discharge around the target, dealing {damage} damage to enemies.";
        var spell = relicPassive(DynamicSpellSchool.DYNAMIC);
        spell.passive.triggers = damageDealt(0.20F);
        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 5.0F;
        cloud.time_to_live_seconds = 0.6F;
        cloud.impact_tick_interval = 10;
        cloud.spawn = new Spell.Delivery.Cloud.Spawn();
        cloud.spawn.sound = new Sound(Identifier.of("block.amethyst_block.resonate"));
        cloud.spawn.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_arcane, Color.ARCANE, 40));
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.ASCEND, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .count(6F).speed(0.1F, 0.3F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)));
        spell.deliver.clouds = List.of(cloud);
        var damage = SpellBuilder.Impacts.damage(0.4F);
        damage.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_arcane, Color.ARCANE, 12));
        damage.sound = Sound.withVolume(Identifier.of("block.amethyst_cluster.break"), 0.6F);
        spell.impacts = List.of(damage);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry glacier_shard = add(glacier_shard());
    private static Entry glacier_shard() {
        var id = Identifier.of(MOD_ID, "glacier_shard");
        var title = "Glacial Shards";
        var description = "When you deal damage: {trigger_chance_1} chance that 5 icicles shoot outward in a circle, each dealing {damage} damage.";
        var spell = relicPassive(DynamicSpellSchool.DYNAMIC);
        spell.passive.triggers = damageDealt(0.10F);
        spell.range = 24.0F;
        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.inherit_shooter_pitch = false;
        spell.deliver.projectile.direction_offsets = new Spell.Delivery.ShootProjectile.DirectionOffset[]{
                new Spell.Delivery.ShootProjectile.DirectionOffset(0, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(72, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(144, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(216, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(288, 0)};
        spell.deliver.projectile.direction_offsets_require_target = false;
        spell.deliver.projectile.launch_properties.velocity = 1.0F;
        spell.deliver.projectile.launch_properties.extra_launch_count = 4;
        spell.deliver.projectile.launch_properties.extra_launch_delay = 0;
        spell.deliver.projectile.launch_properties.sound = Sound.withVolume(Identifier.of("block.glass.break"), 0.5F);
        var projectile = new Spell.ProjectileData();
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(
                SpellBuilder.ProjectileModels.model("mrpg_lib:spell_projectile/glacial_arrow", 1.0F));
        spell.deliver.projectile.projectile = projectile;
        var damage = SpellBuilder.Impacts.damage(0.4F);
        damage.sound = new Sound(Identifier.of("spell_engine", "generic_frost_impact"));
        damage.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_frost, Color.FROST, 8));
        spell.impacts = List.of(damage);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry amphitrite_diadem = add(amphitrite_diadem());
    private static Entry amphitrite_diadem() {
        var id = Identifier.of(MOD_ID, "amphitrite_diadem");
        var title = "Soothing Tide";
        var description = "When you deal damage: {trigger_chance_1} chance of a 5 block soothing water area that heals you and your allies for {heal}.";
        var spell = relicPassive(MoreSpellSchools.WATER);
        spell.passive.triggers = damageDealt(0.10F);
        aroundCaster(spell, 5.0F, true);
        spell.release.sound = new Sound(Identifier.of("ambient.underwater.enter"));
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("splash")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(60).speed(0.3F, 0.6F)));
        var heal = SpellBuilder.Impacts.heal(0.3F);
        heal.visuals = Fx.Visuals.of(burst(SpellEngineParticles.magic_heal, Color.HOLY, 8));
        spell.impacts = List.of(heal);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry withered_obsidian_shard = add(withered_obsidian_shard());
    private static Entry withered_obsidian_shard() {
        var id = Identifier.of(MOD_ID, "withered_obsidian_shard");
        var title = "Withering Spite";
        var description = "When you take damage: {trigger_chance} chance of a 6 block withering aura that inflicts Wither on enemies for {effect_duration} seconds. The Wither level grows with your soul spell power.";
        var spell = relicPassive(SpellSchools.SOUL);
        spell.passive.triggers = List.of(withChance(SpellBuilder.Triggers.damageTaken(), 0.10F));
        aroundCaster(spell, 6.0F, false);
        spell.release.sound = new Sound(Identifier.of("entity.wither.ambient"));
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.DECELERATE)
                        .color(858993663L)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                                .count(40).speed(0.4F, 0.7F)));
        var wither = SpellBuilder.Impacts.effectSet_ScaledAmplifier_Cap("wither", 5.0F, 0, 0.1F, 2);
        wither.action.status_effect.show_particles = true;
        spell.impacts = List.of(wither);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry lost_soul = add(lost_soul());
    private static Entry lost_soul() {
        var id = Identifier.of(MOD_ID, "lost_soul");
        var title = "Soul Siphon";
        var description = "On kill: {trigger_chance_1} chance to heal for 15%% of the killed target's max health.";
        var spell = relicPassive(ExternalSpellSchools.PHYSICAL_MELEE);
        spell.passive.triggers = kills(0.05F);
        visibleCooldown(spell, 10.0F);
        var heal = SpellBuilder.Impacts.heal(0.15F);
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.attribute_from_target = true;
        heal.action.apply_to_caster = true;
        heal.sound = Sound.withVolume(Identifier.of("particle.soul_escape"), 0.8F);
        heal.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("soul")
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(14).speed(0.05F, 0.15F)),
                ParticleGroupBuilder.of("soul_fire_flame")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                                .count(8).speed(0.03F, 0.1F)),
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.BURST)
                        .color(0x5FD9E0FFL)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.CENTER)
                                .count(8).speed(0.2F, 0.4F)));
        spell.impacts = List.of(heal);
        return new Entry(id, spell, title, description, null);
    }

    public static Entry unknown_remains = add(unknown_remains());
    private static Entry unknown_remains() {
        var id = Identifier.of(MOD_ID, "unknown_remains");
        var title = "Risen Remains";
        var description = "On kill: {trigger_chance_1} chance that a friendly skeleton rises from the ground and fights alongside you for 20 seconds. It grows stronger with your soul spell power.";
        var spell = selfPassive(SpellSchools.SOUL);
        spell.passive.triggers = kills(0.04F);
        visibleCooldown(spell, 60.0F);
        spell.impacts = List.of(customImpact(SUMMON_SKELETON));
        return new Entry(id, spell, title, description, null);
    }

    public static Entry frozen_rib = add(frozen_rib());
    private static Entry frozen_rib() {
        var id = Identifier.of(MOD_ID, "frozen_rib");
        var title = "Frozen Legion";
        var description = "When you take damage: {trigger_chance} chance to raise 3 friendly Frost Haunted from the ground that fight alongside you for 20 seconds. They grow stronger with your frost spell power.";

        var spell = selfPassive(SpellSchools.FROST);
        spell.passive.triggers = List.of(withChance(SpellBuilder.Triggers.damageTaken(), 0.10F));
        visibleCooldown(spell, 60.0F);
        spell.impacts = List.of(customImpact(SUMMON_FROST_HAUNTED));
        return new Entry(id, spell, title, description, null);
    }

    public static Entry rainbow_coral = add(rainbow_coral());
    private static Entry rainbow_coral() {
        var title = "Prismatic Shell";
        var description = "When you receive healing: 5%% chance (one roll per second) to gain absorption hearts worth 20%% of your max health for 60 seconds.";
        var spell = relicPassive(MoreSpellSchools.WATER);
        spell.passive.triggers = List.of();
        visibleCooldown(spell, 30.0F);
        return new Entry(RAINBOW_CORAL, spell, title, description, null);
    }

    public static Entry poseidons_amphora = add(poseidons_amphora());
    private static Entry poseidons_amphora() {
        var title = "Tidal Ward";
        var description = "When you fall below 10%% health you are healed to 50%% health and take no damage for 5 seconds, but the damage is stored. When the ward ends you take 50%% of the stored damage.";
        var spell = relicPassive(MoreSpellSchools.WATER);
        spell.passive.triggers = List.of();
        visibleCooldown(spell, 60.0F);
        return new Entry(POSEIDONS_AMPHORA, spell, title, description, null);
    }

}