package more_rpg_loot.api;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class WeaponTooltips {
    public static void EnderDragonMelee(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context){
        tooltip.add(Text.translatable("lore.loot_n_explore.ender_dragon_weapon").formatted(Formatting.GOLD));
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("passive.loot_n_explore.ender_dragon_weapon").formatted(Formatting.GOLD));
            if(FabricLoader.getInstance().isModLoaded("spell_engine")){
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_dragonclaw.name").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_dragonclaw.description").formatted(Formatting.DARK_PURPLE));
            }
        }else{
            tooltip.add(Text.translatable("tooltip.loot_n_explore.shift_down"));
        }
    }
    public static void ElderGuardianMelee(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context){
        tooltip.add(Text.translatable("lore.loot_n_explore.elder_guardian_weapon").formatted(Formatting.GOLD));
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("passive.loot_n_explore.elder_guardian_weapon").formatted(Formatting.GOLD));
            if(FabricLoader.getInstance().isModLoaded("spell_engine")&& FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_waterbomb_melee.name").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_waterbomb_melee.description").formatted(Formatting.AQUA));
            }
        }else{
            tooltip.add(Text.translatable("tooltip.loot_n_explore.shift_down"));
        }
    }
    public static void WitherMelee(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context){
        tooltip.add(Text.translatable("lore.loot_n_explore.wither_weapon").formatted(Formatting.GOLD));
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("passive.loot_n_explore.wither_weapon").formatted(Formatting.GOLD));
            if(FabricLoader.getInstance().isModLoaded("spell_engine")){
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_wither_pulse.name").formatted(Formatting.BLUE));
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_wither_pulse.description").formatted(Formatting.BLUE));
            }
        }else{
            tooltip.add(Text.translatable("tooltip.loot_n_explore.shift_down"));
        }
    }
    public static void GlacialMelee(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context){
        tooltip.add(Text.translatable("lore.loot_n_explore.glacial_weapon").formatted(Formatting.GOLD));

        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("passive.loot_n_explore.glacial_weapon").formatted(Formatting.GOLD));
            if(FabricLoader.getInstance().isModLoaded("spell_engine")){
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_small_avalanche.name").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("spell.loot_n_explore.passive_small_avalanche.description").formatted(Formatting.WHITE));
            }

        }else{
            tooltip.add(Text.translatable("tooltip.loot_n_explore.shift_down"));
        }
    }
}
