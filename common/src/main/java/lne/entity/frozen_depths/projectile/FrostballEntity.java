package lne.entity.frozen_depths.projectile;

import com.mrpg_lib.sounds.MRPGLibSounds;
import com.mrpg_lib.util.AllyHelper;
import lne.util.damage.FrostDamageProfiles;
import com.mrpg_lib.util.damage.SpellDamage;

import lne.effects.Effects;
import lne.entity.ModEntities;
import lne.item.CommonItems;
import com.mrpg_lib.util.CloudMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import static lne.util.HelperMethods.applyStatusEffect;
import static com.mrpg_lib.util.FreezeMethods.addFrozenTicksCapped;

public class FrostballEntity extends ThrownItemEntity implements FlyingItemEntity {
    private boolean hail;
    private float impactVolume = 1.0F;

    public FrostballEntity(EntityType<? extends FrostballEntity> entityType, World world) {
        super(entityType, world);
    }
    public FrostballEntity(World world, LivingEntity owner) {
        super(ModEntities.FROSTBALL, owner, world);
    }
    public FrostballEntity(World world, double x, double y, double z) {
        super(ModEntities.FROSTBALL, x, y, z, world);
    }


    public FrostballEntity asHail() {
        this.hail = true;
        this.impactVolume = 0.35F;
        return this;
    }

    public FrostballEntity withImpactVolume(float volume) {
        this.impactVolume = volume;
        return this;
    }

    private void playImpactSound() {
        if (this.hail) {
            this.playSound(MRPGLibSounds.ICICLE_HIT.soundEvent(), this.impactVolume, 1.1F + this.random.nextFloat() * 0.4F);
            return;
        }
        this.playSound(MRPGLibSounds.ICICLE_HIT.soundEvent(), this.impactVolume, 1.1F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.playSound(SoundEvents.BLOCK_POWDER_SNOW_BREAK, this.impactVolume * 0.6F, 1.0F);
    }

    @Override
    protected Item getDefaultItem() {
        return CommonItems.FROSTBALL.item();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            this.playImpactSound();
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();

            if (entity instanceof LivingEntity livingEntity && canAffect(owner, livingEntity)
                    && !livingEntity.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                addFrozenTicksCapped(livingEntity, 20);
                applyStatusEffect(livingEntity, 0, 10, Effects.FREEZING.registryEntry, 0,
                        false, true, false, 0);
                LivingEntity damageOwner = owner instanceof LivingEntity livingOwner ? livingOwner : null;
                livingEntity.damage(livingEntity.getDamageSources().magic(),
                        SpellDamage.calculate(damageOwner, FrostDamageProfiles.FROSTBALL_IMPACT));
            }
            this.spawnBurst(serverWorld);
        }
    }

    private static boolean canAffect(Entity owner, LivingEntity target) {
        if (AllyHelper.resolveOwner(owner) instanceof LivingEntity) {
            return AllyHelper.canHurt(owner, target);
        }
        return !(target instanceof Monster) && AllyHelper.canHurt(null, target);
    }

    private void spawnBurst(ServerWorld serverWorld) {
        serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getBodyY(0.5), this.getZ(),
                8, 0.2, 0.2, 0.2, 0.02);
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
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            this.playImpactSound();
            this.spawnBurst(serverWorld);
        }
        this.discard();
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            Entity target = this;
            Entity owner = this.getOwner();

            if (hitResult.getType() == HitResult.Type.ENTITY) {
                target = ((EntityHitResult) hitResult).getEntity();
            }

            float cloudDamageAmount = SpellDamage.calculate(owner instanceof LivingEntity cloudOwner ? cloudOwner : null,
                    FrostDamageProfiles.FROSTBALL_CLOUD);

            CloudMethods.spawnCustomCloud(ParticleTypes.SNOWFLAKE,this,target,1,1.0F,2,2.0F,
                    Effects.FREEZING.registryEntry,5,0,false,0,true,cloudDamageAmount,
                    new DamageSource(target.getDamageSources().freeze().getTypeRegistryEntry()));
            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.discard();
        }
    }

}
