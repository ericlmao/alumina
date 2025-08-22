/*
 * MIT License
 *
 * Copyright (C) 2025 Negative Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package games.negative.alumina.sound;

import com.google.common.base.Preconditions;
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

import javax.naming.ldap.PagedResultsControl;
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

    // --- Builders --- \\

    /**
     * Make a customizable sound from {@link NamespacedKey}
     * @param key The key of the sound
     * @param volume The specified volume
     * @param pitch The specified pitch
     * @return {@link Sound} instance
     */
    public static Sound of(@NotNull NamespacedKey key, float volume, float pitch) {
        return new Sound(key, volume, pitch);
    }

    /**
     * Make a customizable sound from {@link NamespacedKey}
     * @param key The key of the sound
     * @return {@link Sound} instance
     */
    public static Sound of(@NotNull NamespacedKey key) {
        return of(key, 1.0f, 1.0f);
    }

    /**
     * Make a customizable sound from {@link org.bukkit.Sound}
     * @param sound The specified sound
     * @param volume The specified volume
     * @param pitch The specified pitch
     * @return {@link Sound} instance
     */
    public static Sound of(@NotNull org.bukkit.Sound sound, float volume, float pitch) {
        NamespacedKey key = RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT).getKey(sound);
        Preconditions.checkNotNull(key, "Could not find sound for key " + key);

        return of(key, volume, pitch);
    }

    /**
     * Make a customizable sound from {@link org.bukkit.Sound}
     * @param sound The specified sound
     * @return {@link Sound} instance
     */
    public static Sound of(@NotNull org.bukkit.Sound sound) {
        return of(sound, 1.0f, 1.0f);
    }

}
