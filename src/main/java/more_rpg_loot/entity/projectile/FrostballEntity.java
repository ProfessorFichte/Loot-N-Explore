package more_rpg_loot.entity.projectile;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.util.HelperMethods;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;
import static more_rpg_loot.util.HelperMethods.stackFreezeStacks;

public class FrostballEntity extends ThrownItemEntity implements FlyingItemEntity {
    public FrostballEntity(EntityType<? extends FrostballEntity> entityType, World world) {
        super(entityType, world);
    }
    public FrostballEntity(World world, LivingEntity owner) {
        super(ModEntities.FROSTBALL, owner, world);
    }
    public FrostballEntity(World world, double x, double y, double z) {
        super(ModEntities.FROSTBALL, x, y, z, world);
    }


    @Override
    protected Item getDefaultItem() {
        return CommonItems.FROSTBALL.item();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            float d = 2.0F;
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();

            if(entity instanceof LivingEntity livingEntity){
                if (entity == owner) {
                    return;
                }
                if (owner instanceof LivingEntity livingOwner && livingOwner.isTeammate(livingEntity)) {
                    return;
                }
                if (owner instanceof MobEntity && entity instanceof MobEntity) {
                    return;
                }

                if(owner != null){
                    EntityType<?> type = livingEntity.getType();
                    if(!type.isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)){
                        stackFreezeStacks(livingEntity,20);
                        applyStatusEffect(livingEntity,0,10,Effects.FREEZING.registryEntry,0,
                                false,true,false,0);
                        if(owner instanceof PlayerEntity playerEntity && FabricLoader.getInstance().isModLoaded("spell_power")){
                            double frostPower = playerEntity.getAttributeValue(SpellSchools.FROST.attributeEntry) * 0.25F;
                            livingEntity.damage(livingEntity.getDamageSources().magic(), (float) (d + frostPower));
                        }else{
                            livingEntity.damage(livingEntity.getDamageSources().magic(),d);
                        }
                    }
                }

            }
            this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                    this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5),
                    0, 0, 0);
        }
    }


    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getStack();
        return (ParticleEffect)(!itemStack.isEmpty() && !itemStack.isOf(this.getDefaultItem()) ? new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack) : ParticleTypes.ITEM_SNOWBALL);
    }

    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0, 0.2, 0);
            }
        }

    }


    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.getWorld().isClient) {
            this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                    this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5),
                    0, 0, 0);
        }
        this.discard();
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            Entity target = this;
            Entity owner = this.getOwner();

            if(hitResult.getType() == HitResult.Type.ENTITY){
                EntityHitResult result = (EntityHitResult) hitResult;
                target = result.getEntity();

                boolean shouldDamage = true;
                if (target == owner) {
                    shouldDamage = false;
                }
                if (owner instanceof LivingEntity livingOwner && target instanceof LivingEntity livingTarget) {
                    if (livingOwner.isTeammate(livingTarget)) {
                        shouldDamage = false;
                    }
                }
                if(owner instanceof MobEntity && target instanceof  MobEntity){
                    shouldDamage = false;
                }

                if (shouldDamage) {
                    target.damage(new DamageSource(target.getDamageSources().magic().getTypeRegistryEntry()), 4.0F);
                }
            }

            float cloudDamageAmount = 1.0F;
            if(owner instanceof LivingEntity livingOwner){
                float frostDamageOwner = 0.0F;
                float atkDamageOwner = (float) (livingOwner.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2F);
                if(FabricLoader.getInstance().isModLoaded("spell_power")){
                    frostDamageOwner = (float) (livingOwner.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.3F);
                }
                cloudDamageAmount = frostDamageOwner + atkDamageOwner;
            }

            HelperMethods.spawnCloudEntity(ParticleTypes.SNOWFLAKE,this,target,1,1.0F,2,2.0F,
                    Effects.FREEZING.registryEntry,5,0,false,0,true,cloudDamageAmount,
                    new DamageSource(target.getDamageSources().freeze().getTypeRegistryEntry()));
            this.discard();
        }
    }


}
