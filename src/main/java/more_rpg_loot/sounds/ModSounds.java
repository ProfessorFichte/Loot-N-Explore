package more_rpg_loot.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModSounds {
    public static final Identifier ENTITY_GLAZE_AMBIENT_ID = new Identifier(MOD_ID, "entity.glaze.ambient");
    public static SoundEvent ENTITY_GLAZE_AMBIENT_EVENT = SoundEvent.of(ENTITY_GLAZE_AMBIENT_ID);
    public static final Identifier ENTITY_GLAZE_HURT_ID = new Identifier(MOD_ID, "entity.glaze.hurt");
    public static SoundEvent ENTITY_GLAZE_HURT_EVENT = SoundEvent.of(ENTITY_GLAZE_HURT_ID);
    public static final Identifier ENTITY_GLAZE_DEATH_ID = new Identifier(MOD_ID, "entity.glaze.death");
    public static SoundEvent ENTITY_GLAZE_DEATH_EVENT = SoundEvent.of(ENTITY_GLAZE_DEATH_ID);
    public static final Identifier ENTITY_GLAZE_FREEZE_ID = new Identifier(MOD_ID, "entity.glaze.freeze");
    public static SoundEvent ENTITY_GLAZE_FREEZE_EVENT = SoundEvent.of(ENTITY_GLAZE_FREEZE_ID);
    public static final Identifier VILLAGER_INNKEEPER_ID = new Identifier(MOD_ID, "villager.innkeeper");
    public static SoundEvent VILLAGER_INNKEEPER_EVENT = SoundEvent.of(VILLAGER_INNKEEPER_ID);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, ENTITY_GLAZE_AMBIENT_ID, ENTITY_GLAZE_AMBIENT_EVENT);
        Registry.register(Registries.SOUND_EVENT, ENTITY_GLAZE_HURT_ID, ENTITY_GLAZE_HURT_EVENT);
        Registry.register(Registries.SOUND_EVENT, ENTITY_GLAZE_DEATH_ID, ENTITY_GLAZE_DEATH_EVENT);
        Registry.register(Registries.SOUND_EVENT, ENTITY_GLAZE_FREEZE_ID, ENTITY_GLAZE_FREEZE_EVENT);
        Registry.register(Registries.SOUND_EVENT, VILLAGER_INNKEEPER_ID, VILLAGER_INNKEEPER_EVENT);

    }
}
