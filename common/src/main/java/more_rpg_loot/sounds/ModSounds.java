package more_rpg_loot.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModSounds {
    public static final class Entry {
        private final Identifier id;
        private final SoundEvent soundEvent;
        private RegistryEntry<SoundEvent> entry;

        public Entry(Identifier id, SoundEvent soundEvent) {
            this.id = id;
            this.soundEvent = soundEvent;
        }

        public Entry(String name) {
            this(Identifier.of(MOD_ID, name));
        }

        public Entry(Identifier id) {
            this(id, SoundEvent.of(id));
        }

        public Identifier id() {
            return id;
        }

        public SoundEvent soundEvent() {
            return soundEvent;
        }

        public RegistryEntry<SoundEvent> entry() {
            return entry;
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }
    public static final Entry ENTITY_GLAZE_AMBIENT = add(new Entry("entity.glaze.ambient"));
    public static final Entry ENTITY_GLAZE_HURT = add(new Entry("entity.glaze.hurt"));
    public static final Entry ENTITY_GLAZE_DEATH = add(new Entry("entity.glaze.death"));
    public static final Entry ENTITY_GLAZE_FREEZE = add(new Entry("entity.glaze.freeze"));
    public static final Entry VILLAGER_INNKEEPER = add(new Entry("villager.innkeeper"));
    public static final Entry FROSTMONARCH_SCREECH = add(new Entry("frostmonarch.screech"));
    public static final Entry FROSTMONARCH_DEEP_FREEZE = add(new Entry("frostmonarch.deep_freeze"));
    public static final Entry FROSTMONARCH_SPAWNED_LAUGH = add(new Entry("frostmonarch.spawned_laugh"));
    public static final Entry FROSTMONARCH_SPAWNED_STORM = add(new Entry("frostmonarch.spawned_storm"));
    public static final Entry FROSTMONARCH_HEALING = add(new Entry("frostmonarch.healing"));
    public static final Entry FROSTMONARCH_HURT = add(new Entry("frostmonarch.hurt"));
    public static final Entry FROSTMONARCH_DEATH = add(new Entry("frostmonarch.death"));
    public static final Entry MUSIC_FROST_MONARCH_BATTLE = add(new Entry("music.frost_monarch_battle"));
    public static final Entry MUSIC_FROZEN_DEPTHS = add(new Entry("music.frozen_depths"));
    public static final Entry BLOCK_FROZEN_SOUL_AMBIENT = add(new Entry("block.frozen_soul_block.ambient"));

    public static void register() {
        for (var entry: entries) {
            entry.entry = Registry.registerReference(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
    }
}
