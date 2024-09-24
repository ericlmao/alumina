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

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import games.negative.alumina.logger.Logs;
import games.negative.alumina.util.MiniMessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Represents a message that can be sent to a {@link Audience}.
 * <p>
 * This supports color codes, placeholders, and hex colors.
 * <p>
 */
@SuppressWarnings("unused")
public class Message {

    private static Component PREFIX;

    private static final Map<String, Tag> CUSTOM_TAGS = Maps.newConcurrentMap();

    private static MiniMessage miniMessage;

    /*
     * This is the default, unmodified message.
     */
    private final String content;

    /**
     * Whether to parse PlaceholderAPI placeholders.
     */
    private final boolean papi;


    /**
     * Creates a new message.
     *
     * @param text The text of the message.
     */
    public Message(@NotNull final String text) {
        Preconditions.checkNotNull(text, "Text cannot be null.");

        this.content = text;
        this.papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     * Send the final message to a {@link Audience}.
     *
     * @param audience The recipient of the message.
     */
    public void send(@NotNull Audience audience, @Nullable String... placeholders) {
        Preconditions.checkNotNull(audience, "Audience cannot be null.");

        Component component = asComponent(audience, placeholders);
        audience.sendMessage(component);
    }

    /**
     * Send the final message to a {@link Audience}.
     *
     * @param audience The recipient of the message.
     */
    @SafeVarargs
    public final void send(@NotNull Audience audience, @Nullable Map.Entry<String, Component>... placeholders) {
        Preconditions.checkNotNull(audience, "Audience cannot be null.");

        Component component = asComponent(audience, placeholders);
        audience.sendMessage(component);
    }

    /**
     * Send the final message to a {@link Audience}.
     *
     * @param audience The recipient of the message.
     */
    public void send(@NotNull Audience audience) {
        Preconditions.checkNotNull(audience, "Audience cannot be null.");

        Component component = asComponent(audience);
        audience.sendMessage(component);
    }

    /**
     * Send the final message to an iterable collection of a class that extends {@link Audience}
     * @param iterable The iterable collection of a class that extends {@link Audience}
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @param <T> The class that extends {@link Audience}
     */
    public <T extends Iterable<? extends Audience>> void send(@NotNull T iterable, @Nullable String... placeholders) {
        Preconditions.checkNotNull(iterable, "Iterable cannot be null.");
        Preconditions.checkArgument(iterable.iterator().hasNext(), "Iterable cannot be empty.");

        for (Audience audience : iterable) {
            send(audience, placeholders);
        }
    }

    /**
     * Send the final message to an iterable collection of a class that extends {@link Audience}
     * @param iterable The iterable collection of a class that extends {@link Audience}
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @param <T> The class that extends {@link Audience}
     */
    public <T extends Iterable<? extends Audience>> void send(@NotNull T iterable, @Nullable Map.Entry<String, Component>... placeholders) {
        Preconditions.checkNotNull(iterable, "Iterable cannot be null.");
        Preconditions.checkArgument(iterable.iterator().hasNext(), "Iterable cannot be empty.");

        for (Audience audience : iterable) {
            send(audience, placeholders);
        }
    }

    /**
     * Send the final message to an iterable collection of a class that extends {@link Audience}
     * @param iterable The iterable collection of a class that extends {@link Audience}
     * @param <T> The class that extends {@link Audience}
     */
    public <T extends Iterable<? extends Audience>> void send(@NotNull T iterable) {
        Preconditions.checkNotNull(iterable, "Iterable cannot be null.");
        Preconditions.checkArgument(iterable.iterator().hasNext(), "Iterable cannot be empty.");

        for (Audience audience : iterable) {
            send(audience);
        }
    }

    /**
     * Broadcast the final message to the server.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     */
    public void broadcast(@Nullable String... placeholders) {
        broadcast(null, placeholders);
    }

    /**
     * Broadcast the final message to the server.
     * @param audience The audience to display the message to.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     */
    public void broadcast(@Nullable Audience audience, @Nullable String... placeholders) {
        Component component = asComponent(audience, placeholders);
        Bukkit.getServer().broadcast(component);
    }

    /**
     * Broadcast the final message to the server.
     */
    public void broadcast() {
        broadcast(null, (String[]) null);
    }

    /**
     * Converts the message to a Component.
     * @param audience The audience to display the message to.
     * @return The Component representation of the message.
     */
    @NotNull
    public Component asComponent(@Nullable Audience audience) {
        return asComponent(audience, (String[]) null);
    }

    /**
     * Converts the message to a Component.
     *
     * @param audience     The audience to display the message to.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @return The Component representation of the message.
     * @throws NullPointerException     if the audience is null.
     * @throws IllegalArgumentException if the number of placeholders is not even.
     */
    @NotNull
    public Component asComponent(@Nullable Audience audience, @Nullable String... placeholders) {
        String current = content;
        if (papi) {
            current = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders((audience instanceof Player player ? player : null), current);
        }

        if (placeholders != null) {
            Preconditions.checkArgument(placeholders.length % 2 == 0, "Placeholders must be in key-value pairs.");

            for (int i = 0; i < placeholders.length; i += 2) {
                String placeholder = placeholders[i];
                String replacement = placeholders[i + 1];

                if (placeholder == null || replacement == null) {
                    Logs.warning("Placeholder of " + placeholder + " has result of " + replacement + ". None of these value can be null. Skipping.", true);
                    continue;
                }

                current = current.replaceAll(placeholder, replacement);
            }
        }

        if (miniMessage == null) {
            miniMessage = MiniMessage.builder().editTags(builder -> {
                for (Map.Entry<String, Tag> entry : CUSTOM_TAGS.entrySet()) {
                    builder.tag(entry.getKey(), entry.getValue());
                }
            }).build();
        }

        Component component = MiniMessageUtil.translate(current, miniMessage);
        if (PREFIX != null) {
            component = component.replaceText(TextReplacementConfig.builder().matchLiteral("%prefix%").replacement(PREFIX).build());
        }

        return component;
    }

    /**
     * Converts the message to a Component.
     *
     * @param audience     The audience to display the message to.
     * @param placeholders The optional key-value pairs of placeholders to replace in the message.
     * @return The Component representation of the message.
     * @throws NullPointerException     if the audience is null.
     * @throws IllegalArgumentException if the number of placeholders is not even.
     */
    @SafeVarargs
    @NotNull
    public final Component asComponent(@Nullable Audience audience, @Nullable Map.Entry<String, Component>... placeholders) {
        String current = content;
        if (papi) {
            current = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders((audience instanceof Player player ? player : null), current);
        }

        if (miniMessage == null) {
            miniMessage = MiniMessage.builder().editTags(builder -> {
                for (Map.Entry<String, Tag> entry : CUSTOM_TAGS.entrySet()) {
                    builder.tag(entry.getKey(), entry.getValue());
                }
            }).build();
        }

        Component component = MiniMessageUtil.translate(current, miniMessage);
        if (PREFIX != null) {
            component = component.replaceText(TextReplacementConfig.builder().matchLiteral("%prefix%").replacement(PREFIX).build());
        }

        if (placeholders != null) {
            for (Map.Entry<String, Component> placeholder : placeholders) {
                if (placeholder == null) continue;

                String key = placeholder.getKey();
                Component value = placeholder.getValue();

                if (key == null || value == null) {
                    Logs.warning("Placeholder of " + key + " has result of " + value + ". None of these value can be null. Skipping.", true);
                    continue;
                }

                component = component.replaceText(TextReplacementConfig.builder().matchLiteral(key).replacement(value).build());
            }
        }

        return component;
    }

    /**
     * Returns the content of the message.
     *
     * @return The content of the message.
     */
    @NotNull
    public String content() {
        return this.content;
    }

    /**
     * Statically create a new message instance using {@link Message#of(String...)}
     *
     * @param text The text of the message.
     * @return The message.
     */
    public static Message of(@NotNull final String... text) {
        Preconditions.checkNotNull(text, "Text cannot be null.");
        Preconditions.checkArgument(text.length > 0, "Text cannot be empty.");

        return new Message(String.join("\n", text));
    }

    /**
     * Statically create a new message instance using {@link Message#of(String)}
     *
     * @param text The text of the message.
     * @return The message.
     */
    public static Message of(@NotNull final String text) {
        Preconditions.checkNotNull(text, "Text cannot be null.");

        return new Message(text);
    }

    /**
     * Statically create a new message instance using {@link Message#of(List)}
     *
     * @param text The text of the message.
     * @return The message.
     */
    public static Message of(@NotNull final List<String> text) {
        Preconditions.checkNotNull(text, "Text cannot be null.");
        Preconditions.checkArgument(!text.isEmpty(), "Text cannot be empty.");

        return new Message(String.join("\n", text));
    }

    /**
     * Register a custom minimessage tag for global use in all messages.
     * @param name The name of the tag.
     * @param tag The tag to register.
     */
    public static void registerTag(@NotNull String name, @NotNull Tag tag) {
        CUSTOM_TAGS.put(name, tag);
    }

    /**
     * Set the prefix of the message.
     * @param component The prefix of the message.
     */
    public static void setPrefix(@Nullable Component component) {
        PREFIX = component;
    }

    @Nullable
    public static Component prefix() {
        return PREFIX;
    }

    @NotNull
    public static Map<String, Tag> customTags() {
        return CUSTOM_TAGS;
    }
}
