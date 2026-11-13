package lne.entity.relic;

import lne.ability.impl.CorruptedPearlAbility;
import lne.entity.ModEntities;
import lne.item.relics.LNE_RelicItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class CorruptedPearlEntity extends ThrownItemEntity {
    @Nullable
    private BiConsumer<ServerPlayerEntity, Vec3d> landing;

    public CorruptedPearlEntity(EntityType<? extends CorruptedPearlEntity> type, World world) {
        super(type, world);
    }

    public CorruptedPearlEntity(World world, LivingEntity owner) {
        super(ModEntities.CORRUPTED_PEARL, owner, world);
    }

    public void onLanding(BiConsumer<ServerPlayerEntity, Vec3d> landing) {
        this.landing = landing;
    }

    @Override
    protected Item getDefaultItem() {
        return LNE_RelicItems.CORRUPTED_ENDER_PEARL.item().get();
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld() instanceof ServerWorld world && age % 2 == 0) {
            world.spawnParticles(ParticleTypes.PORTAL, getX(), getY(), getZ(), 3, 0.1, 0.1, 0.1, 0.2);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (getWorld() instanceof ServerWorld world && getOwner() instanceof ServerPlayerEntity owner) {
            var destination = getPos();
            if (owner.networkHandler.isConnectionOpen() && owner.getServerWorld() == world && !owner.isSleeping()) {
                owner.stopRiding();
                owner.teleport(world, destination.x, destination.y, destination.z, owner.getYaw(), owner.getPitch());
                owner.fallDistance = 0;
            }
            if (landing != null) {
                landing.accept(owner, destination);
            } else {
                CorruptedPearlAbility.explode(world, owner, destination);
            }
        }
        if (!getWorld().isClient) {
            discard();
        }
    }
}
