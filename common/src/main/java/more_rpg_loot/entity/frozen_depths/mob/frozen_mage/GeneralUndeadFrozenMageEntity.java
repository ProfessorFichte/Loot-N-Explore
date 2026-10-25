package more_rpg_loot.entity.frozen_depths.mob.frozen_mage;

import more_rpg_loot.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.world.World;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import more_rpg_loot.entity.frozen_depths.FrozenDepthsTuning;
import net.spell_power.api.SpellSchools;

public class GeneralUndeadFrozenMageEntity extends UndeadFrozenMageEntity {
    public GeneralUndeadFrozenMageEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.26)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_SCALE, 1.5);
        if (LNEPlatform.isModLoaded("spell_power")) {
            builder = builder.add(SpellSchools.FROST.attributeEntry, 4.5);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            builder = builder.add(ThermooAttributes.MIN_TEMPERATURE, 0.0)
                    .add(ThermooAttributes.FROST_RESISTANCE, 0.0);
        }
        return builder;
    }

    @Override
    protected EntityAttributeConfig.Entry attributeFallback() {
        EntityAttributeConfig.Entry fallback = new EntityAttributeConfig.Entry()
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 50.0)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 5.0);
        if (LNEPlatform.isModLoaded("spell_power")) {
            fallback.set(SpellSchools.FROST.attributeEntry.getIdAsString(), 4.5);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.generalUndeadFrozenMage(fallback);
    }
}
