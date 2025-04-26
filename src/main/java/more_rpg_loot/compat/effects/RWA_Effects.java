package more_rpg_loot.compat.effects;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.effects.CustomStatusEffect;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class RWA_Effects {
    private static final ArrayList<Entry> entries = new ArrayList<Entry>();
    public static class Entry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
            this.id = Identifier.of(WitcherClassMod.MOD_ID, name);
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return Identifier.of(MOD_ID, "effect." + id.getPath());
        }
    }

    public static final Entry APPLE_JUICE =  new Entry("apple_juice",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static final Entry WALDMEISTER =  new Entry("waldmeister",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static final Entry FORREST_SPIRIT =  new Entry("forrest_spirit",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Ranged Weapon API Compat Effects for " + MOD_ID);
        APPLE_JUICE.effect.addAttributeModifier(
                        EntityAttributes_RangedWeapon.DAMAGE.entry, APPLE_JUICE.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        WALDMEISTER.effect.addAttributeModifier(
                EntityAttributes_RangedWeapon.DAMAGE.entry, WALDMEISTER.modifierId(),
                0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.entry, WALDMEISTER.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        ;

        FORREST_SPIRIT.effect.addAttributeModifier(
                EntityAttributes_RangedWeapon.DAMAGE.entry, FORREST_SPIRIT.modifierId(),
                0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.entry, WALDMEISTER.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED, WALDMEISTER.modifierId(),
                        0.2F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                ;


        for (Entry entry: entries) {
            entry.register();
        }
    }
}
