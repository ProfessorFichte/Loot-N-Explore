package more_rpg_loot.entity.frozen_depths.projectile;

import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.CommonItems;
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

    public ThrownLanceEntity(EntityType<? extends ThrownLanceEntity> type, World world) {
        super(type, 0.0, 0.0, 0.0, world, new ItemStack(CommonItems.GUARDS_FROST_LANCE.item()), null);
    }

    public ThrownLanceEntity(World world, LivingEntity owner) {
        super(ModEntities.THROWN_LANCE, owner, world, new ItemStack(CommonItems.GUARDS_FROST_LANCE.item()), null);
        this.setSound(SoundEvents.ITEM_TRIDENT_THROW.value());
        this.setDamage(owner.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
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
}
