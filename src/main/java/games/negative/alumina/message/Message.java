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

package games.negative.alumina.message;

import com.google.common.collect.Maps;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a MiniMessage message with placeholders.
 */
public class Message {

    private static MiniMessage DEFAULT_PROVIDER = MiniMessage.miniMessage();

    private final String content;

    public Message(@NotNull String content) {
        this.content = content;
    }

    @NotNull
    @CheckReturnValue
    public Builder create() {
        return new Builder();
    }

    @NotNull
    @CheckReturnValue
    public Builder create(@NotNull MiniMessage provider) {
        return new Builder(provider);
    }

    /**
     * Sets the default provider for all messages.
     * @param instance The provider to use.
     */
    public static void setDefaultProvider(@NotNull MiniMessage instance) {
        DEFAULT_PROVIDER = instance;
    }

    public class Builder {

        private String current = content;
        private final MiniMessage provider;
        private final Map<String, String> stringPlaceholders = Maps.newHashMap();
        private final Map<String, Component> componentPlaceholders = Maps.newHashMap();

        /**
         * Creates a new builder instance with the default provider.
         */
        public Builder() {
            this(DEFAULT_PROVIDER);
        }

        /**
         * Creates a new builder instance with the specified provider.
         * @param provider The provider to use.
         */
        public Builder(@NotNull MiniMessage provider) {
            this.provider = provider;
        }

        /**
         * Replaces a placeholder with a string.
         * @param placeholder The placeholder to replace.
         * @param replacement The replacement string.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder replace(@NotNull String placeholder, @NotNull String replacement) {
            stringPlaceholders.put(placeholder, replacement);
            return this;
        }

        /**
         * Replaces a placeholder with a component.
         * @param placeholder The placeholder to replace.
         * @param replacement The replacement component.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder replace(@NotNull String placeholder, @NotNull Component replacement) {
            componentPlaceholders.put(placeholder, replacement);
            return this;
        }

        /**
         * Sends the message to the recipient.
         * @param recipient The recipient of the message.
         * @param <T> The type of the recipient.
         */
        public <T extends Audience> void send(@NotNull T recipient) {
            Component component = asComponent(recipient);
            recipient.sendMessage(component);
        }

        @NotNull
        public <V extends Audience> Component asComponent(@Nullable V viewer) {
            // Replacements for pre-serialized strings
            for (Map.Entry<String, String> entry : stringPlaceholders.entrySet()) {
                current = current.replaceAll(entry.getKey(), entry.getValue());
            }

            // Parse PlaceholderAPI if available.
            if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                Player player = (viewer instanceof Player) ? (Player) viewer : null;
                current = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, current);
            }

            Component component = provider.deserialize(current);

            // Replacements for components
            for (Map.Entry<String, Component> entry : componentPlaceholders.entrySet()) {
                component = component.replaceText(TextReplacementConfig.builder().matchLiteral(entry.getKey()).replacement(entry.getValue()).build());
            }

            return component;
        }
    }
}

