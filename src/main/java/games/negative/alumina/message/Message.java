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

package games.negative.alumina.message;

import com.google.common.collect.Maps;
import games.negative.alumina.message.translation.LegacyMiniMessageTranslator;
import games.negative.alumina.util.PluginUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a MiniMessage message with placeholders.
 */
public class Message {

    private static MiniMessage DEFAULT_PROVIDER = MiniMessage.miniMessage();

    private final String content;

    /**
     * Creates a new message with the specified content.
     * @param content The content of the message.
     */
    public Message(@NotNull String content) {
        this.content = content;
    }

    /**
     * Creates a new message with the specified content.
     * @param content The content of the message.
     */
    public Message(@NotNull String... content) {
        this(String.join("<newline>", content));
    }

    /**
     * Get the content of the message.
     * @return The content of the message.
     */
    @NotNull
    public String content() {
        return content;
    }

    /**
     * Get the content of the message as a list.
     * @return The content of the message as a list.
     */
    @NotNull
    @Unmodifiable
    public List<String> contentList() {
        return List.of(content.split("<newline>"));
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

        /**
         * Get a {@link Component} representation of the message.
         * @return The component representation of the message.
         */
        @NotNull
        public Component asComponent() {
            return asComponent(null);
        }

        /**
         * Get a {@link Component} representation of the message.
         * @param viewer The viewer of the message.
         * @return The component representation of the message.
         * @param <V> The type of the viewer.
         */
        public <V extends Audience> Component asComponent(@Nullable V viewer) {
            return asComponent(viewer, null, null);
        }

        /**
         * Get a {@link Component} representation of the message.
         * @param viewer The viewer of the message.
         * @param pre The function to apply to the pre-process builder.
         * @param post The function to apply to the post-process builder.
         * @return The component representation of the message.
         * @param <V> The type of the viewer.
         */
        @NotNull
        public <V extends Audience> Component asComponent(@Nullable V viewer, @Nullable Consumer<PreProcessBuilder> pre, @Nullable Consumer<PostProcessBuilder> post) {
            PostProcessBuilder builder = new PostProcessBuilder();
            if (post != null) post.accept(builder);

            Component component = provider.deserialize(asMiniMessage(viewer, pre));

            // Replacements for components
            if (builder.parsePlaceholders) {
                for (Map.Entry<String, Component> entry : componentPlaceholders.entrySet()) {
                    component = component.replaceText(TextReplacementConfig.builder().matchLiteral(entry.getKey()).replacement(entry.getValue()).build());
                }
            }

            // Parse ItemsAdder unicodes if available.
            if (builder.parseItemsAdder && PluginUtil.hasPlugin("ItemsAdder") && viewer instanceof Player player) {
                component = parseItemsAdder(player, component);
            }

            return component;
        }

        /**
         * Get a {@link String} representation of the message.
         * @return The component representation of the message.
         */
        @NotNull
        public String asMiniMessage() {
            return asMiniMessage(null);
        }

        /**
         * Get a {@link Component} representation of the message.
         * @param viewer The viewer of the message.
         * @return The component representation of the message.
         * @param <V> The type of the viewer.
         */
        @NotNull
        public <V extends Audience> String asMiniMessage(@Nullable V viewer) {
            return asMiniMessage(viewer, null);
        }

        /**
         * Get a {@link Component} representation of the message.
         * @param viewer The viewer of the message.
         * @param function The function to apply to the pre-process builder.
         * @return The component representation of the message.
         * @param <V> The type of the viewer.
         */
        @NotNull
        public <V extends Audience> String asMiniMessage(@Nullable V viewer, @Nullable Consumer<PreProcessBuilder> function) {
            PreProcessBuilder builder = new PreProcessBuilder();
            if (function != null) function.accept(builder);

            // Parse PlaceholderAPI if available.
            if (builder.parsePlaceholderApi && Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                Player player = (viewer instanceof Player) ? (Player) viewer : null;
                current = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, current);
            }

            // Parse legacy color codes if available.
            if (builder.parseLegacyColors && current.contains("&")) {
                current = LegacyMiniMessageTranslator.legacyToMiniMessage(current);
            }

            // Replacements for pre-serialized strings
            if (builder.parsePlaceholders) {
                for (Map.Entry<String, String> entry : stringPlaceholders.entrySet()) {
                    current = current.replaceAll(entry.getKey(), entry.getValue());
                }
            }

            return current;
        }
    }

    private Component parseItemsAdder(@NotNull Player player, @NotNull Component component) {
        try {
            Class.forName("dev.lone.itemsadder.api.FontImages");

            Class<?> clazz = Class.forName("dev.lone.itemsadder.api.FontImages.FontImageWrapper");
            Object method = clazz.getDeclaredMethod("replaceFontImages", Permissible.class, Component.class).invoke(null, player, component);
            return (Component) method;
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            return component;
        }
    }

    public static class PreProcessBuilder {
        private boolean parsePlaceholders = true;
        private boolean parsePlaceholderApi = true;
        private boolean parseLegacyColors = true;

        public PreProcessBuilder parsePlaceholders(boolean parsePlaceholders) {
            this.parsePlaceholders = parsePlaceholders;
            return this;
        }

        public PreProcessBuilder parsePlaceholderApi(boolean parsePlaceholderApi) {
            this.parsePlaceholderApi = parsePlaceholderApi;
            return this;
        }

        public PreProcessBuilder parseLegacyColors(boolean parseLegacyColors) {
            this.parseLegacyColors = parseLegacyColors;
            return this;
        }
    }

    public static class PostProcessBuilder {
        private boolean parsePlaceholders = true;
        private boolean parseItemsAdder = true;

        public PostProcessBuilder parsePlaceholders(boolean parsePlaceholders) {
            this.parsePlaceholders = parsePlaceholders;
            return this;
        }

        public PostProcessBuilder parseItemsAdder(boolean parseItemsAdder) {
            this.parseItemsAdder = parseItemsAdder;
            return this;
        }
    }
}

