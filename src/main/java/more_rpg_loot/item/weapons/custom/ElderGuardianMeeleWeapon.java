package more_rpg_loot.item.weapons.custom;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
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
import net.spell_engine.utils.TargetHelper;

import java.util.List;
import java.util.function.Predicate;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static net.spell_engine.internals.SpellRegistry.getSpell;

public class ElderGuardianMeeleWeapon extends SpellSwordItem {

    public ElderGuardianMeeleWeapon(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player){
            float range = getSpell(new Identifier(MOD_ID, "passive_waterbomb_melee")).range;
            Predicate<Entity> selectionPredicate = (target2) -> {
                return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player, target2)
                );
            };
            List<Entity> list = player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(range), selectionPredicate);

            SpellHelper.performSpell(
                    player.getWorld(),
                    player,
                    new Identifier(MOD_ID,"passive_waterbomb_melee"),
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
        tooltip.add(Text.translatable("lore.loot_n_explore.elder_guardian_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("spell.loot_n_explore.passive_waterbomb_melee.name").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("spell.loot_n_explore.passive_waterbomb_melee.description").formatted(Formatting.AQUA));
    }
}
