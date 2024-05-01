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

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MinecraftSound {

    /**
     * The sound to play.
     * @return The sound to play.
     */
    @NotNull
    Sound sound();

    /**
     * The volume of the sound.
     * @return The volume of the sound.
     */
    float volume();

    /**
     * The pitch of the sound.
     * @return The pitch of the sound.
     */
    float pitch();

    /**
     * Plays the sound to the player.
     * @param player The player to play the sound to.
     */
    default void play(@NotNull Player player) {
        player.playSound(player, sound(), volume(), pitch());
    }

    /**
     * Plays the sound at the location.
     * @param location The location to play the sound at.
     */
    default void play(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, sound(), volume(), pitch());
    }

}
