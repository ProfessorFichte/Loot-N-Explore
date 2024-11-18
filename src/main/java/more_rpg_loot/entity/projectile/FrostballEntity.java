package more_rpg_loot.entity.projectile;

import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.CommonItems;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_power.api.SpellSchools;

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
        return CommonItems.FROSTBALL;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            float d = 6.0F;
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();

            if(entity instanceof LivingEntity livingEntity){
                if(entity2 != null){
                    EntityType<?> type = entity2.getType();
                    if(!type.isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)){
                        if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
                            livingEntity.addStatusEffect(new StatusEffectInstance(MRPGCEffects.FROSTED,
                                    200, 1, false, false, true));
                        }else{
                            livingEntity.setFrozenTicks(entity.getFrozenTicks() + 60);
                        }

                    }
                }
                //DAMAGE
                if(entity2 instanceof PlayerEntity playerEntity){
                    double frostPower = playerEntity.getAttributeValue(SpellSchools.FROST.attribute) * 0.25F;
                    livingEntity.damage(livingEntity.getDamageSources().freeze(), (float) (d + frostPower));
                }else{
                    livingEntity.damage(livingEntity.getDamageSources().freeze(),d);
                }
            }
            this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                    this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5),
                    0, 0, 0);
        }
    }


    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.SNOWFLAKE : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
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
            this.getWorld().sendEntityStatus(this, (byte)3);
            this.discard();
        }
    }


}
