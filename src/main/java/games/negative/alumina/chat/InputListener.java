/*
 *  MIT License
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
 *
 */

package games.negative.alumina.chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import games.negative.alumina.event.Events;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * The InputListener class is responsible for listening to player input events, such as chat messages and commands.
 * It provides a static method to register input listeners and a functional interface for handling input responses.
 * When a player sends a chat message or command,
 * the InputListener will check if there is a registered listener for that player
 * and execute the corresponding response.
 */
public class InputListener {

    private static final Cache<UUID, InputProcessor> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.of(10, ChronoUnit.MINUTES))
            .build();

    static {
        // Listen to chat messages
        Events.listen(AsyncChatEvent.class, event -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            if (!cache.asMap().containsKey(uuid)) return;

            InputProcessor response = cache.getIfPresent(uuid);
            if (response == null) return;

            try {
                event.setCancelled(true);
                response.process(event);
            } catch (Exception ignored) {} finally {
                cache.invalidate(uuid);
            }
        });

        // Remove the listener when the player quits the server
        Events.listen(PlayerQuitEvent.class, event -> cache.invalidate(event.getPlayer().getUniqueId()));
    }

    /**
     * Registers an input listener for a specific player.
     *
     * @param uuid The UUID of the player to register the listener for.
     * @param response The input listener response that will be executed when the player sends a chat message or command.
     */
    public static void listen(@NotNull UUID uuid, @NotNull InputListener.InputProcessor response) {
        cache.put(uuid, response);
    }

    /**
     * Functional interface for handling input responses from players.
     */
    @FunctionalInterface
    public interface InputProcessor {

        /**
         * Process the input message from a player.
         *
         * @param event The chat event that contains the input message.
         */
        void process(@NotNull AsyncChatEvent event);

    }
}
