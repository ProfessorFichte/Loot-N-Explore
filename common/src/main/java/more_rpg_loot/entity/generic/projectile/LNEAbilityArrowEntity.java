package more_rpg_loot.entity.generic.projectile;

import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.frozen_depths.projectile.FrostballEntity;
import more_rpg_loot.util.HelperMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class LNEAbilityArrowEntity extends ArrowEntity {
    public static final byte ENDER_DRAGON  = 0;
    public static final byte WITHER        = 1;
    public static final byte GLACIAL       = 2;
    public static final byte ELDER_GUARDIAN = 3;

    private static final int[] COOLDOWN_TICKS = {300, 240, 360, 200};

    // Per-shooter cooldown: UUID -> long[4] (last trigger world-time per ability)
    private static final HashMap<UUID, long[]> cooldowns = new HashMap<>();

    private byte abilityId = -1;

    // Required by EntityType.EntityFactory
    public LNEAbilityArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
    }

    // Used when firing from a bow/crossbow
    public LNEAbilityArrowEntity(World world, LivingEntity owner, byte abilityId) {
        super(ModEntities.LNE_ABILITY_ARROW, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.abilityId = abilityId;
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);
        if (getWorld().isClient || abilityId < 0) return;
        Entity ownerEntity = getOwner();
        if (!(ownerEntity instanceof LivingEntity attacker)) return;
        if (!(hitResult.getEntity() instanceof LivingEntity target)) return;
        if (attacker.getRandom().nextFloat() >= 0.20f) return;

        UUID uid = attacker.getUuid();
        long now = getWorld().getTime();
        long[] times = cooldowns.computeIfAbsent(uid, k -> new long[4]);
        if (now - times[abilityId] < COOLDOWN_TICKS[abilityId]) return;
        times[abilityId] = now;

        float atkDmg = (float) attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        switch (abilityId) {
            case ENDER_DRAGON -> {
                float dmg = atkDmg * 0.15f;
                target.damage(target.getDamageSources().magic(), dmg);
                if (attacker instanceof PlayerEntity p) p.heal(dmg);
            }
            case WITHER -> {
                HelperMethods.spawnCloudEntity(
                        ParticleTypes.SMOKE, attacker, target, 1,
                        6.0f, 5, 6.0f,
                        StatusEffects.WITHER, 5, 1,
                        false, 0, true, atkDmg * 0.15f,
                        target.getDamageSources().magic());
            }
            case GLACIAL -> {
                double bx = target.getX(), by = target.getY() + 6.0, bz = target.getZ();
                double r = 1.5;
                for (int i = 0; i < 5; i++) {
                    double angle = (2 * Math.PI / 5) * i;
                    FrostballEntity ball = new FrostballEntity(getWorld(),
                            bx + r * Math.cos(angle), by, bz + r * Math.sin(angle));
                    ball.setVelocity(0, -1, 0);
                    getWorld().spawnEntity(ball);
                }
            }
            case ELDER_GUARDIAN -> {
                HelperMethods.spawnCloudEntity(
                        ParticleTypes.RAIN, attacker, target, 1,
                        5.0f, 5, 5.0f,
                        null, 0, 0, false, 0,
                        true, 5.0f, target.getDamageSources().magic());
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("AbilityId", abilityId);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        abilityId = nbt.getByte("AbilityId");
    }
}
