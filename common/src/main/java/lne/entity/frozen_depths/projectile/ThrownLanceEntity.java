package lne.entity.frozen_depths.projectile;

import lne.entity.ModEntities;
import com.mrpg_lib.util.AllyHelper;
import lne.item.CommonItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class ThrownLanceEntity extends PersistentProjectileEntity implements FlyingItemEntity {

    private double configuredDamage = 6.0;

    public ThrownLanceEntity(EntityType<? extends ThrownLanceEntity> type, World world) {
        super(type, 0.0, 0.0, 0.0, world, new ItemStack(CommonItems.GUARDS_FROST_LANCE.item()), null);
    }

    public ThrownLanceEntity(World world, LivingEntity owner) {
        super(ModEntities.THROWN_LANCE, owner, world, new ItemStack(CommonItems.GUARDS_FROST_LANCE.item()), null);
        this.configuredDamage = owner.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        this.setDamage(this.configuredDamage);
    }

    @Override
    public ItemStack getStack() {
        return this.getItemStack();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(CommonItems.GUARDS_FROST_LANCE.item());
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        this.setSound(SoundEvents.ITEM_TRIDENT_HIT);
        super.onEntityHit(entityHitResult);
        this.setSound(this.getHitSound());
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed, divergence);
        double launchSpeed = this.getVelocity().length();
        if (launchSpeed > 0.1) {
            this.setDamage(this.configuredDamage / launchSpeed);
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && AllyHelper.canHurt(this.getOwner(), entity);
    }
}
