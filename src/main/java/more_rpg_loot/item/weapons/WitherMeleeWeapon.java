package more_rpg_loot.item.weapons;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class WitherMeleeWeapon extends SwordItem {
    public WitherMeleeWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("spell_engine")){
            List<Entity> list       = new ArrayList<Entity>();
            list.add(target);

            SpellHelper.performSpell(
                    player.getWorld(),
                    player,
                    new Identifier(MOD_ID,"passive_wither_pulse"),
                    list,
                    SpellCast.Action.RELEASE,
                    1);
        }
        if (attacker instanceof PlayerEntity player){
            List<StatusEffectInstance> list = target.getStatusEffects().stream().toList();
            if (!list.isEmpty()){
                for (StatusEffectInstance statusEffectInstance : list) {
                    StatusEffect statusEffect = statusEffectInstance.getEffectType();
                    if (!statusEffect.isBeneficial()) {
                        double gen_atk = attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                        target.damage(new DamageSource(target.getDamageSources().magic().getTypeRegistryEntry()), (float) (gen_atk * 0.15F));
                    }
                }
            }
        }

        stack.damage(1, attacker, (e)->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("lore.loot_n_explore.wither_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.loot_n_explore.wither_weapon").formatted(Formatting.GOLD));
        super.appendTooltip(stack, world, tooltip, context);
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_wither_pulse.name").formatted(Formatting.BLUE));
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_wither_pulse.description").formatted(Formatting.BLUE));
        }
    }
}