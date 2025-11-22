package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import mod.azure.azurelib.common.util.MoveAnalysis;
import more_rpg_loot.client.entity.renderers.frosthaunt.FrosthauntDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static more_rpg_loot.util.HelperMethods.stackFreezeStacks;




public class FrosthauntEntity extends SkeletonEntity {
    public final FrosthauntDispatcher dispatcher;
    public final MoveAnalysis moveAnalysis;

    public FrosthauntEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 8.0F);
        this.dispatcher = new FrosthauntDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.experiencePoints += 1;
    }


    public static DefaultAttributeContainer.Builder createFrosthauntSkeletonAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2505)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2f);
    }

    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData);
        if(FabricLoader.getInstance().isModLoaded("thermoo")){
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0);
        return entityData2;
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean attacked = super.tryAttack(target);
        if (attacked && target instanceof LivingEntity entity) {
            moveAnalysis.update();
            stackFreezeStacks(entity, 20);

            Runnable animationRunner;
            animationRunner = dispatcher::attack;
            animationRunner.run();
            this.setAttacking(true);

        }
        return attacked;
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();

        if (this.getWorld().isClient) {
            var isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.isOnGround();
            Runnable animationRunner;
            if (isMovingOnGround) {
                animationRunner = dispatcher::walk;
            } else {
                animationRunner = dispatcher::idle;
            }
            animationRunner.run();
        }
    }
}



