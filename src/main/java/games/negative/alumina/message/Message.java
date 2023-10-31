/*
 *
 *  *  MIT License
 *  *
 *  * Copyright (C) 2023 Negative Games
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *  *
 *
 */

package games.negative.alumina.message;

import com.google.common.base.Preconditions;
import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.message.color.ColorAgent;
import games.negative.alumina.model.Deliverable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a message that can be sent to a {@link CommandSender}.
 * <p>
 * This supports color codes, placeholders, and hex colors.
 * <p>
 */
public class Message implements Deliverable<CommandSender> {

    /*
     * This is the color agent that is used to translate color codes.
     */
    private final ColorAgent colorAgent;

    /*
     * This is the default, unmodified message.
     */
    private final String def;

    /*
     * This is the current message with all modifications.
     */
    private String current;

    /*
     * This is used to determine if PlaceholderAPI should be used.
     */
    private boolean parsePlaceholderAPI = false;

    /**
     * Creates a new message.
     *
     * @param text The text of the message.
     */
    protected Message(@NotNull String text) {
        this.def = text;
        this.current = text;
        this.colorAgent = AluminaPlugin.getAluminaInstance().getColorAgent();
    }

    /**
     * Replaces a placeholder with a replacement.
     *
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement.
     * @return The message.
     */
    public Message replace(@NotNull String placeholder, @NotNull String replacement) {
        this.current = this.current.replace(placeholder, replacement);
        return this;
    }

    /**
     * Allow the message to be parsed by PlaceholderAPI.
     *
     * @return The message.
     * @throws IllegalStateException If PlaceholderAPI is not installed.
     */
    public Message parsePlaceholderAPI() {
        Preconditions.checkState(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null, "PlaceholderAPI is not installed.");
        this.parsePlaceholderAPI = true;
        return this;
    }

    /**
     * Send the final message to a {@link CommandSender}.
     *
     * @param sender The sender to send the message to.
     */
    @Override
    public void send(@NotNull CommandSender sender) {
        String translate = colorAgent.translate(this.current);
        String text = parsePAPI(sender, translate);
        String[] message = text.split("\n");

        for (String line : message) {
            sender.sendMessage(line);
        }

        this.current = def;
    }

    /**
     * Send the final message to an iterable collection of a class that extends {@link CommandSender}
     *
     * @param iterable The iterable collection of a class that extends {@link CommandSender}
     * @param <T>      The class that extends {@link CommandSender}
     */
    public <T extends Iterable<? extends CommandSender>> void send(T iterable) {
        String translate = colorAgent.translate(this.current);
        String text = parsePAPI(null, translate);
        String[] message = text.split("\n");

        for (CommandSender sender : iterable) {
            for (String line : message) {
                sender.sendMessage(line);
            }
        }

        this.current = def;
    }

    /**
     * Broadcast the final message to the server.
     */
    public void broadcast() {
        String translate = colorAgent.translate(this.current);
        String text = parsePAPI(null, translate);
        String[] message = text.split("\n");

        for (String line : message) {
            Bukkit.broadcastMessage(line);
        }

        this.current = def;
    }

    /**
     * Statically create a new message instance using {@link Message#of(String...)}
     *
     * @param text The text of the message.
     * @return The message.
     */
    public static Message of(@NotNull String... text) {
        return new Message(String.join("\n", text));
    }

    /**
     * Statically create a new message instance using {@link Message#of(String)}
     *
     * @param text The text of the message.
     * @return The message.
     */
    public static Message of(@NotNull String text) {
        return new Message(text);
    }

    /**
     * Statically create a new message instance using {@link Message#of(List)}
     *
     * @param text The text of the message.
     * @return The message.
     */
    public static Message of(@NotNull List<String> text) {
        return new Message(String.join("\n", text));
    }

    /**
     * Parses the message using PlaceholderAPI.
     *
     * @param sender  The sender to parse the message for.
     * @param message The message to parse.
     * @return The parsed message.
     * @apiNote The sender parameter can be null if you want to parse the message for the console.
     */
    @NotNull
    private String parsePAPI(@Nullable CommandSender sender, @NotNull String message) {
        boolean enabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if (!enabled || !parsePlaceholderAPI) return message;

        return (sender == null ? PlaceholderAPI.setPlaceholders(null, message) : (sender instanceof Player player ? PlaceholderAPI.setPlaceholders(player, message) : PlaceholderAPI.setPlaceholders(null, message)));
    }
}
