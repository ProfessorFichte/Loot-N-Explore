package more_rpg_loot.item.weapons.custom;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class DragonMeeleeWeapon extends SpellSwordItem {
    public DragonMeeleeWeapon(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("spell_engine")){
            List<Entity> list       = new ArrayList<Entity>();
            list.add(target);

            SpellHelper.performSpell(
                    player.getWorld(),
                    player,
                    new Identifier(MOD_ID,"passive_dragonclaw"),
                    list,
                    SpellCast.Action.RELEASE,
                    1);
        }
        stack.damage(1, attacker, (e)->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_dragonclaw.name").formatted(Formatting.DARK_PURPLE));
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_dragonclaw.description").formatted(Formatting.DARK_PURPLE));
        }
    }
}
