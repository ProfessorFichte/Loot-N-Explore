package lne.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static lne.LneMod.MOD_ID;

public class ModSounds {
    public static final class Entry {
        private final Identifier id;
        private final SoundEvent soundEvent;
        private RegistryEntry<SoundEvent> entry;
        private String subtitle;

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

        public Entry subtitle(String text) {
            this.subtitle = text;
            return this;
        }

        public String subtitle() {
            return subtitle;
        }

        public String subtitleKey() {
            return "subtitles." + id.getNamespace() + "." + id.getPath();
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }
    public static final Entry ENTITY_GLAZE_AMBIENT = add(new Entry("entity.glaze.ambient").subtitle("Glaze breathes"));
    public static final Entry ENTITY_GLAZE_HURT = add(new Entry("entity.glaze.hurt").subtitle("Glaze hurts"));
    public static final Entry ENTITY_GLAZE_DEATH = add(new Entry("entity.glaze.death").subtitle("Glaze dies"));
    public static final Entry ENTITY_GLAZE_FREEZE = add(new Entry("entity.glaze.freeze").subtitle("Glaze crackles"));
    public static final Entry VILLAGER_INNKEEPER = add(new Entry("villager.innkeeper").subtitle("Innkeeper works"));
    public static final Entry FROSTMONARCH_SCREECH = add(new Entry("frostmonarch.screech").subtitle("Frost Monarch screeches"));
    public static final Entry FROSTMONARCH_DEEP_FREEZE = add(new Entry("frostmonarch.deep_freeze").subtitle("Ice bursts"));
    public static final Entry FROSTMONARCH_SPAWNED_LAUGH = add(new Entry("frostmonarch.spawned_laugh").subtitle("Frost Monarch laughs"));
    public static final Entry FROSTMONARCH_SPAWNED_STORM = add(new Entry("frostmonarch.spawned_storm").subtitle("Blizzard howls"));
    public static final Entry FROSTMONARCH_HEALING = add(new Entry("frostmonarch.healing").subtitle("Frost Monarch gathers strength"));
    public static final Entry FROSTMONARCH_HURT = add(new Entry("frostmonarch.hurt").subtitle("Frost Monarch hurts"));
    public static final Entry FROSTMONARCH_DEATH = add(new Entry("frostmonarch.death").subtitle("Frost Monarch dies"));
    public static final Entry MUSIC_FROST_MONARCH_BATTLE = add(new Entry("music.frost_monarch_battle"));
    public static final Entry MUSIC_FROZEN_DEPTHS = add(new Entry("music.frozen_depths"));
    public static final Entry MUSIC_INN = add(new Entry("music.inn"));
    public static final Entry BLOCK_FROZEN_SOUL_AMBIENT = add(new Entry("block.frozen_soul_block.ambient").subtitle("Frozen souls whisper"));

    public static void register() {
        for (var entry: entries) {
            entry.entry = Registry.registerReference(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
    }
}
