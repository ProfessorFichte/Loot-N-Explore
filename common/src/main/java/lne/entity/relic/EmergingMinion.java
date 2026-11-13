package lne.entity.relic;

import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import com.mrpg_lib.fx.FxEffect;
import lne.fx.LNEFx;
import lne.ability.AbilityDamage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

public interface EmergingMinion {
    int EMERGE_TICKS = 30;

    Identifier OWNER_SCALING = Identifier.of("loot_n_explore", "relic_owner_scaling");

    static void scaleWithOwner(MobEntity minion, LivingEntity owner, double healthScaling, double damageScaling) {
        double highest = AbilityDamage.highest(owner);
        addOwnerScaling(minion, highest * healthScaling, highest * damageScaling);
    }

    static void addOwnerScaling(MobEntity minion, double bonusHealth, double bonusDamage) {
        addScaling(minion, EntityAttributes.GENERIC_MAX_HEALTH, bonusHealth);
        addScaling(minion, EntityAttributes.GENERIC_ATTACK_DAMAGE, bonusDamage);
        minion.setHealth(minion.getMaxHealth());
    }

    private static void addScaling(MobEntity minion, RegistryEntry<EntityAttribute> attribute, double amount) {
        var instance = minion.getAttributeInstance(attribute);
        if (instance == null || amount <= 0) return;
        instance.removeModifier(OWNER_SCALING);
        instance.addPersistentModifier(new EntityAttributeModifier(OWNER_SCALING, amount, EntityAttributeModifier.Operation.ADD_VALUE));
    }

    static void nameAfterOwner(MobEntity minion, LivingEntity owner) {
        minion.setCustomName(Text.translatable(minion.getType().getTranslationKey() + ".owned", owner.getName()));
    }

    static void vanish(MobEntity minion, FxEffect effect) {
        if (minion.getWorld() instanceof ServerWorld world) {
            LNEFx.play(world, effect, minion.getPos());
        }
        minion.discard();
    }

    static boolean isEmerging(MobEntity minion) {
        return minion.age < EMERGE_TICKS;
    }

    static float progress(MobEntity minion, float tickDelta) {
        return Math.min(1.0F, (minion.age + tickDelta) / EMERGE_TICKS);
    }

    static void tick(MobEntity minion) {
        if (!isEmerging(minion)) return;
        var world = minion.getWorld();
        BlockPos below = minion.getBlockPos().down();
        BlockState ground = world.getBlockState(below);
        if (ground.isAir()) return;
        if (!world.isClient && minion.age == 1) {
            var sound = ground.getSoundGroup().getBreakSound();
            world.playSound(null, minion.getX(), minion.getY(), minion.getZ(), sound, SoundCategory.HOSTILE, 1.0F, 0.7F);
        }
        if (world.isClient && minion.age % 2 == 0) {
            var effect = new BlockStateParticleEffect(ParticleTypes.BLOCK, ground);
            for (int i = 0; i < 4; i++) {
                world.addParticle(effect,
                        minion.getX() + (minion.getRandom().nextDouble() - 0.5) * 0.9,
                        minion.getY() + 0.05,
                        minion.getZ() + (minion.getRandom().nextDouble() - 0.5) * 0.9,
                        (minion.getRandom().nextDouble() - 0.5) * 0.2, 0.2, (minion.getRandom().nextDouble() - 0.5) * 0.2);
            }
        }
        minion.setVelocity(0, 0, 0);
    }
}
