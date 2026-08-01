package more_rpg_loot.blocks.frozen_depths;

import more_rpg_loot.entity.frozen_depths.projectile.FrostballLocatorEntity;
import more_rpg_loot.worldgen.structures.StructureTags;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class MonarchsCrownItem extends BlockItem {
    private static final int LOCATE_COOLDOWN_TICKS = 600;
    private static final int SEARCH_RADIUS_CHUNKS = 100;

    public MonarchsCrownItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.isSneaking() || user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.pass(stack);
        }

        if (world instanceof ServerWorld serverWorld) {
            BlockPos targetPos = serverWorld.locateStructure(StructureTags.FROSTMONARCH_TEMPLE_LOCATED, user.getBlockPos(), SEARCH_RADIUS_CHUNKS, false);
            user.getItemCooldownManager().set(this, LOCATE_COOLDOWN_TICKS);

            if (targetPos != null) {
                FrostballLocatorEntity locator = new FrostballLocatorEntity(world, user.getX(), user.getBodyY(0.5), user.getZ());
                locator.initTargetPos(targetPos);
                world.spawnEntity(locator);
                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking()) {
            return this.use(context.getWorld(), player, context.getHand()).getResult();
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".lore").formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".ability_hint").formatted(Formatting.DARK_AQUA));
    }
}
