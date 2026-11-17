package lne.item.weapons.frozen_depths;

import lne.internal.ability.AbilityTheme;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerType;
import lne.entity.generic.projectile.LNEAbilityArrowEntity;
import lne.item.relics.RelicTooltips;
import lne.item.weapons.LNE_WeaponItems;
import lne.item.weapons.WeaponAbilities;
import lne.item.weapons.WeaponProcs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

import static lne.LneMod.MOD_ID;

public final class FreezingWeapons {
    public static final String LANG_KEY = "item." + MOD_ID + ".freezing_weapon.ability";

    private FreezingWeapons() {
    }

    private static void appendTooltip(TriggerType trigger, List<Text> tooltip) {
        var config = WeaponAbilities.config.value.frozen_weapons;
        if (!config.enabled || !LNE_WeaponItems.vanillaAbilitiesActive()) return;
        var description = Text.translatable(LANG_KEY, RelicText.seconds(config.freezing_ticks));
        RelicTooltips.appendAbilityBlock(tooltip, false, List.of(trigger), AbilityTheme.GLACIAL.style(Text.translatable(LANG_KEY + ".name")), description, List.of(), 0);
    }

    public static class Axe extends AxeItem {
        public Axe(ToolMaterial material, Settings settings) {
            super(material, settings);
        }

        @Override
        public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            WeaponProcs.applyFrozenWeaponHit(attacker, target);
            return super.postHit(stack, target, attacker);
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            super.appendTooltip(stack, context, tooltip, type);
            FreezingWeapons.appendTooltip(TriggerType.MELEE_HIT, tooltip);
        }
    }

    public static class Sword extends SwordItem {
        public Sword(ToolMaterial material, Settings settings) {
            super(material, settings);
        }

        @Override
        public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            WeaponProcs.applyFrozenWeaponHit(attacker, target);
            return super.postHit(stack, target, attacker);
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            super.appendTooltip(stack, context, tooltip, type);
            FreezingWeapons.appendTooltip(TriggerType.MELEE_HIT, tooltip);
        }
    }

    public static class Bow extends BowItem {
        public Bow(Settings settings) {
            super(settings);
        }

        @Override
        protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
            ProjectileEntity vanilla = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
            if (!LNE_WeaponItems.vanillaAbilitiesActive() || !WeaponAbilities.config.value.frozen_weapons.enabled) {
                return vanilla;
            }
            ProjectileEntity converted = LNE_WeaponItems.toAbilityArrow(world, shooter, (byte) -1, vanilla);
            if (converted instanceof LNEAbilityArrowEntity arrow) {
                arrow.setFreezeOnHit(true);
            }
            return converted;
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            super.appendTooltip(stack, context, tooltip, type);
            FreezingWeapons.appendTooltip(TriggerType.ARROW_HIT, tooltip);
        }
    }
}
