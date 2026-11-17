package lne.item;

import lne.internal.ability.AbilityTheme;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerType;
import lne.entity.frozen_depths.projectile.TrackingIcicleEntity;
import lne.item.relics.RelicTooltips;
import lne.config.TweaksConfig;
import lne.internal.platform.LNEPlatform;
import lne.item.weapons.WeaponAbilities;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.internal.fx.LNEFx;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class StaffItem extends Item {

    public StaffItem(Settings settings) {
        super(settings);
    }

    public static boolean hasSpellEngineSpell() {
        return TweaksConfig.weaponAbilities() && TweaksConfig.spellEngineAbilities() && LNEPlatform.isModLoaded("wizards");
    }

    private static boolean abilityActive() {
        return TweaksConfig.weaponAbilities() && !hasSpellEngineSpell() && WeaponAbilities.config.value.monarchs_frost_staff.enabled;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (hand != Hand.MAIN_HAND || !abilityActive() || user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.pass(stack);
        }
        if (world instanceof ServerWorld sw) {
            var config = WeaponAbilities.config.value.monarchs_frost_staff;
            Vec3d look = user.getRotationVec(1.0F);
            Vec3d tip = new Vec3d(user.getX(), user.getEyeY() - 0.15, user.getZ()).add(look.multiply(0.9));
            TrackingIcicleEntity icicle = new TrackingIcicleEntity(world, user, look, tip.x, tip.y, tip.z, TrackingIcicleEntity.Shot.STAFF);
            icicle.configureStaffShot((float) config.damage_multiplier, Math.max(1, config.freezing_ticks));
            world.spawnEntity(icicle);
            MrpgFx.play(sw, LNEFx.STAFF_CAST, FxParams.at(tip).direction(look));
            user.getItemCooldownManager().set(this, Math.max(1, config.cooldown_ticks));
        }
        user.swingHand(hand);
        return TypedActionResult.success(stack, world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (abilityActive()) {
            var config = WeaponAbilities.config.value.monarchs_frost_staff;
            RelicTooltips.appendAbilityBlock(tooltip, true, List.of(TriggerType.ACTIVE_USE),
                    AbilityTheme.GLACIAL.style(Text.translatable(getTranslationKey() + ".ability.name")),
                    Text.translatable(getTranslationKey() + ".ability", RelicText.seconds(config.freezing_ticks)),
                    List.of(), config.cooldown_ticks);
        }
    }
}
