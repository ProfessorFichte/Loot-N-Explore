package more_rpg_loot.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_engine.internals.casting.SpellCast;

import java.util.List;
import java.util.UUID;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.util.HelperMethods.executeSpellSpellEngine;

public class ElderGuardianMeleeWeapon extends SwordItem implements ConfigurableAttributes {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;
    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        this.attributes = attributes;
    }
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (this.attributes == null) {
            return super.getAttributeModifiers(slot);
        } else {
            return slot == EquipmentSlot.MAINHAND ? this.attributes : super.getAttributeModifiers(slot);
        }
    }
    private final float attackDamage;
    public ElderGuardianMeleeWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.attackDamage = (float) attackDamage + toolMaterial.getAttackDamage();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID,
                "Weapon modifier", (double) this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID,
                "Weapon modifier", (double) attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            builder.put(MoreSpellSchools.WATER.attribute, new EntityAttributeModifier(UUID.fromString("c14a4b73-81ff-4c3f-8d09-a4752c830fe2"),
                    "Spell Power Modifier", (double) 3.0, EntityAttributeModifier.Operation.ADDITION));
        }
        this.attributes = builder.build();
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
