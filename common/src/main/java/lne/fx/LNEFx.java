package lne.fx;

import com.mrpg_lib.sounds.MRPGLibSounds;
import com.mrpg_lib.client.particle.ColoredParticles;
import com.mrpg_lib.client.particle.MoreParticles;
import com.mrpg_lib.fx.FxAnchor;
import com.mrpg_lib.fx.FxEffect;
import com.mrpg_lib.fx.FxLayer;
import com.mrpg_lib.fx.FxMotion;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.FxPresets;
import com.mrpg_lib.fx.FxShape;
import com.mrpg_lib.fx.FxSound;
import com.mrpg_lib.fx.FxVelocity;
import com.mrpg_lib.fx.MrpgFx;
import lne.particle.Particles;
import net.minecraft.block.Blocks;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import static lne.LneMod.MOD_ID;

public final class LNEFx {
    public static final int DRAGON = 0xB040F0;
    public static final int DRAGON_DARK = 0x4A1478;
    public static final int WITHER = 0x3A3A44;
    public static final int WITHER_DARK = 0x141418;
    public static final int ICE = 0xC8F0FF;
    public static final int ICE_DEEP = 0x7FD0FF;
    public static final int GUARDIAN = 0x4DE6CC;
    public static final int GUARDIAN_DEEP = 0x1A738C;
    public static final int AMETHYST = 0xC890FF;
    public static final int AMETHYST_DARK = 0x7A4AC0;
    public static final int PEARL = 0x8A3AD0;
    public static final int PEARL_DARK = 0x40106A;
    public static final int HEALING = 0xF82423;
    public static final int WARD_SAFE = 0x40C8FF;
    public static final int WARD_LETHAL = 0xFF2A2A;

    private LNEFx() {
    }

    private static FxEffect register(String path, FxEffect effect) {
        return MrpgFx.register(Identifier.of(MOD_ID, path), effect);
    }

    public static final FxEffect DRAGON_DRAIN = register("weapon/dragon_drain", FxEffect.builder()
            .layer(FxLayer.of(FxShape.POINT).count(18).velocity(FxVelocity.OUTWARD).speed(0.1F, 0.3F).particle(ParticleTypes.PORTAL))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.35F).count(12).velocity(FxVelocity.RANDOM).speed(0.01F, 0.04F).particle(ParticleTypes.DRAGON_BREATH))
            .layer(FxLayer.of(FxShape.BEAM).count(12).jitter(0.08F).velocity(FxVelocity.ALONG_AXIS).speed(0.02F, 0.05F).colored(ColoredParticles.END_ROD, DRAGON))
            .sound(SoundEvents.ENTITY_ENDER_DRAGON_FLAP, 0.7F, 1.3F)
            .sound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.5F, 0.8F)
            .build());

    public static final FxEffect DRAGON_CLAW = register("weapon/dragon_claw", FxEffect.builder()
            .layer(FxLayer.of(FxShape.POINT).count(1).particle(Particles.DRAGON_CLAW))
            .layer(FxLayer.of(FxShape.POINT).count(1).jitter(0.3F).delay(3).particle(Particles.DRAGON_CLAW))
            .layer(FxLayer.of(FxShape.POINT).count(1).jitter(0.3F).delay(6).particle(Particles.DRAGON_CLAW))
            .sound(MRPGLibSounds.CARVE.soundEvent(), 0.6F, 1.2F)
            .build());

    public static final FxEffect DRAGON_BURST = register("weapon/dragon_burst", FxPresets.EXPLOSION.tweak(t -> t
            .radius(5.0F).speed(0.3F)
            .color(DRAGON, DRAGON_DARK)
            .vanillaParticle(ParticleTypes.DRAGON_BREATH)
            .sound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.9F, 1.1F)
            .addSound(FxSound.of(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 0.3F, 1.8F))));

    public static final FxEffect WITHER_AURA = register("weapon/wither_aura", FxEffect.builder().radius(3.0F)
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).anchor(FxAnchor.GROUND).count(36).particle(ParticleTypes.SOUL_FIRE_FLAME))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(0.6F).anchor(FxAnchor.GROUND).yOffset(0.5F).count(20).colored(ColoredParticles.LARGE_SMOKE, WITHER))
            .layer(FxLayer.of(FxShape.DISC).radiusFromParams(0.8F).anchor(FxAnchor.GROUND).count(16).velocity(FxVelocity.UP).speed(0.02F, 0.06F).particle(ParticleTypes.SOUL))
            .sound(SoundEvents.ENTITY_WITHER_AMBIENT, 0.5F, 1.4F)
            .build());

    public static final FxEffect WITHER_BLAST = register("weapon/wither_blast", FxPresets.EXPLOSION.tweak(t -> t
            .radius(7.0F).speed(0.3F)
            .color(WITHER, WITHER_DARK)
            .vanillaParticle(ParticleTypes.SOUL_FIRE_FLAME)
            .sound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 0.6F, 1.3F)
            .addSound(FxSound.of(SoundEvents.ENTITY_WITHER_AMBIENT, 0.5F, 1.4F))));

    public static final FxEffect WITHER_SUMMON = register("weapon/wither_summon", FxPresets.BURST.tweak(t -> t
            .radius(0.6F).count(1.2F)
            .vanillaParticle(ParticleTypes.SOUL_FIRE_FLAME)
            .sound(SoundEvents.ENTITY_WITHER_SHOOT, 0.8F, 0.8F)));

    public static final FxEffect SKULL_BLAST = register("weapon/skull_blast", FxPresets.EXPLOSION.tweak(t -> t
            .radius(2.0F).speed(0.25F).count(0.7F)
            .color(WITHER, WITHER_DARK)
            .vanillaParticle(ParticleTypes.SOUL_FIRE_FLAME)
            .sound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 0.7F, 1.2F)));

    public static final FxEffect SPINE_BLAST = register("relic/spine_blast", FxEffect.builder().radius(5.0F)
            .layer(FxLayer.of(FxShape.POINT).count(1).colored(ColoredParticles.EXPLOSION_EMITTER, WITHER))
            .layer(FxLayer.of(FxShape.SPHERE).radiusFromParams(0.1F).count(30).velocity(FxVelocity.OUTWARD).speed(0.15F, 0.35F).speedFromRadius()
                    .particle(ParticleTypes.SOUL_FIRE_FLAME))
            .layer(FxLayer.of(FxShape.SPHERE).radiusFromParams(0.35F).count(20).velocity(FxVelocity.RANDOM).speed(0.02F, 0.06F)
                    .colored(ColoredParticles.LARGE_SMOKE, WITHER_DARK))
            .sound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1.0F, 0.8F)
            .sound(SoundEvents.ENTITY_WITHER_SHOOT, 0.6F, 0.7F)
            .build());

    public static final FxEffect FROSTBALL_RING = register("weapon/frostball_ring", FxEffect.builder().radius(1.5F)
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).count(12).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.SPHERE).radiusFromParams(0.7F).count(14).motion(FxMotion.DRIFT).colored(ColoredParticles.SNOWFLAKE, ICE))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(0.7F).anchor(FxAnchor.TARGET).count(16).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F).colored(ColoredParticles.CLOUD, ICE))
            .layer(FxLayer.of(FxShape.POINT).anchor(FxAnchor.TARGET).count(8).velocity(FxVelocity.RANDOM).speed(0.05F, 0.12F).particle(ParticleTypes.ITEM_SNOWBALL))
            .sound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.9F, 0.7F)
            .sound(MRPGLibSounds.FROST_CRACKLE.soundEvent(), 0.7F, 1.3F)
            .build());

    public static final FxEffect ICICLE_LINE = register("weapon/icicle_line", FxEffect.builder().length(10.0F)
            .layer(FxLayer.of(FxShape.LINE).lengthFromParams(1.0F).anchor(FxAnchor.GROUND).count(28).jitter(0.2F)
                    .velocity(FxVelocity.UP).speed(0.02F, 0.06F).colored(ColoredParticles.SNOWFLAKE, ICE))
            .layer(FxLayer.of(FxShape.POINT).count(12).velocity(FxVelocity.OUTWARD).speed(0.04F, 0.1F).colored(ColoredParticles.CLOUD, ICE))
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 0.9F, 0.6F)
            .build());

    public static final FxEffect GUARDIAN_SPLASH = register("weapon/guardian_splash", FxEffect.builder().radius(1.2F)
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).count(14).particle(ParticleTypes.BUBBLE_POP))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).count(14).velocity(FxVelocity.OUTWARD).speed(0.05F, 0.15F).particle(ParticleTypes.SPLASH))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).count(18).colored(ColoredParticles.CLOUD, GUARDIAN))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).count(6).velocity(FxVelocity.INWARD).speed(0.1F, 0.3F).particle(ParticleTypes.NAUTILUS))
            .layer(FxLayer.of(FxShape.DISC).radius(0.4F).yOffset(1.0F).count(8).particle(ParticleTypes.DRIPPING_WATER))
            .sound(SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.5F, 1.6F)
            .sound(MRPGLibSounds.WATER_MAGIC_IMPACT_1.soundEvent(), 0.5F, 1.2F)
            .sound(SoundEvents.WEATHER_RAIN, 0.4F, 1.2F)
            .build());

    public static final FxEffect WHIRLPOOL = register("weapon/whirlpool", FxPresets.SWIRL.tweak(t -> t
            .radius(4.0F).height(1.2F).turns(1.5F).duration(100).count(1.5F)
            .vanillaParticle(MoreParticles.FLOATING_BUBBLE)
            .secondaryVanillaParticle(ParticleTypes.CLOUD)
            .color(GUARDIAN, GUARDIAN)
            .sound(SoundEvents.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1.0F, 0.8F)
            .addSound(FxSound.of(SoundEvents.BLOCK_CONDUIT_ACTIVATE, 0.7F, 1.4F))
            .addSound(FxSound.of(SoundEvents.ENTITY_GENERIC_SPLASH, 0.5F, 1.2F).delay(10).repeat(20))));

    public static final FxEffect FROZEN_TEMPEST = register("weapon/frozen_tempest", FxPresets.STORM.tweak(t -> t
            .radius(3.0F).duration(100)
            .secondaryVanillaParticle(ParticleTypes.CLOUD)
            .sound(MRPGLibSounds.FROST_CRACKLE_LONG.soundEvent(), 0.9F, 1.0F)
            .addSound(FxSound.of(SoundEvents.BLOCK_POWDER_SNOW_BREAK, 1.0F, 0.6F))
            .addSound(FxSound.of(SoundEvents.BLOCK_POWDER_SNOW_STEP, 0.6F, 0.7F).delay(20).repeat(20))));

    public static final FxEffect BUBBLE_BURST = register("weapon/bubble_burst", FxEffect.builder().radius(2.5F)
            .layer(FxLayer.of(FxShape.SPHERE).radiusFromParams(0.25F).count(22).velocity(FxVelocity.OUTWARD).speed(0.08F, 0.2F).speedFromRadius()
                    .particle(MoreParticles.FLOATING_BUBBLE))
            .layer(FxLayer.of(FxShape.SPHERE).radiusFromParams(0.2F).count(16).velocity(FxVelocity.OUTWARD).speed(0.1F, 0.25F).particle(ParticleTypes.SPLASH))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(0.8F).count(18).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F).colored(ColoredParticles.POOF, GUARDIAN))
            .sound(MRPGLibSounds.WATER_BUBBLE_EXPLODE.soundEvent(), 0.9F, 1.1F)
            .sound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1.0F, 0.8F)
            .build());

    public static final FxEffect STAFF_CAST = register("staff/cast", FxPresets.CAST.tweak(t -> t
            .radius(0.5F)
            .color(ICE, ICE_DEEP)
            .secondaryVanillaParticle(ParticleTypes.SNOWFLAKE)
            .sound(MRPGLibSounds.FROST_CRACKLE.soundEvent(), 0.8F, 1.3F)
            .addSound(FxSound.of(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.8F, 0.8F))));

    public static final FxEffect STAFF_IMPACT = register("staff/impact", FxPresets.IMPACT.tweak(t -> t
            .radius(0.8F)
            .color(ICE, ICE_DEEP)
            .vanillaParticle(ParticleTypes.SNOWFLAKE)
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 0.7F, 1.5F)
            .addSound(FxSound.of(MRPGLibSounds.FROST_CRACKLE.soundEvent(), 0.6F, 1.0F))));

    public static final FxEffect ICICLE_SHATTER = register("icicle/shatter", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.2F).count(14).velocity(FxVelocity.OUTWARD).speed(0.08F, 0.18F)
                    .particle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState())))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.2F).count(6).velocity(FxVelocity.RANDOM).speed(0.01F, 0.04F).particle(ParticleTypes.SNOWFLAKE))
            .sound(FxSound.of(SoundEvents.BLOCK_GLASS_BREAK, 0.5F, 1.55F).pitchVariance(0.15F))
            .build());

    public static final FxEffect TOOTH_STACK = register("relic/tooth_stack", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.4F).count(12).velocity(FxVelocity.RANDOM).speed(0.01F, 0.03F).particle(ParticleTypes.DRAGON_BREATH))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.4F).count(12).velocity(FxVelocity.OUTWARD).speed(0.2F, 0.4F).particle(ParticleTypes.PORTAL))
            .sound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 0.3F, 1.8F)
            .build());

    public static final FxEffect AMETHYST_BURST = register("relic/amethyst_burst", FxPresets.EXPLOSION.tweak(t -> t
            .radius(5.0F).speed(0.25F)
            .color(AMETHYST, AMETHYST_DARK)
            .vanillaParticle(ParticleTypes.END_ROD)
            .sound(SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, 1.2F, 0.7F)
            .addSound(FxSound.of(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 0.5F, 1.4F))));

    public static final FxEffect PEARL_BLAST = register("relic/pearl_blast", FxPresets.EXPLOSION.tweak(t -> t
            .radius(7.0F).speed(0.3F)
            .color(PEARL, PEARL_DARK)
            .vanillaParticle(ParticleTypes.REVERSE_PORTAL)
            .sound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.5F)
            .addSound(FxSound.of(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 0.6F, 1.2F))));

    public static final FxEffect SKELETON_EMERGE = register("relic/skeleton_emerge", FxEffect.builder()
            .layer(FxLayer.of(FxShape.DISC).radius(0.5F).count(30).velocity(FxVelocity.UP).speed(0.05F, 0.15F)
                    .particle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState())))
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.3F).height(1.2F).count(10).velocity(FxVelocity.UP).speed(0.02F, 0.05F).particle(ParticleTypes.SOUL))
            .sound(SoundEvents.ENTITY_SKELETON_AMBIENT, 1.0F, 0.6F)
            .build());

    public static final FxEffect FROST_HAUNTED_EMERGE = register("relic/frost_haunted_emerge", FxEffect.builder()
            .layer(FxLayer.of(FxShape.DISC).radius(0.5F).count(24).velocity(FxVelocity.UP).speed(0.05F, 0.15F)
                    .particle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.PACKED_ICE.getDefaultState())))
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.4F).height(1.4F).count(14).motion(FxMotion.DRIFT).colored(ColoredParticles.SNOWFLAKE, ICE))
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 0.5F, 0.5F)
            .sound(SoundEvents.ENTITY_STRAY_AMBIENT, 0.6F, 0.7F)
            .build());

    public static final FxEffect FROST_HAUNTED_VANISH = register("relic/frost_haunted_vanish", FxEffect.builder()
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.35F).height(1.8F).count(14).velocity(FxVelocity.UP).speed(0.03F, 0.08F).particle(Particles.FROZEN_SOUL))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.4F).yOffset(1.0F).count(10).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F).colored(ColoredParticles.SNOWFLAKE, ICE))
            .sound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), 1.0F, 0.9F)
            .build());

    public static final FxEffect MINION_VANISH = register("relic/minion_vanish", FxEffect.builder()
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.35F).height(1.8F).count(14).velocity(FxVelocity.UP).speed(0.03F, 0.08F).particle(ParticleTypes.SOUL))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.4F).yOffset(1.0F).count(10).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F).particle(ParticleTypes.SOUL_FIRE_FLAME))
            .layer(FxLayer.of(FxShape.DISC).radius(0.5F).yOffset(0.1F).count(8).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.04F).colored(ColoredParticles.POOF, 0x9FD8FF))
            .sound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), 1.0F, 0.9F)
            .build());

    public static final FxEffect LOST_SOUL = register("relic/lost_soul", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.4F).count(14).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.06F).particle(ParticleTypes.SOUL))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.3F).count(8).velocity(FxVelocity.UP).speed(0.02F, 0.05F).particle(ParticleTypes.SOUL_FIRE_FLAME))
            .sound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), 0.8F, 1.0F)
            .build());

    public static final FxEffect SOUL_HIT = register("relic/soul_hit", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.3F).count(6).velocity(FxVelocity.UP).speed(0.01F, 0.03F).particle(ParticleTypes.SOUL))
            .build());

    public static final FxEffect FROST_STORM = register("relic/frost_storm", FxPresets.STORM.tweak(t -> t
            .radius(4.0F).duration(140)
            .secondaryVanillaParticle(ParticleTypes.CLOUD)
            .addSound(FxSound.of(SoundEvents.BLOCK_POWDER_SNOW_BREAK, 1.0F, 0.6F))));

    public static final FxEffect CORAL_SHIELD = register("relic/coral_shield", FxEffect.builder()
            .layer(FxLayer.of(FxShape.POINT).yOffset(1.0F).count(1).colored(ColoredParticles.FLASH, 0xFFD9F0))
            .layer(FxLayer.of(FxShape.SHELL).radius(0.9F).yOffset(1.0F).count(18).velocity(FxVelocity.OUTWARD).speed(0.01F, 0.03F).particle(MoreParticles.FLOATING_BUBBLE))
            .layer(rainbow(0xFF2626))
            .layer(rainbow(0xFF8C1A))
            .layer(rainbow(0xFFF233))
            .layer(rainbow(0x33E64D))
            .layer(rainbow(0x3399FF))
            .layer(rainbow(0x804DE6))
            .sound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 1.4F)
            .sound(SoundEvents.ITEM_ARMOR_EQUIP_GOLD.value(), 0.8F, 1.2F)
            .build());

    public static final FxEffect AMPHORA_START = register("relic/amphora_start", FxEffect.builder()
            .layer(FxLayer.of(FxShape.DISC).radius(0.6F).count(30).velocity(FxVelocity.UP).speed(0.03F, 0.08F).particle(MoreParticles.FLOATING_BUBBLE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.8F).yOffset(1.0F).count(40).velocity(FxVelocity.INWARD).speed(0.3F, 0.6F).particle(ParticleTypes.NAUTILUS))
            .layer(FxLayer.of(FxShape.RING).radius(2.5F).yOffset(0.9F).count(48).colored(ColoredParticles.END_ROD, WARD_SAFE))
            .sound(SoundEvents.ITEM_BOTTLE_FILL, 1.0F, 0.8F)
            .sound(SoundEvents.BLOCK_CONDUIT_ACTIVATE, 0.8F, 1.2F)
            .build());

    public static final FxEffect AMPHORA_WARD = register("relic/amphora_ward", FxEffect.builder().duration(100).follow()
            .layer(FxLayer.of(FxShape.RING).radius(0.45F).growFrom(5.5F).anchor(FxAnchor.ENTITY).verticalOrigin(0.5F)
                    .count(40).lifetime(2).colored(ColoredParticles.END_ROD, WARD_SAFE))
            .layer(FxLayer.of(FxShape.RING).radius(0.45F).growFrom(5.5F).anchor(FxAnchor.ENTITY).verticalOrigin(0.5F)
                    .count(0.6F).velocity(FxVelocity.UP).speed(0.02F, 0.04F).particle(MoreParticles.FLOATING_BUBBLE))
            .build());

    public static final FxEffect AMPHORA_ABSORB = register("relic/amphora_absorb", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.45F).yOffset(1.0F).count(10).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F).particle(MoreParticles.FLOATING_BUBBLE))
            .sound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1.0F, 1.0F)
            .build());

    public static final FxEffect AMPHORA_RELEASE = register("relic/amphora_release", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).yOffset(1.0F).count(45).velocity(FxVelocity.OUTWARD).speed(0.1F, 0.25F).particle(ParticleTypes.SPLASH))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).yOffset(1.0F).count(16).velocity(FxVelocity.OUTWARD).speed(0.04F, 0.08F).particle(MoreParticles.FLOATING_BUBBLE))
            .sound(SoundEvents.ENTITY_PLAYER_SPLASH, 1.0F, 0.8F)
            .build());

    public static final FxEffect SOOTHING_WATER = register("relic/soothing_water", FxEffect.builder().radius(5.0F)
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).anchor(FxAnchor.GROUND).count(36).particle(ParticleTypes.SPLASH))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(0.6F).anchor(FxAnchor.GROUND).yOffset(0.5F).count(24).particle(MoreParticles.FLOATING_BUBBLE))
            .layer(FxLayer.of(FxShape.DISC).radiusFromParams(0.4F).anchor(FxAnchor.GROUND).count(30).velocity(FxVelocity.UP).speed(0.03F, 0.08F).particle(MoreParticles.FLOATING_BUBBLE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.8F).yOffset(1.0F).count(20).velocity(FxVelocity.INWARD).speed(0.3F, 0.6F).particle(ParticleTypes.NAUTILUS))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(0.8F).anchor(FxAnchor.GROUND).yOffset(0.2F).count(16).colored(ColoredParticles.CLOUD, GUARDIAN))
            .sound(SoundEvents.ENTITY_PLAYER_SPLASH, 0.9F, 1.2F)
            .sound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 0.9F, 1.6F)
            .build());

    public static final FxEffect HEAL = register("relic/heal", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.45F).count(10).velocity(FxVelocity.UP).speed(0.01F, 0.04F).colored(ColoredParticles.WITCH, HEALING))
            .build());

    public static final FxEffect ICICLE_RING = register("relic/icicle_ring", FxEffect.builder()
            .layer(FxLayer.of(FxShape.RING).radius(1.1F).count(20).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F).particle(ParticleTypes.SNOWFLAKE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).count(8).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F).colored(ColoredParticles.CLOUD, ICE))
            .sound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 0.6F)
            .sound(MRPGLibSounds.FROST_CRACKLE.soundEvent(), 0.8F, 1.3F)
            .build());

    public static final FxEffect DRAGON_CLOUD = register("relic/dragon_cloud", FxEffect.builder().radius(5.0F).duration(120).interval(2)
            .layer(FxLayer.of(FxShape.DISC).radiusFromParams(1.0F).growFrom(0.4F).yOffset(0.3F).count(10).motion(FxMotion.FLOAT).particle(ParticleTypes.DRAGON_BREATH))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).growFrom(0.4F).yOffset(0.15F).count(14).particle(ParticleTypes.DRAGON_BREATH))
            .layer(FxLayer.of(FxShape.DISC).radiusFromParams(1.0F).growFrom(0.4F).yOffset(0.5F).count(1).motion(FxMotion.FLOAT).colored(ColoredParticles.END_ROD, DRAGON))
            .sound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 0.5F, 1.6F)
            .sound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.9F, 0.9F)
            .build());

    public static final FxEffect WITHER_CAST = register("relic/wither_cast", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.35F).count(14).velocity(FxVelocity.OUTWARD).speed(0.03F, 0.07F).particle(ParticleTypes.SOUL_FIRE_FLAME))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.3F).count(10).velocity(FxVelocity.RANDOM).speed(0.01F, 0.04F).colored(ColoredParticles.SMOKE, WITHER))
            .sound(SoundEvents.ENTITY_WITHER_SHOOT, 0.9F, 1.1F)
            .build());

    public static final FxEffect FROZEN_SOUL = register("relic/frozen_soul", FxEffect.builder().radius(3.2F)
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).anchor(FxAnchor.GROUND).yOffset(0.2F).count(24).particle(ParticleTypes.SNOWFLAKE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.7F).yOffset(0.4F).count(14).velocity(FxVelocity.OUTWARD).speed(0.03F, 0.07F).colored(ColoredParticles.CLOUD, ICE))
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 0.6F)
            .build());

    public static final FxEffect BEAM_HIT = register("relic/beam_hit", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.25F).count(3).velocity(FxVelocity.RANDOM).speed(0.05F, 0.1F).particle(ParticleTypes.ELECTRIC_SPARK))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.15F).count(1).particle(MoreParticles.FLOATING_BUBBLE))
            .build());

    public static final FxEffect MONARCH_AWAKENING_STORM = register("boss/frost_monarch/awakening_storm", FxPresets.STORM.tweak(t -> t
            .radius(3.0F).height(4.5F).turns(4.0F).duration(40).count(4.5F)
            .color(ICE, 0xFFFFFF)
            .silent()));

    public static final FxEffect MONARCH_AWAKENING_GUST = register("boss/frost_monarch/awakening_gust", FxEffect.builder().radius(3.0F).duration(40).follow()
            .layer(FxLayer.of(FxShape.CYLINDER).radiusFromParams(1.0F).height(4.0F).count(10).velocity(FxVelocity.TANGENT).speed(0.3F, 0.5F).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(0.8F).anchor(FxAnchor.GROUND).count(3).chance(0.5F).velocity(FxVelocity.TANGENT).speed(0.2F, 0.4F).particle(ParticleTypes.ITEM_SNOWBALL))
            .build());

    public static final FxEffect MONARCH_AURA = register("boss/frost_monarch/aura", FxEffect.builder().radius(3.0F).duration(10).follow()
            .layer(FxLayer.of(FxShape.SHELL).radiusFromParams(1.0F).yOffset(1.5F).count(8).velocity(FxVelocity.INWARD).speed(0.1F, 0.2F).particle(Particles.FREEZING_SNOWFLAKE))
            .build());

    public static final FxEffect MONARCH_CASTING_AURA = register("boss/frost_monarch/casting_aura", FxPresets.AURA.tweak(t -> t
            .radius(1.4F).height(3.2F).duration(10).count(1.2F)
            .color(ICE, ICE_DEEP)
            .silent()));

    public static final FxEffect MONARCH_AWAKENING_SHOCKWAVE = register("boss/frost_monarch/awakening_shockwave", FxPresets.SHOCKWAVE.tweak(t -> t
            .radius(7.0F).duration(16).count(4.0F)
            .color(ICE)
            .silent()));

    public static final FxEffect MONARCH_AWAKENING_BURST = register("boss/frost_monarch/awakening_burst", FxEffect.builder().radius(5.0F).maxParticles(800)
            .layer(FxLayer.of(FxShape.POINT).count(160).velocity(FxVelocity.RANDOM).speed(0.05F, 0.3F).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.RING).radius(0.4F).count(200).velocity(FxVelocity.OUTWARD).speed(0.8F, 1.6F).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.6F).count(120).velocity(FxVelocity.OUTWARD).speed(0.3F, 0.9F).colored(ColoredParticles.SNOWFLAKE, 0xFFFFFF))
            .layer(FxLayer.of(FxShape.SHELL).radius(1.2F).count(60).velocity(FxVelocity.OUTWARD).speed(0.15F, 0.35F).colored(ColoredParticles.CLOUD, ICE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(1.0F).count(10).colored(ColoredParticles.EXPLOSION, ICE))
            .layer(FxLayer.of(FxShape.POINT).count(1).colored(ColoredParticles.EXPLOSION_EMITTER, ICE))
            .build());

    public static final FxEffect MONARCH_DEATH_SNOWFALL = register("boss/frost_monarch/death_snowfall", FxEffect.builder().radius(2.5F).duration(10)
            .layer(FxLayer.of(FxShape.DISC).radiusFromParams(1.0F).yOffset(4.5F).count(2).velocity(FxVelocity.DOWN).speed(0.01F, 0.03F).particle(ParticleTypes.SNOWFLAKE))
            .layer(FxLayer.of(FxShape.DISC).radiusFromParams(0.8F).yOffset(4.0F).count(1).chance(0.5F).velocity(FxVelocity.DOWN).speed(0.01F, 0.02F).particle(Particles.FREEZING_SNOWFLAKE))
            .build());

    public static final FxEffect MONARCH_DEATH_BURST = register("boss/frost_monarch/death_burst", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).count(14).velocity(FxVelocity.OUTWARD).speed(0.04F, 0.1F).particle(Particles.FROZEN_SOUL))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.4F).count(16).velocity(FxVelocity.OUTWARD).speed(0.05F, 0.12F).particle(Particles.FROZEN_FLAME))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.6F).count(30).velocity(FxVelocity.OUTWARD).speed(0.08F, 0.2F).particle(Particles.FREEZING_SNOWFLAKE))
            .sound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), 1.0F, 0.7F)
            .build());

    public static final FxEffect MONARCH_BARRIER_RAISE = register("boss/frost_monarch/barrier_raise", FxPresets.PILLAR.tweak(t -> t
            .radius(0.9F).height(4.0F)
            .color(ICE, ICE_DEEP)
            .silent()));

    public static final FxEffect MONARCH_BARRIER_WALL = register("boss/frost_monarch/barrier_wall", FxEffect.builder().radius(3.5F)
            .layer(FxLayer.of(FxShape.CYLINDER).radiusFromParams(1.0F).height(3.0F).count(14).velocity(FxVelocity.UP).speed(0.01F, 0.04F).colored(ColoredParticles.SNOWFLAKE, ICE))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).anchor(FxAnchor.GROUND).count(10).colored(ColoredParticles.CLOUD, ICE_DEEP))
            .build());

    public static final FxEffect MONARCH_ICE_BURST = register("boss/frost_monarch/ice_burst", FxPresets.BURST.tweak(t -> t
            .radius(1.5F).speed(0.3F).count(2.0F)
            .color(ICE)
            .silent()));

    public static final FxEffect MONARCH_CAST = register("boss/frost_monarch/cast", FxPresets.CAST.tweak(t -> t
            .radius(1.4F).count(1.5F)
            .color(ICE, ICE_DEEP)
            .secondaryVanillaParticle(ParticleTypes.SNOWFLAKE)
            .sound(SoundEvents.ENTITY_EVOKER_CAST_SPELL, 1.5F, 0.6F)
            .addSound(FxSound.of(MRPGLibSounds.FROST_CRACKLE_LONG.soundEvent(), 1.0F, 1.0F))));

    public static final FxEffect ETERNAL_WINTER_PORTAL = register("boss/frost_monarch/eternal_winter_portal", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SHELL).radius(1.0F).anchor(FxAnchor.ENTITY).count(22).motion(FxMotion.STATIC).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.SHELL).radius(0.95F).anchor(FxAnchor.ENTITY).count(6).motion(FxMotion.STATIC).colored(ColoredParticles.SNOWFLAKE, ICE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).anchor(FxAnchor.ENTITY).count(3).motion(FxMotion.FLOAT).colored(ColoredParticles.SNOWFLAKE, ICE_DEEP))
            .build());

    public static final FxEffect ETERNAL_WINTER_PORTAL_OPEN = register("boss/frost_monarch/eternal_winter_portal_open", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SHELL).radius(1.4F).count(30).velocity(FxVelocity.INWARD).speed(0.08F, 0.16F).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.RING).radius(1.2F).yOffset(-0.9F).count(16).colored(ColoredParticles.CLOUD, ICE))
            .sound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 0.7F)
            .sound(FxSound.of(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0F, 0.6F))
            .build());

    public static final FxEffect ETERNAL_WINTER_PORTAL_CLOSE = register("boss/frost_monarch/eternal_winter_portal_close", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.9F).count(24).velocity(FxVelocity.UP).speed(0.03F, 0.08F).particle(ParticleTypes.CLOUD))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.6F).count(20).velocity(FxVelocity.OUTWARD).speed(0.1F, 0.25F).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.5F).count(10).velocity(FxVelocity.RANDOM).speed(0.02F, 0.06F).particle(ParticleTypes.FLAME))
            .sound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0F, 0.8F)
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 0.9F, 0.7F)
            .build());

    public static final FxEffect ETERNAL_WINTER_PORTAL_FADE = register("boss/frost_monarch/eternal_winter_portal_fade", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SHELL).radius(1.0F).count(20).velocity(FxVelocity.INWARD).speed(0.04F, 0.08F).particle(Particles.FREEZING_SNOWFLAKE))
            .sound(SoundEvents.BLOCK_BEACON_DEACTIVATE, 0.8F, 0.8F)
            .build());

    public static final FxEffect ETERNAL_WINTER_PORTAL_RELEASE = register("boss/frost_monarch/eternal_winter_portal_release", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.8F).count(40).velocity(FxVelocity.OUTWARD).speed(0.15F, 0.35F).particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.RING).radius(1.0F).yOffset(-0.9F).count(20).velocity(FxVelocity.OUTWARD).speed(0.05F, 0.1F).colored(ColoredParticles.CLOUD, ICE))
            .sound(SoundEvents.ENTITY_STRAY_AMBIENT, 1.0F, 0.7F)
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 0.6F, 0.5F)
            .build());

    public static final FxEffect PORTAL_MARKER = register("event/frostsiege/portal_marker", FxPresets.PILLAR.tweak(t -> t
            .radius(1.6F).height(5.0F)
            .vanillaParticle(Particles.FREEZING_SNOWFLAKE)
            .sound(SoundEvents.BLOCK_POWDER_SNOW_BREAK, 1.0F, 0.6F)));

    public static final FxEffect COLD_AURA = register("event/frostsiege/cold_aura", FxEffect.builder().radius(3.0F).duration(100).interval(4).follow()
            .layer(FxLayer.of(FxShape.CYLINDER).radiusFromParams(1.0F).height(2.5F).count(4).velocity(FxVelocity.UP).speed(0.01F, 0.04F).particle(Particles.FROZEN_SOUL))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(1.0F).anchor(FxAnchor.GROUND).count(6).colored(ColoredParticles.SNOWFLAKE, ICE))
            .sound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), 1.0F, 0.6F)
            .build());

    public static final FxEffect SEALED_MONARCH_BREAK = register("block/sealed_monarch_break", FxEffect.builder()
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.6F).yOffset(0.8F).count(40).velocity(FxVelocity.OUTWARD).speed(0.05F, 0.3F).particle(Particles.FROZEN_SOUL))
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.5F).height(1.6F).count(20).velocity(FxVelocity.UP).speed(0.02F, 0.08F).particle(Particles.FROZEN_SOUL_MOTE))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.7F).yOffset(0.8F).count(30).velocity(FxVelocity.OUTWARD).speed(0.08F, 0.2F).particle(Particles.FREEZING_SNOWFLAKE))
            .sound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), 1.5F, 0.6F)
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 0.6F)
            .build());

    public static final FxEffect ICE_WALL_SHATTER = register("boss/frost_monarch/ice_wall_shatter", FxEffect.builder()
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.7F).height(2.4F).yOffset(-1.2F).count(20).velocity(FxVelocity.OUTWARD).speed(0.05F, 0.15F)
                    .particle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.PACKED_ICE.getDefaultState())))
            .sound(FxSound.of(SoundEvents.BLOCK_GLASS_BREAK, 0.6F, 0.8F).pitchVariance(0.15F))
            .build());

    public static final FxEffect FROST_BLINK = register("mob/frozen_magician/frost_blink", FxEffect.builder()
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.5F).height(2.2F).yOffset(-1.1F).count(26).velocity(FxVelocity.OUTWARD).speed(0.04F, 0.12F)
                    .particle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.BLUE_ICE.getDefaultState())))
            .layer(FxLayer.of(FxShape.SPHERE).radius(0.8F).count(18).velocity(FxVelocity.RANDOM).speed(0.02F, 0.06F).colored(ColoredParticles.SNOWFLAKE, ICE))
            .layer(FxLayer.of(FxShape.RING).radius(0.9F).anchor(FxAnchor.GROUND).count(14).velocity(FxVelocity.OUTWARD).speed(0.03F, 0.07F).colored(ColoredParticles.CLOUD, ICE_DEEP))
            .sound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.7F, 1.5F)
            .sound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 1.3F)
            .build());

    public static final FxEffect FROST_DECOY_BURST = register("mob/frozen_magician/frost_decoy_burst", FxEffect.builder()
            .radius(3.0F)
            .layer(FxLayer.of(FxShape.SPHERE).radiusFromParams(0.1F).count(90).velocity(FxVelocity.OUTWARD).speed(0.08F, 0.2F)
                    .particle(Particles.FREEZING_SNOWFLAKE))
            .layer(FxLayer.of(FxShape.SHELL).radiusFromParams(0.35F).count(60).velocity(FxVelocity.OUTWARD).speed(0.12F, 0.3F).speedFromRadius()
                    .colored(ColoredParticles.SNOWFLAKE, ICE))
            .layer(FxLayer.of(FxShape.SPHERE).radiusFromParams(0.2F).count(40).velocity(FxVelocity.RANDOM).speed(0.02F, 0.08F)
                    .colored(ColoredParticles.SNOWFLAKE, ICE_DEEP))
            .layer(FxLayer.of(FxShape.RING).radiusFromParams(0.6F).anchor(FxAnchor.GROUND).count(40).velocity(FxVelocity.OUTWARD).speed(0.06F, 0.14F)
                    .particle(Particles.FREEZING_SNOWFLAKE))
            .sound(SoundEvents.BLOCK_GLASS_BREAK, 1.2F, 0.6F)
            .sound(FxSound.of(MRPGLibSounds.FROST_CRACKLE_LONG.soundEvent(), 1.0F, 0.8F))
            .sound(SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, 1.0F, 0.7F)
            .build());

    public static final FxEffect FROST_DECOY_SHARDS = register("mob/frozen_magician/frost_decoy_shards", FxEffect.builder()
            .layer(FxLayer.of(FxShape.COLUMN).radius(0.5F).height(2.0F).count(30).velocity(FxVelocity.OUTWARD).speed(0.15F, 0.35F)
                    .particle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.BLUE_ICE.getDefaultState())))
            .build());

    private static FxLayer rainbow(int color) {
        return FxLayer.of(FxShape.SHELL).radius(0.7F).yOffset(1.0F).count(5).velocity(FxVelocity.OUTWARD).speed(0.02F, 0.05F)
                .colored(ColoredParticles.END_ROD, color);
    }

    public static void register() {
    }

    public static void sound(ServerWorld world, Vec3d pos, SoundEvent sound, float volume, float pitch) {
        world.playSound(null, pos.x, pos.y, pos.z, sound, SoundCategory.PLAYERS, volume, pitch);
    }

    public static void play(ServerWorld world, FxEffect effect, Vec3d pos) {
        MrpgFx.play(world, effect, FxParams.at(pos));
    }
}
