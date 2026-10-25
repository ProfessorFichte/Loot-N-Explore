package more_rpg_loot.entity.frozen_depths.projectile;

import more_rpg_loot.entity.ModEntities;
import com.mrpg_lib.util.AllyHelper;
import more_rpg_loot.item.CommonItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class ThrownLanceEntity extends PersistentProjectileEntity implements FlyingItemEntity {

    private double configuredDamage = 6.0;

    public ThrownLanceEntity(EntityType<? extends ThrownLanceEntity> type, World world) {
        super(type, 0.0, 0.0, 0.0, world, new ItemStack(CommonItems.GUARDS_FROST_LANCE.item()), null);
    }

    public ThrownLanceEntity(World world, LivingEntity owner) {
        super(ModEntities.THROWN_LANCE, owner, world, new ItemStack(CommonItems.GUARDS_FROST_LANCE.item()), null);
        this.setSound(SoundEvents.ITEM_TRIDENT_THROW.value());
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
