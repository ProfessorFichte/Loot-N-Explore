package lne.entity.generic.projectile;

import lne.entity.ModEntities;
import com.mrpg_lib.util.AllyHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import lne.item.weapons.WeaponAbility;
import lne.item.weapons.WeaponProcs;
import lne.internal.network.WeaponCooldownPayload;
import lne.internal.platform.LNEEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class LNEAbilityArrowEntity extends ArrowEntity {
    public static final byte ENDER_DRAGON  = 0;
    public static final byte WITHER        = 1;
    public static final byte GLACIAL       = 2;
    public static final byte ELDER_GUARDIAN = 3;

    private static final Map<World, Map<UUID, long[]>> COOLDOWNS = Collections.synchronizedMap(new WeakHashMap<>());

    private byte abilityId = -1;
    private boolean freezeOnHit = false;

    public static WeaponAbility weaponAbility(byte abilityId) {
        return WeaponAbility.values()[abilityId];
    }

    public LNEAbilityArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
    }

    public LNEAbilityArrowEntity(World world, LivingEntity owner, byte abilityId) {
        super(ModEntities.LNE_ABILITY_ARROW, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.abilityId = abilityId;
    }

    public void setFreezeOnHit(boolean freezeOnHit) {
        this.freezeOnHit = freezeOnHit;
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);
        if (getWorld().isClient) return;
        Entity ownerEntity = getOwner();
        if (!(ownerEntity instanceof LivingEntity attacker)) return;
        if (!(hitResult.getEntity() instanceof LivingEntity target)) return;
        if (freezeOnHit) {
            WeaponProcs.applyFrozenWeaponHit(attacker, target);
        }
        if (abilityId < 0 || abilityId >= WeaponAbility.values().length) return;
        WeaponAbility ability = weaponAbility(abilityId);
        if (!ability.roll(WeaponAbility.Slot.RANGED, attacker.getRandom())) return;

        UUID uid = attacker.getUuid();
        long now = getWorld().getTime();
        Map<UUID, long[]> worldCooldowns = COOLDOWNS.computeIfAbsent(getWorld(), k -> new ConcurrentHashMap<>());
        worldCooldowns.values().removeIf(t -> isExpired(t, now));
        long[] times = worldCooldowns.computeIfAbsent(uid, k -> new long[WeaponAbility.values().length]);
        int cooldown = ability.cooldownTicks(WeaponAbility.Slot.RANGED);
        if (now - times[abilityId] < cooldown) return;
        times[abilityId] = now;

        WeaponProcs.fire(ability, WeaponAbility.Slot.RANGED, attacker, target);
        if (attacker instanceof ServerPlayerEntity player) {
            LNEEvents.get().sendToClient(player, new WeaponCooldownPayload(abilityId, cooldown));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("AbilityId", abilityId);
        nbt.putBoolean("FreezeOnHit", freezeOnHit);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        abilityId = nbt.contains("AbilityId") ? nbt.getByte("AbilityId") : -1;
        freezeOnHit = nbt.getBoolean("FreezeOnHit");
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && AllyHelper.canHurt(this.getOwner(), entity);
    }

    private static boolean isExpired(long[] times, long now) {
        for (int i = 0; i < times.length; i++) {
            if (now - times[i] < WeaponAbility.values()[i].cooldownTicks(WeaponAbility.Slot.RANGED)) {
                return false;
            }
        }
        return true;
    }
}
