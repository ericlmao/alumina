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
