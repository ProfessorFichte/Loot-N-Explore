package more_rpg_loot.entity.frozen_depths.projectile;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.more_rpg_classes.effect.MRPGCEffects;

import static more_rpg_loot.util.HelperMethods.stackFreezeStacks;

public class FrozenArrowEntity extends ArrowEntity {

    public FrozenArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
    }

    public FrozenArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.FROZEN_ARROW, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);
        if (getWorld().isClient) return;
        if (!(hitResult.getEntity() instanceof LivingEntity target)) return;

        stackFreezeStacks(target, 20);

        if (LNEPlatform.isModLoaded("more_rpg_classes")) {
            target.addStatusEffect(new StatusEffectInstance(MRPGCEffects.FROSTED.entry, 60, 0, false, true, true));
        } else {
            target.addStatusEffect(new StatusEffectInstance(Effects.FREEZING.registryEntry, 60, 0, false, true, true));
        }
    }
}
