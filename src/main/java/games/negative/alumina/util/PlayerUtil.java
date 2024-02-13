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

package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import games.negative.alumina.future.BukkitCompletableFuture;
import games.negative.alumina.future.BukkitFuture;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Player utility to handle some player-related tasks.
 */
public class PlayerUtil {

    /**
     * Reset the player to default properties.
     *
     * @param player The player to reset.
     */
    public static void reset(@NotNull Player player) {
        Preconditions.checkNotNull(player, "'player' cannot be null!");

        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        player.setWalkSpeed(0.2F);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.getOpenInventory().close();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20);
        resetHealth(player);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setFireTicks(0);
        player.resetPlayerTime();
        player.resetPlayerWeather();
        player.setGameMode(GameMode.SURVIVAL);
        if (player.getVehicle() != null)
            player.leaveVehicle();

        if (!player.getPassengers().isEmpty())
            player.getPassengers().forEach(Entity::leaveVehicle);
    }

    /**
     * Reset the player's health.
     *
     * @param player The player to reset.
     */
    private static void resetHealth(@NotNull Player player) {
        Preconditions.checkNotNull(player, "'player' cannot be null!");

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) return;

        Collection<AttributeModifier> modifiers = attribute.getModifiers();
        if (modifiers.isEmpty()) return;

        List<AttributeModifier> toRemove = Lists.newArrayList(modifiers);
        toRemove.forEach(attribute::removeModifier);
    }

    /**
     * Fills the player's inventory with the given collection of items
     *
     * @param player     The player
     * @param collection The collection of items
     * @param <T>        The type of collection
     * @return The collection of items that could not be added to the player's inventory
     */
    public static  <T extends Iterable<ItemStack>> Collection<ItemStack> fillInventory(@NotNull Player player, @NotNull T collection) {
        Preconditions.checkNotNull(player, "'player' cannot be null!");
        Preconditions.checkNotNull(collection, "'collection' cannot be null!");

        List<ItemStack> failed = Lists.newArrayList();

        PlayerInventory inv = player.getInventory();
        for (ItemStack item : collection) {
            // Make sure player's inventory isn't full
            if (inv.firstEmpty() != -1) {
                inv.addItem(item);
                continue;
            }

            int amount = item.getAmount();

            for (ItemStack content : inv.getContents()) {
                if (content == null || !content.isSimilar(item)) continue;

                int max = content.getMaxStackSize();
                int current = content.getAmount();

                int difference = Math.abs(max - current);
                if (amount > difference) {
                    content.setAmount(max);
                    amount -= difference;
                    continue;
                }

                content.setAmount(current + amount);
                amount = 0;
            }

            if (amount <= 0) continue;

            item.setAmount(amount);
            failed.add(item);
        }

        player.updateInventory();
        return failed;
    }

    /**
     * Get the player's UUID by their username.
     * @param username The username of the player.
     * @return The UUID of the player.
     * @throws IOException If an error occurs while getting the UUID.
     */
    public static UUID getByName(@NotNull String username) throws IOException {
        Preconditions.checkNotNull(username, "'username' cannot be null!");

        JsonObject response = HTTPUtil.get("https://api.mojang.com/users/profiles/minecraft/" + username);
        if (response == null) return null;

        String uuidStr = response.get("id").getAsString();
        uuidStr = uuidStr.replaceAll("\"", "")
                .replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

        return UUID.fromString(uuidStr);
    }

    /**
     * Retrieves an OfflinePlayer object for the given username.
     *
     * @param username The username of the player.
     * @return A BukkitFuture object that completes with the OfflinePlayer.
     * @throws NullPointerException if 'username' is null.
     */
    public static BukkitFuture<OfflinePlayer> getOfflinePlayer(@NotNull String username) {
        Preconditions.checkNotNull(username, "'username' cannot be null!");;

        BukkitFuture<OfflinePlayer> future = new BukkitCompletableFuture<>();
        BukkitFuture<UUID> uuidFuture = new BukkitCompletableFuture<>();
        uuidFuture.supplyAsync(() -> {
            try {
                return getByName(username);
            } catch (IOException e) {
                return null;
            }
        });

        uuidFuture.whenCompleteAsync(uuid -> {
            if (uuid == null) {
                future.cancel();
                return;
            }

            future.supply(() -> Bukkit.getOfflinePlayer(uuid));
        });

        return future;
    }

    /**
     * Checks if a player with the given UUID is online.
     *
     * @param uuid The UUID of the player.
     * @return true if the player is online, false otherwise.
     * @throws NullPointerException if 'uuid' is null.
     */
    public static boolean isOnline(@NotNull UUID uuid) {
        Preconditions.checkNotNull(uuid, "'uuid' cannot be null!");

        return Bukkit.getPlayer(uuid) != null;
    }

    /**
     * Checks if the given username is online.
     *
     * @param username The username to check.
     * @return {@code true} if the username is online, {@code false} otherwise.
     */
    public static boolean isOnline(@NotNull String username) {
        Preconditions.checkNotNull(username, "'username' cannot be null!");

        return Bukkit.getPlayer(username) != null;
    }

    /**
     * Retrieves a player by their UUID.
     *
     * @param uuid The UUID of the player.
     * @return Optional<Player> The player, or an empty Optional if the player is not found.
     * @throws NullPointerException if 'uuid' is null.
     */
    public static Optional<Player> getPlayer(@NotNull UUID uuid) {
        Preconditions.checkNotNull(uuid, "'uuid' cannot be null!");

        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    /**
     * Sends a title and subtitle message to a player with specified fade in, stay, and fade out times.
     * @param player The player to send the title and subtitle to.
     * @param title The title message. Can be null.
     * @param subtitle The subtitle message. Can be null.
     * @param fadeIn The time in ticks for the title and subtitle to fade in.
     * @param stay The time in ticks for the title and subtitle to stay on the screen.
     * @param fadeOut The time in ticks for the title and subtitle to fade out.
     * @throws IllegalArgumentException If the player is null.
     * @throws IllegalStateException If both title and subtitle are null.
     */
    public static void sendTitle(@NotNull Player player, @Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        Preconditions.checkNotNull(player, "'player' cannot be null!");

        if (title == null && subtitle == null) throw new IllegalStateException("You cannot have `title` and `subtitle` be null at the same time!");

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * Sends an action bar message to a player.
     *
     * @param player The player to send the action bar message to.
     * @param message The message to be displayed in the action bar.
     *
     * @throws NullPointerException if {@code player} or {@code message} is null.
     */
    public static void sendActionBar(@NotNull Player player, @NotNull String message) {
        Preconditions.checkNotNull(player, "'player' cannot be null!");
        Preconditions.checkNotNull(message, "'message' cannot be null!");

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
