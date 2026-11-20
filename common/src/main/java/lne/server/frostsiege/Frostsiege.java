package lne.server.frostsiege;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.LneMod;
import lne.blocks.FrozenDepthsBlocks;
import lne.config.FrostsiegeConfig;
import lne.config.TweaksConfig;
import lne.internal.fx.LNEFx;
import lne.internal.hud_text.EntityHudText;
import lne.internal.hud_text.EntityHudTextStyle;
import lne.internal.network.EntityHudTextPayload;
import lne.internal.platform.LNEEvents;
import lne.util.LneTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class Frostsiege {
    public static final String WARNING_KEY = Siege.NAME_KEY + ".warning";
    private static final double SIEGE_SPACING = 96.0;
    private static final int WARNING_TYPE_TICKS = 40;
    private static final int WARNING_HOLD_TICKS = 80;
    private static final int CLEAR_WEATHER_TICKS = 12000;
    private static final double RITUAL_OWNER_RANGE = 32.0;
    private static final double RITUAL_PLAYER_RANGE = 16.0;
    private static final Map<ServerWorld, List<Siege>> ACTIVE = new WeakHashMap<>();

    private Frostsiege() {
    }

    private static FrostsiegeConfig config() {
        return LneMod.frostsiegeConfig.value;
    }

    public static void tick(ServerWorld world) {
        if (world.getRegistryKey() != World.OVERWORLD) {
            return;
        }
        List<Siege> sieges = ACTIVE.get(world);
        if (sieges != null) {
            for (Iterator<Siege> iterator = sieges.iterator(); iterator.hasNext(); ) {
                Siege siege = iterator.next();
                boolean running;
                try {
                    running = siege.tick(world);
                } catch (RuntimeException e) {
                    LneMod.LOGGER.error("Eternal Frostsiege stopped because of an error", e);
                    running = false;
                }
                if (!running) {
                    iterator.remove();
                    finish(world, siege);
                }
            }
        }
        FrostsiegeConfig config = config();
        if (config.enabled && world.getTime() % Math.max(20, config.check_interval_ticks) == 0) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (canTrigger(world, player) && world.getRandom().nextFloat() < chance(world, player)) {
                    start(player);
                }
            }
        }
    }

    private static boolean canTrigger(ServerWorld world, ServerPlayerEntity player) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && player.isAlive() && !player.isCreative() && !player.isSpectator()
                && canStartAt(world, player)
                && world.getBiome(player.getBlockPos()).isIn(LneTags.FROSTSIEGE_BIOMES)
                && player.getInventory().containsAny(stack -> stack.isOf(FrozenDepthsBlocks.MONARCHS_CROWN.item()))
                && !FrostsiegeState.get(world.getServer()).onCooldown(player.getUuid(), world.getTime());
    }

    private static boolean canStartAt(ServerWorld world, ServerPlayerEntity player) {
        if (!world.isSkyVisible(BlockPos.ofFloored(player.getEyePos()))) {
            return false;
        }
        for (Siege siege : ACTIVE.getOrDefault(world, List.of())) {
            if (siege.includes(player) || siege.center().squaredDistanceTo(player.getPos()) < SIEGE_SPACING * SIEGE_SPACING) {
                return false;
            }
        }
        return true;
    }

    private static float chance(ServerWorld world, ServerPlayerEntity player) {
        FrostsiegeConfig config = config();
        float base = switch (world.getDifficulty()) {
            case HARD -> config.chance_hard;
            case NORMAL -> config.chance_normal;
            default -> config.chance_easy;
        };
        return Math.min(config.max_chance, base + FrostsiegeState.get(world.getServer()).chanceBonus(player.getUuid()));
    }

    public static void onCrownAbility(ServerPlayerEntity player) {
        FrostsiegeConfig config = config();
        if (config.enabled) {
            FrostsiegeState.get(player.getServer()).addChanceBonus(player.getUuid(), config.crown_ability_chance_bonus, config.max_chance);
        }
    }

    public static boolean start(ServerPlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld world) || world.getRegistryKey() != World.OVERWORLD
                || world.getDifficulty() == Difficulty.PEACEFUL || !canStartAt(world, player)) {
            return false;
        }
        FrostsiegeConfig config = config();
        boolean ominous = player.hasStatusEffect(StatusEffects.BAD_OMEN) || player.hasStatusEffect(StatusEffects.RAID_OMEN)
                || player.hasStatusEffect(StatusEffects.TRIAL_OMEN);
        Siege siege = new Siege(player, config, ominous);
        ACTIVE.computeIfAbsent(world, key -> new ArrayList<>()).add(siege);
        FrostsiegeState.get(world.getServer()).startCooldown(player.getUuid(), world.getTime() + config.cooldown_ticks);

        if (TweaksConfig.entityHudText()) {
            LNEEvents.get().sendToClient(player, new EntityHudTextPayload(WARNING_KEY, EntityHudTextStyle.TYPEWRITER,
                    EntityHudText.FROST_COLOR, WARNING_TYPE_TICKS, WARNING_HOLD_TICKS));
        } else {
            player.sendMessage(Text.translatable(WARNING_KEY), true);
        }
        MrpgFx.play(world, LNEFx.COLD_AURA, FxParams.on(player).duration(config.warmup_ticks));
        if (config.change_weather && !world.isRaining()) {
            world.setWeather(0, siege.estimatedTicks(), true, false);
            siege.startedSnow = true;
        }
        return true;
    }

    // Throwing Cryo Essence into a fire in a snowy biome always calls a siege and puts the fire out.
    // It skips the Crown and the cooldown, but the player still needs open sky like a normal siege.
    public static void tryCryoRitual(ItemEntity item) {
        if (!(item.getWorld() instanceof ServerWorld world) || world.getRegistryKey() != World.OVERWORLD) {
            return;
        }
        BlockPos firePos = findFire(world, item);
        if (firePos == null || !world.getBiome(firePos).isIn(LneTags.FROSTSIEGE_BIOMES)) {
            return;
        }
        ServerPlayerEntity player = ritualPlayer(world, item);
        if (player == null || !start(player)) {
            return;
        }
        BlockState fire = world.getBlockState(firePos);
        if (fire.getBlock() instanceof CampfireBlock) {
            CampfireBlock.extinguish(item, world, firePos, fire);
            world.setBlockState(firePos, fire.with(CampfireBlock.LIT, false));
        } else {
            world.removeBlock(firePos, false);
        }
        world.playSound(null, firePos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 0.7F);
        MrpgFx.play(world, LNEFx.ETERNAL_WINTER_PORTAL_FADE, FxParams.at(firePos.toCenterPos()));
        item.getStack().decrement(1);
        if (item.getStack().isEmpty()) {
            item.discard();
        }
    }

    @Nullable
    private static BlockPos findFire(ServerWorld world, ItemEntity item) {
        for (BlockPos pos : BlockPos.iterate(BlockPos.ofFloored(item.getX(), item.getY() - 0.1, item.getZ()), item.getBlockPos())) {
            BlockState state = world.getBlockState(pos);
            if (state.isIn(BlockTags.FIRE) || state.getBlock() instanceof CampfireBlock && state.get(CampfireBlock.LIT)) {
                return pos.toImmutable();
            }
        }
        return null;
    }

    @Nullable
    private static ServerPlayerEntity ritualPlayer(ServerWorld world, ItemEntity item) {
        if (item.getOwner() instanceof ServerPlayerEntity owner && owner.getWorld() == world
                && owner.squaredDistanceTo(item) <= RITUAL_OWNER_RANGE * RITUAL_OWNER_RANGE) {
            return owner;
        }
        return world.getClosestPlayer(item, RITUAL_PLAYER_RANGE) instanceof ServerPlayerEntity nearest ? nearest : null;
    }

    public static boolean stop(ServerPlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld world)) {
            return false;
        }
        List<Siege> sieges = ACTIVE.getOrDefault(world, List.of());
        for (Iterator<Siege> iterator = sieges.iterator(); iterator.hasNext(); ) {
            Siege siege = iterator.next();
            if (siege.includes(player)) {
                iterator.remove();
                finish(world, siege);
                return true;
            }
        }
        return false;
    }

    private static void finish(ServerWorld world, Siege siege) {
        siege.end(world);
        if (siege.startedSnow && ACTIVE.getOrDefault(world, List.of()).stream().noneMatch(other -> other.startedSnow)) {
            world.setWeather(CLEAR_WEATHER_TICKS, 0, false, false);
        }
    }

    public static boolean isBesieged(ServerPlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld world)) {
            return false;
        }
        for (Siege siege : ACTIVE.getOrDefault(world, List.of())) {
            if (siege.includes(player)) {
                return true;
            }
        }
        return false;
    }
}
