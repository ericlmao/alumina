/*
 *  MIT License
 *
 * Copyright (C) 2024 Negative Games
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
 *
 */

package games.negative.alumina.sound;

import com.google.common.base.Preconditions;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * A simple class to play sounds
 * @param sound The sound to play
 * @param volume The volume
 * @param pitch The pitch
 */
public record BukkitSound(@NotNull Sound sound, float volume, float pitch) implements MinecraftSound {

    /**
     * Converts a ConfigurationSection to a BukkitSound object.
     *
     * @param section The ConfigurationSection to convert. Must not be null.
     * @return The converted BukkitSound object.
     * @throws NullPointerException if the 'section' parameter is null, or if the 'sound' value in the section is null.
     */
    public static BukkitSound fromConfiguration(@NotNull ConfigurationSection section) {
        Preconditions.checkNotNull(section, "ConfigurationSection cannot be null");

        String soundName = section.getString("sound");
        Preconditions.checkNotNull(soundName, "Sound name cannot be null");

        Sound sound = Sound.valueOf(soundName);
        float volume = (float) section.getDouble("volume", 1.0);
        float pitch = (float) section.getDouble("pitch", 1.0);

        return new BukkitSound(sound, volume, pitch);
    }

}
