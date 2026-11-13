package lne.client.hud;

import com.mrpg_lib.client.particle.ColoredParticleEffect;
import com.mrpg_lib.client.particle.ColoredParticles;
import lne.item.weapons.LNE_WeaponItems;
import lne.item.weapons.WeaponAbilities;
import lne.item.weapons.WeaponAbility;
import lne.item.weapons.WeaponAbility.Slot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class WeaponReadyCue {
    private static final int PARTICLE_INTERVAL = 8;
    private static final float PARTICLE_SCALE = 0.6F;
    private static final long[] RANGED_END = new long[WeaponAbility.values().length];
    private static final int[] RANGED_DURATION = new int[WeaponAbility.values().length];

    private WeaponReadyCue() {
    }

    public static void onRangedCooldown(int ability, int ticks) {
        var world = MinecraftClient.getInstance().world;
        if (world == null || ability < 0 || ability >= RANGED_END.length) return;
        RANGED_END[ability] = world.getTime() + ticks;
        RANGED_DURATION[ability] = ticks;
    }

    public static float rangedProgress(Item item, float tickDelta) {
        if (!(item instanceof RangedWeaponItem)) return 0.0F;
        var ability = WeaponAbility.ofItem(item);
        var world = MinecraftClient.getInstance().world;
        if (ability == null || world == null) return 0.0F;
        int i = ability.ordinal();
        int duration = RANGED_DURATION[i];
        if (duration <= 0) return 0.0F;
        float left = RANGED_END[i] - (world.getTime() + tickDelta);
        if (left <= 0.0F || left > duration) {
            RANGED_DURATION[i] = 0;
            return 0.0F;
        }
        return left / duration;
    }

    public static void tick(ClientPlayerEntity player) {
        if (player.age % PARTICLE_INTERVAL != 0 || player.isInvisible() || player.isSpectator()
                || !WeaponAbilities.config.value.ready_particles || !LNE_WeaponItems.vanillaAbilitiesActive()) {
            return;
        }
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            var ability = WeaponAbility.ofItem(stack.getItem());
            if (ability != null && isReady(player, stack.getItem(), ability)) {
                spawnParticle(player, hand, ability);
            }
        }
    }

    private static boolean isReady(ClientPlayerEntity player, Item item, WeaponAbility ability) {
        Slot slot = item instanceof RangedWeaponItem ? Slot.RANGED : item instanceof ShieldItem ? Slot.BLOCK : Slot.MELEE;
        if (!ability.enabled(slot)) return false;
        return slot == Slot.RANGED ? rangedProgress(item, 0.0F) <= 0.0F : !player.getItemCooldownManager().isCoolingDown(item);
    }

    private static void spawnParticle(ClientPlayerEntity player, Hand hand, WeaponAbility ability) {
        Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
        double side = arm == Arm.RIGHT ? 1.0 : -1.0;
        var random = player.getRandom();
        Vec3d pos;
        if (MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
            Vec3d look = player.getRotationVec(1.0F);
            Vec3d right = look.crossProduct(new Vec3d(0, 1, 0)).normalize();
            pos = player.getEyePos().add(look.multiply(0.7)).add(right.multiply(0.4 * side)).add(0, -0.35, 0);
        } else {
            float yaw = player.bodyYaw * MathHelper.RADIANS_PER_DEGREE;
            Vec3d forward = new Vec3d(-MathHelper.sin(yaw), 0, MathHelper.cos(yaw));
            Vec3d right = new Vec3d(-MathHelper.cos(yaw), 0, -MathHelper.sin(yaw));
            pos = player.getPos().add(forward.multiply(0.3)).add(right.multiply(0.4 * side)).add(0, player.getHeight() * 0.45, 0);
        }
        pos = pos.add((random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2);
        player.getWorld().addParticle(new ColoredParticleEffect(ColoredParticles.END_ROD, ability.theme().color(), PARTICLE_SCALE),
                pos.x, pos.y, pos.z, 0.0, 0.01, 0.0);
    }
}
