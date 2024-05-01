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
import games.negative.alumina.logger.Logs;
import games.negative.alumina.util.MiniMessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a message that can be sent to a {@link Audience}.
 * <p>
 * This supports color codes, placeholders, and hex colors.
 * <p>
 */
@SuppressWarnings("unused")
public class Message {

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
     * Send the final message to an iterable collection of a class that extends {@link Audience}
     *
     * @param iterable The iterable collection of a class that extends {@link Audience}
     * @param <T>      The class that extends {@link Audience}
     */
    public <T extends Iterable<? extends Audience>> void send(@NotNull final T iterable) {
        send(iterable, (String) null);
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
                    Logs.WARNING.print("Placeholder of " + placeholder + " has result of " + replacement + ". None of these value can be null. Skipping.", true);
                    continue;
                }

                current = current.replaceAll(placeholder, replacement);
            }
        }

        return MiniMessageUtil.translate(current);
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

}
