package games.negative.alumina.sound;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import games.negative.alumina.logger.Logs;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public record Sound(
        @NotNull NamespacedKey key,
        float volume,
        float pitch
) {

    private static final LoadingCache<NamespacedKey, org.bukkit.Sound> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .build(new CacheLoader<>() {
                @Override
                public org.bukkit.@NotNull Sound load(@NotNull NamespacedKey key) throws Exception {
                    org.bukkit.Sound sound = RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(key);
                    if (sound == null) throw new Exception("Could not find sound for " + key);

                    return sound;
                }
            });

    public org.bukkit.Sound getBukkitSound() {
        try {
            return CACHE.get(key);
        } catch (ExecutionException e) {
            Logs.error(e.getMessage());
            return null;
        }
    }

    /**
     * Play the sound for a specified player at a custom volume and pitch
     * @param player The player which the sound is played for
     * @param volume The volume the sound is played at
     * @param pitch The pitch the sound is played at
     */
    public void playSound(@NotNull Player player, float volume, float pitch) {
        org.bukkit.Sound sound = getBukkitSound();
        if (sound == null) return;

        player.playSound(player, sound, volume, pitch);
    }

    /**
     * Play the sound for a specified player
     * @param player The player which the sound is played for
     */
    public void playSound(@NotNull Player player) {
        playSound(player, this.volume, this.pitch);
    }

    /**
     * Play the sound at a specified location at a custom volume and pitch
     * @param location The location which the sound is played
     * @param volume The volume which the sound is played at
     * @param pitch The pitch which the sound is played at
     */
    public void playSound(@NotNull Location location, float volume, float pitch) {
        org.bukkit.Sound sound = getBukkitSound();
        if (sound == null) return;

        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, sound, volume, pitch);
    }

    /**
     * Play the sound at a specified location
     * @param location The location which the sound is played
     */
    public void playSound(@NotNull Location location) {
        playSound(location, this.volume, this.pitch);
    }

    // --- Builders ---
    public static Sound of(@NotNull NamespacedKey key) {
        return new Sound(key, 1.0f, 1.0f);
    }

    public static Sound of(@NotNull NamespacedKey key, float volume, float pitch) {
        return new Sound(key, volume, pitch);
    }

    public static Sound of(@NotNull org.bukkit.Sound )

}
