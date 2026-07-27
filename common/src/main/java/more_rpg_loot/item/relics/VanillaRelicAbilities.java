package more_rpg_loot.item.relics;

import more_rpg_loot.compat.spell_engine.ISpellRelicEnhancer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VanillaRelicAbilities implements ISpellRelicEnhancer {

    @Override
    public Item.Settings enhance(Item.Settings settings, @Nullable Identifier spellId) {
        return settings;
    }

    public static TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        var stack = player.getStackInHand(hand);
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            return TypedActionResult.pass(stack);
        }
        if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            return TypedActionResult.pass(stack);
        }
        var id = Registries.ITEM.getId(stack.getItem());
        int cooldown = switch (id.getPath()) {
            case "corrupted_ender_pearl" -> {
                blinkTeleport(world, player);
                yield 100;
            }
            case "withered_obsidian_shard" -> {
                witherTouch(world, player);
                yield 200;
            }
            case "frozen_rib" -> {
                frozenTouch(world, player);
                yield 200;
            }
            case "poseidons_amphora" -> {
                poseidonsGrace(world, player);
                yield 400;
            }
            default -> 0;
        };
        if (cooldown <= 0) {
            return TypedActionResult.pass(stack);
        }
        if (!world.isClient) {
            player.getItemCooldownManager().set(stack.getItem(), cooldown);
        }
        return TypedActionResult.success(stack, world.isClient);
    }

    private static void blinkTeleport(World world, PlayerEntity player) {
        if (world.isClient) return;
        var start = player.getEyePos();
        var end = start.add(player.getRotationVector().multiply(8));
        var hit = world.raycast(new RaycastContext(start, end,
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        var target = hit.getType() == HitResult.Type.MISS ? end : hit.getPos();
        player.requestTeleport(target.x, target.y, target.z);
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    private static void witherTouch(World world, PlayerEntity player) {
        if (world.isClient) return;
        for (var target : nearbyEnemies(world, player, 4.0)) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 0.5F, 1.4F);
    }

    private static void frozenTouch(World world, PlayerEntity player) {
        if (world.isClient) return;
        for (var target : nearbyEnemies(world, player, 4.0)) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 1));
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.6F, 0.6F);
    }

    private static void poseidonsGrace(World world, PlayerEntity player) {
        if (world.isClient) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 1200, 0));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 1200, 0));
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1.0F, 1.2F);
    }

    private static List<LivingEntity> nearbyEnemies(World world, PlayerEntity player, double radius) {
        return world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(radius),
                e -> e != player && e.isAlive() && e.canTakeDamage());
    }
}
