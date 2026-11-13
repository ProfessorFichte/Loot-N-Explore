package lne.entity.frozen_depths.projectile;

import lne.platform.LNEPlatform;

import lne.effects.Effects;
import lne.entity.ModEntities;
import com.mrpg_lib.util.AllyHelper;
import com.mrpg_lib.sounds.MRPGLibSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import com.mrpg_lib.effect.MRPGCEffects;

import static com.mrpg_lib.util.FreezeMethods.addFrozenTicksCapped;

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

        addFrozenTicksCapped(target, 20);
        getWorld().playSound(null, target.getX(), target.getBodyY(0.5), target.getZ(), MRPGLibSounds.FROST_CRACKLE.soundEvent(),
                this.getSoundCategory(), 0.4F, 1.0F);

        if (LNEPlatform.isModLoaded("mrpg_lib")) {
            target.addStatusEffect(new StatusEffectInstance(MRPGCEffects.FROSTED.entry, 60, 0, false, true, true));
        } else {
            target.addStatusEffect(new StatusEffectInstance(Effects.FREEZING.registryEntry, 60, 0, false, true, true));
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && AllyHelper.canHurt(this.getOwner(), entity);
    }
}
