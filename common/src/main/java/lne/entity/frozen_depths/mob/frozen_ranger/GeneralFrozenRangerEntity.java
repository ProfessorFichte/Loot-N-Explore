package lne.entity.frozen_depths.mob.frozen_ranger;

import lne.compat.items.RWA_Effects;
import lne.internal.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.world.World;

public class GeneralFrozenRangerEntity extends FrozenRangerEntity {
    public GeneralFrozenRangerEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected float voicePitch() {
        return 0.75F;
    }

    @Override
    protected float getSoundVolume() {
        return 1.3F;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_SCALE, 1.5);
        if (LNEPlatform.isModLoaded("ranged_weapon_api")) {
            builder = RWA_Effects.addDamageAttribute(builder);
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
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 100.0, 1.25, 1.5)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 4.0);
        if (LNEPlatform.isModLoaded("ranged_weapon_api")) {
            fallback.set(RWA_Effects.damageAttributeId(), 1.5);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.generalFrozenRanger(fallback);
    }
}
