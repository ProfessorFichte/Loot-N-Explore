package more_rpg_loot.compat.spell_engine.trinket_compat;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_engine.api.item.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class LNETrinketItem extends TrinketItem {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public LNETrinketItem(Settings settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        var itemPath = Registries.ITEM.getId(stack.getItem()).getPath();
        var modifierUuid = UUID.nameUUIDFromBytes((uuid + ":" + itemPath).getBytes(StandardCharsets.UTF_8));
        var modifierName = "loot_n_explore:relic/" + itemPath;
        for (var entry : this.customAttributes.modifiers()) {
            var attribute = entry.attributeValue();
            if (attribute == null) {
                continue;
            }
            modifiers.put(attribute,
                    new EntityAttributeModifier(modifierUuid, modifierName,
                            entry.modifier().getValue(), entry.modifier().getOperation()));
        }
        return modifiers;
    }

    public void setConfigurableModifiers(ItemAttributeModifiers component) {
        this.customAttributes = component;
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var isOnCooldown = false;
        if (entity instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
        }
        return super.canUnequip(stack, slot, entity) && !isOnCooldown;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        String loreKey = this.getTranslationKey() + ".lore";
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable(loreKey).formatted(Formatting.GOLD, Formatting.ITALIC));
    }
}
