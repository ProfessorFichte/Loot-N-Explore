package more_rpg_loot.item.weapons;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.utils.TargetHelper;

import java.util.List;
import java.util.function.Predicate;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.util.HelperMethods.executeSpellSpellEngine;
import static net.spell_engine.internals.SpellRegistry.getSpell;

public class ElderGuardianMeleeWeapon extends SwordItem {
    public ElderGuardianMeleeWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            executeSpellSpellEngine(player,target,MOD_ID,"passive_waterbomb_melee", SpellCast.Action.RELEASE,true);
        }
        if(attacker.isInsideWaterOrBubbleColumn()){
            double gen_atk = attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            target.damage(new DamageSource(target.getDamageSources().magic().getTypeRegistryEntry()), (float) (gen_atk * 0.25F));
        }
        stack.damage(1, attacker, (e)->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("lore.loot_n_explore.elder_guardian_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.loot_n_explore.elder_guardian_weapon").formatted(Formatting.GOLD));
        if(FabricLoader.getInstance().isModLoaded("spell_engine")&& FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_waterbomb_melee.name").formatted(Formatting.AQUA));
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_waterbomb_melee.description").formatted(Formatting.AQUA));
        }
    }
}
