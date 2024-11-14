package more_rpg_loot.item.weapons.custom;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabric_extras.ranged_weapon.api.CustomBow;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class ElderGuardianRangedWeapon extends CustomBow{
    public ElderGuardianRangedWeapon(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("lore.loot_n_explore.elder_guardian_weapon").formatted(Formatting.GOLD));
    }
}
