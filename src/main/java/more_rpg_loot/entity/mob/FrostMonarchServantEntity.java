package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostMonarchServantEntity extends FrosthauntEntity{
    public FrostMonarchServantEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void mobTick() {
        List<FrostMonarchEntity> list = this.getWorld().getNonSpectatingEntities(FrostMonarchEntity.class, this.getBoundingBox().expand(50.0));
        if(list.isEmpty()){
            this.kill();
        }
    }
    public static DefaultAttributeContainer.Builder createMonarchServantAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2505)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2f);
    }
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        if(FabricLoader.getInstance().isModLoaded("thermoo")){
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0);
        this.updateAttackType();
        return entityData2;
    }

}
