package more_rpg_loot.effects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.tag.EntityTypeTags;

import static more_rpg_loot.util.HelperMethods.stackFreezeStacks;

public class FreezingEffect extends StatusEffect {
    protected FreezingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        stackFreezeStacks(livingEntity,2);
        super.applyUpdateEffect(livingEntity, amplifier);
    }
    @Override
    public void onApplied(LivingEntity livingEntity, AttributeContainer attributes, int amplifier) {
        EntityType<?> type = livingEntity.getType();
        if(type.isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
            livingEntity.removeStatusEffect(Effects.FREEZING);
        } else{
            if(livingEntity.hasStatusEffect(Effects.FREEZING)){
                stackFreezeStacks(livingEntity,10);
            }else{
                stackFreezeStacks(livingEntity,20*(amplifier+1));
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
