package more_rpg_loot.compat.spell_engine;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class LNE_Abilities {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable net.spell_engine.client.gui.SpellTooltip.DescriptionMutator mutator) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    // ===== HELPER METHODS =====
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
    // MRPGLIB SAFETY SPELL SCHOOL
    public static SpellSchool waterSpellSchool() {
        if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            return MoreSpellSchools.WATER;
        } else{
            return SpellSchools.GENERIC;
        }
    }    
    // ===== SPELL DEFINITIONS =====

    public static Entry dragonclaw = add(dragonclaw());
    private static Entry dragonclaw() {
        var id = Identifier.of(MOD_ID, "dragonclaw");
        var title = "Dragonclaw";
        var description = "On melee hit: {trigger_chance} chance to deal extra {damage} to the target damage and heals the user for {heal} hearts.";

        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 0.35F;
        trigger.chance_batching = true;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var damage = SpellBuilder.Impacts.damage(0.6F, 0F);
        damage.attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(30F).speed(0.2F, 0.7F)),
                ParticleGroupBuilder.of("loot_n_explore:dragon_claw")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(2F).speed(0.2F, 0.5F)));

        var heal = SpellBuilder.Impacts.heal(0.025F);
        heal.attribute_from_target = true;
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.ASCEND, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .count(1F).speed(0.05F, 0.1F)
                                .verticalOrigin(Batches.FEET)));

        spell.impacts = List.of(damage, heal);
        SpellBuilder.Cost.cooldown(spell, 5.0F);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry waterbomb = add(waterbomb());
    private static Entry waterbomb() {
        var id = Identifier.of(MOD_ID, "waterbomb");
        var title = "Waterbomb";
        var description = "On melee hit: {trigger_chance} chance to deal {damage} damage around the target.";

        var spell = passiveSpellBase();
        spell.school = waterSpellSchool();
        spell.range = 7.5F;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        trigger.chance = 0.4F;
        spell.passive.triggers = List.of(trigger);

        // `more_rpg_classes` is an optional dependency (see `waterSpellSchool` above), so these stay
        // raw ids rather than `MoreParticles.*` constants: a static reference would break this class'
        // initializer when the library is absent. The library registers them as SpellEngine entries,
        // so their own appearance defaults still apply - and no site here overrides any of them.
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("more_rpg_classes:big_splash")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(30F).speed(0.5F, 0.75F)
                                .verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of("more_rpg_classes:water_circle")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(1F).speed(0.2F, 1.0F)
                                .verticalOrigin(Batches.FEET)));

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.distance_dropoff = Spell.Target.Area.DropoffCurve.NONE;
        spell.target.area.angle_degrees = 360.0F;
        spell.target.area.horizontal_range_multiplier = 1.0F;

        var damage = SpellBuilder.Impacts.damage( 0.4F,0);
        damage.attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:water_magic_impact1"), 0.4F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("more_rpg_classes:big_splash")
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .count(20F).speed(0.05F, 0.2F)
                                .verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of("more_rpg_classes:splash")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(15F).speed(0.05F, 0.2F)
                                .verticalOrigin(Batches.FEET)),
                ParticleGroupBuilder.of("more_rpg_classes:splash")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25F).speed(1.0F, 1.2F)));

        spell.impacts = List.of(damage);
        SpellBuilder.Cost.cooldown(spell, 5.0F);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry wither_pulse = add(wither_pulse());
    private static Entry wither_pulse() {
        var id = Identifier.of(MOD_ID, "wither_pulse");
        var title = "Wither Pulse";
        var description = "On melee hit: {trigger_chance} inflicts targets in a 90 degree radius with Wither and dealing {damage} damage.";

        var spell = passiveSpellBase();
        spell.school = SpellSchools.SOUL;
        spell.range = 5.0F;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 0.3F;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 90.0F; // 90 degree cone
        spell.target.area.horizontal_range_multiplier = 1.0F;

        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(15F).speed(0.05F, 0.5F)
                                .verticalOrigin(Batches.FEET)));

        var damage = SpellBuilder.Impacts.damage(0.5F,0);
        damage.attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();
        damage.sound = new Sound(Identifier.of("spell_engine:generic_soul_impact"));
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.DECELERATE)
                        .color(858993663L)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25F).speed(0.2F, 0.5F)));

        var witherEffect = SpellBuilder.Impacts.effectSet("wither",8.0F,1);
        witherEffect.action.status_effect.amplifier_cap = 10;
        witherEffect.action.status_effect.amplifier_power_multiplier = 0.15F;
        witherEffect.action.status_effect.show_particles = true;

        spell.impacts = List.of(damage, witherEffect);
        SpellBuilder.Cost.cooldown(spell, 10.0F);

        return new Entry(id, spell, title, description, null);
    }

    // ===== PASSIVE SPELLS =====
    public static Entry wither_touch = add(wither_touch());
    private static Entry wither_touch() {
        var id = Identifier.of(MOD_ID, "wither_touch");
        var title = "Wither's Touch";
        var description = "On Taking damage {trigger_chance} chance, to inflict wither on the attacker for {effect_duration} seconds.";

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.tier = 7;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var witherEffect = SpellBuilder.Impacts.effectSet("wither", 10.0F,1);
        witherEffect.action.status_effect.show_particles = true;
        witherEffect.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.DECELERATE)
                        .color(858993663L)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25F).speed(0.2F, 0.25F)));

        spell.impacts = List.of(witherEffect);
        SpellBuilder.Cost.cooldown(spell, 20.0F);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry frozen_touch = add(frozen_touch());
    private static Entry frozen_touch() {
        var id = Identifier.of(MOD_ID, "frozen_touch");
        var title = "Frozen Touch";
        var description = "On Taking damage {trigger_chance} chance, to inflict freezing on the attacker for {effect_duration} seconds.";

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.tier = 7;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.chance = 0.25F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var freezingEffect = SpellBuilder.Impacts.effectAdd("loot_n_explore:freezing", 10.0F,1,2);
        freezingEffect.target_modifiers = List.of(
                createDenyModifier("#minecraft:freeze_immune_entity_types")
        );
        freezingEffect.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("loot_n_explore:freezing_snowflake")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(25F).speed(0.2F, 0.25F)));

        spell.impacts = List.of(freezingEffect);
        SpellBuilder.Cost.cooldown(spell, 20.0F);

        return new Entry(id, spell, title, description, null);
    }

    // ===== ACTIVE BUFF SPELLS =====
    public static Entry ender_dragon_scales = add(ender_dragon_scales());
    private static Entry ender_dragon_scales() {
        var id = Identifier.of(MOD_ID, "ender_dragon_scales");
        var title = "Ender Dragon's Regeneration";
        var description = "Use: Regenerates your health for {effect_duration} seconds and reducing incoming damage for 20%%.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("end_rod")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(40F).speed(0.6F, 0.8F)),
                ParticleGroupBuilder.of("dragon_breath")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(40F).speed(0.6F, 0.8F)));
        spell.release.sound = new Sound(Identifier.of("spell_engine", "generic_healing_impact_3"));

        spell.target.type = Spell.Target.Type.CASTER;

        var buffEffect = SpellBuilder.Impacts.effectSet("loot_n_explore:ender_dragon_scales", 7.0F,0);
        buffEffect.action.status_effect.amplifier_power_multiplier = 0.2F;

        spell.impacts = List.of(buffEffect);
        SpellBuilder.Cost.cooldown(spell, 70.0F);

        return new Entry(id, spell, title, description, null);
    }

    // ===== CONDITIONAL HEAL SPELL =====
    public static Entry poseidons_grace = add(poseidons_grace());
    private static Entry poseidons_grace() {
        var id = Identifier.of(MOD_ID, "poseidons_grace");
        var title = "Poseidons Grace";
        var description = "On taking damage: {trigger_chance} chance to heal the player for {heal} hearts, if the player is below 50%% health.";

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.chance = 0.75F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        var condition = new Spell.TargetCondition();
        condition.health_percent_below = 0.5F;
        trigger.caster_conditions = List.of(condition);

        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var heal = SpellBuilder.Impacts.heal(0.05F);
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.ASCEND, Color.BLUE)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .count(1F).speed(0.05F, 0.1F)
                                .verticalOrigin(Batches.FEET)));

        spell.impacts = List.of(heal);
        SpellBuilder.Cost.cooldown(spell, 5.0F);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry enderman_teleport = add(enderman_teleport());
    private static Entry enderman_teleport() {
        var id = Identifier.of(MOD_ID, "enderman_teleport");
        var title = "Corrupted Teleport";
        var description = "Use: Teleports you forwards for {teleport_distance} blocks.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");
        spell.release.sound = new Sound(Identifier.of("minecraft", "entity.enderman.teleport"));

        var teleportImpact = new Spell.Impact();
        teleportImpact.action = new Spell.Impact.Action();
        teleportImpact.action.type = Spell.Impact.Action.Type.TELEPORT;
        teleportImpact.action.teleport = new Spell.Impact.Action.Teleport();
        teleportImpact.action.teleport.mode = Spell.Impact.Action.Teleport.Mode.FORWARD;
        teleportImpact.action.teleport.forward = new Spell.Impact.Action.Teleport.Forward();
        teleportImpact.action.teleport.forward.distance = 30.0F;

        teleportImpact.action.teleport.depart = Fx.Visuals.of(
                ParticleGroupBuilder.of("minecraft:portal")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20F).speed(0.3F, 0.5F)
                                .preTravel(1F)));

        teleportImpact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("minecraft:portal")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(40F).speed(0.1F, 0.3F)
                                .invert(true)
                                .preTravel(4F)));

        spell.impacts = List.of(teleportImpact);
        SpellBuilder.Cost.cooldown(spell, 120.0F);

        return new Entry(id, spell, title, description, null);
    }

    // ===== BEAM SPELL =====

    public static Entry elder_guardian_eye = add(elder_guardian_eye());
    private static Entry elder_guardian_eye() {
        var id = Identifier.of(MOD_ID, "elder_guardian_eye");
        var title = "Elder Guardian's Beam";
        var description = "Use: Cast a Guardian Beam dealing {damage} damage and lowering the attack damage for {effect_duration} seconds.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 32.0F;

        spell.active.cast = new Spell.Active.Cast();
        spell.active.cast.duration = 4;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:two_handed_channeling");
        spell.active.cast.sound = new Sound(Identifier.of("entity.guardian.attack"));
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 4;

        spell.target.type = Spell.Target.Type.BEAM;
        spell.target.beam = new Spell.Target.Beam();
        spell.target.beam.texture_id = "minecraft:textures/entity/guardian_beam.png";
        spell.target.beam.width = 0.08F;
        spell.target.beam.flow = 2.0F;
        spell.target.beam.block_hit = Fx.Visuals.of(
                // `count` of 1.5 is above 1, so it stays a count in V2 too (1.5 -> 2 spawns).
                ParticleGroupBuilder.of("bubble")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(1.5F).speed(0.1F, 0.2F)
                                .alignment(ParticleGroup.Alignment.LOOK)));

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 1.0F;
        damage.sound = Sound.withVolume(Identifier.of("entity.guardian.attack"), 0.4F);

        var curseEffect = SpellBuilder.Impacts.effectSet("loot_n_explore:elder_guardians_curse", 8.0F,0);
        spell.impacts = List.of(damage, curseEffect);
        SpellBuilder.Cost.cooldown(spell, 60.0F);

        return new Entry(id, spell, title, description, null);
    }

    // ===== PROJECTILE SPELL =====

    public static Entry wither_spine = add(wither_spine());
    private static Entry wither_spine() {
        var id = Identifier.of(MOD_ID, "wither_spine");
        var title = "Wither's Skull";
        var description = "Use: Shoots a Wither Skull dealing {damage} damage and inflicting wither for {effect_duration} seconds.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.SOUL;
        spell.range = 48.0F;

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_release");

        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(40F).speed(0.6F, 0.8F)));

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.launch_properties.velocity = 1.2F;


        var projectile = new Spell.ProjectileData();
        projectile.divergence = 5.0F;


        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(2F).speed(0.6F, 0.9F)
                                .alignment(ParticleGroup.Alignment.LOOK)));
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single(
                "loot_n_explore:spell_projectile/wither_skull", 1.5F, LightEmission.RADIATE);
        projectile.client_data.composite_model.models.get(0).rotate_degrees_per_tick = 0.0F;

        var damage = SpellBuilder.Impacts.damage(0.25F);
        damage.attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20F).speed(0.6F, 0.8F)));
        damage.sound = new Sound(Identifier.of("entity.generic.explode"));

        var witherEffect = SpellBuilder.Impacts.effectSet("wither", 5.0F,1);
        witherEffect.attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();
        witherEffect.action.status_effect.amplifier_power_multiplier = 0.25F;
        witherEffect.action.status_effect.show_particles = true;

        spell.impacts = List.of(damage, witherEffect);

        // Area impact on hit
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 2.0F;
        spell.area_impact.area = new Spell.Target.Area();
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;

        SpellBuilder.Cost.cooldown(spell, 45.0F);

        return new Entry(id, spell, title, description, null);
    }

    // ===== METEOR SPELL =====

    public static Entry avalanche = add(avalanche());
    private static Entry avalanche() {
        var id = Identifier.of(MOD_ID, "avalanche");
        var title = "Avalanche";
        var description = "On melee hit: {trigger_chance} chance to spawn a small avalanche, dealing {damage} damage and inflicting freezing for {effect_duration}.";

        var spell = passiveSpellBase();
        spell.school = SpellSchools.FROST;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 0.4F;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        // Meteor delivery
        spell.deliver = new Spell.Delivery();
        spell.deliver.type = Spell.Delivery.Type.METEOR;
        spell.deliver.meteor = new Spell.Delivery.Meteor();
        spell.deliver.meteor.launch_height = 4.0F;
        spell.deliver.meteor.launch_radius = 2.0F;
        spell.deliver.meteor.launch_properties.velocity = 0.5F;
        spell.deliver.meteor.launch_properties.extra_launch_count = 3;
        spell.deliver.meteor.launch_properties.extra_launch_delay = 2;
        var projectile = new Spell.ProjectileData();
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 12;
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.of("snowflake")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(3F).speed(0.0F, 0.1F)
                                .alignment(ParticleGroup.Alignment.LOOK)));
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single(
                "loot_n_explore:spell_projectile/small_avalanche");
        spell.deliver.meteor.projectile = projectile;

        var freezingEffect = SpellBuilder.Impacts.effectAdd("loot_n_explore:freezing", 10.0F,1,3);
        freezingEffect.action.status_effect.refresh_duration = false;
        freezingEffect.action.status_effect.show_particles = false;
        freezingEffect.target_modifiers = List.of(
                createDenyModifier("#minecraft:freeze_immune_entity_types")
        );
        var damage = SpellBuilder.Impacts.damage(0.3F);
        damage.attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.BURST, Color.FROST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15F).speed(0.2F, 0.4F)));
        damage.sound = new Sound(Identifier.of("spell_engine", "generic_frost_impact"));

        spell.impacts = List.of(freezingEffect, damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 2.0F;
        spell.area_impact.area = new Spell.Target.Area();
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("snowflake")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(10F).speed(0.5F, 2.0F)));

        SpellBuilder.Cost.cooldown(spell, 8.0F);

        return new Entry(id, spell, title, description, null);
    }

    // ===== CLOUD SPELL =====

    public static Entry frozen_soul = add(frozen_soul());
    private static Entry frozen_soul() {
        var id = Identifier.of(MOD_ID, "frozen_soul");
        var title = "Monarch's Freezing Storm";
        var description = "Use: Creates a powerful freezing storm for {cloud_duration} seconds, freezing the targets.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.FROST;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.DECELERATE, Color.FROST)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(40F).speed(0.6F, 0.8F)));
        spell.release.sound = new Sound(Identifier.of("spell_engine", "generic_frost_casting"));

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 6.0F;
        cloud.time_to_live_seconds = 15.0F;
        cloud.presence_sound = new Sound(Identifier.of("spell_engine", "generic_frost_impact"));
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of("loot_n_explore:freezing_snowflake")
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .count(20F).speed(0.1F, 0.3F)
                                .verticalOrigin(Batches.FEET)));
        spell.deliver.clouds = List.of(cloud);

        var freezingEffect = SpellBuilder.Impacts.effectAdd("loot_n_explore:freezing", 2.0F,1,4);
        freezingEffect.action.status_effect.refresh_duration = false;
        freezingEffect.target_modifiers = List.of(
                createDenyModifier("#minecraft:freeze_immune_entity_types")
        );

        spell.impacts = List.of(freezingEffect);
        SpellBuilder.Cost.cooldown(spell, 70.0F);

        return new Entry(id, spell, title, description, null);
    }

}
