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


package games.negative.alumina.logger;

import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.util.MiniMessageUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * This enum represents different levels of logs: INFO, WARNING, and SEVERE.
 * Each level has a corresponding Java logging Level object.
 */
@RequiredArgsConstructor
public enum Logs {
    /**
     * Represents a log level in the Logs enum. It is used to print log messages with a specific level.
     */
    INFO(Level.INFO),

    /**
     * This variable represents a warning log level in the Logs enum.
     * It is used to print log messages with a warning level.
     */
    WARNING(Level.WARNING),

    /**
     * This constant represents the SEVERE level of logs. The log message at this level indicates a serious error that may prevent the application from functioning correctly.
     */
    SEVERE(Level.SEVERE);

    private final Level level;

    private static boolean disabled = false;

    /**
     * This method is used to print a log message with the given content and log level.
     *
     * @param content The content of the log message.
     * @param force   true to force the log message to be printed, false to print the log message only if the log messages are not disabled
     */
    public void print(@NotNull String content, boolean force) {
        if (disabled && !force) return;

        AluminaPlugin.getAluminaInstance().getLogger().log(level, content);
    }

    /**
     * This method is used to print a log message with the given content.
     * If the log messages are disabled, the log message will not be printed.
     *
     * @param content The content of the log message.
     */
    public void print(@NotNull String content) {
        print(content, false);
    }

    /**
     * Sets the disabled status for log messages.
     *
     * @param disabled true to disable log messages, false to enable log messages
     */
    public static void setDisabled(boolean disabled) {
        Logs.disabled = disabled;
    }

    /**
     * Send a log message with the given content.
     * @param content The content of the log message.
     */
    public static void log(@NotNull String content) {
        INFO.print(content);
    }

    /**
     * Send a log message with the given content and force flag.
     * @param content The content of the log message.
     * @param force true to force the log message to be printed, false to print the log message only if the log messages are not disabled
     */
    public static void log(@NotNull String content, boolean force) {
        INFO.print(content, force);
    }

    /**
     * Send an info log message with the given content.
     * @param content The content of the log message.
     */
    public static void info(@NotNull String content) {
        INFO.print(content);
    }

    /**
     * Send an info log message with the given content and force flag.
     * @param content The content of the log message.
     * @param force true to force the log message to be printed, false to print the log message only if the log messages are not disabled
     */
    public static void info(@NotNull String content, boolean force) {
        INFO.print(content, force);
    }

    /**
     * Send a warning log message with the given content.
     * @param content The content of the log message.
     */
    public static void warning(@NotNull String content) {
        WARNING.print(content);
    }

    /**
     * Send a warning log message with the given content and force flag.
     * @param content The content of the log message.
     * @param force true to force the log message to be printed, false to print the log message only if the log messages are not disabled
     */
    public static void warning(@NotNull String content, boolean force) {
        WARNING.print(content, force);
    }

    /**
     * Send a severe log message with the given content.
     * @param content The content of the log message.
     */
    public static void severe(@NotNull String content) {
        SEVERE.print(content);
    }

    /**
     * Send a severe log message with the given content and force flag.
     * @param content The content of the log message.
     * @param force true to force the log message to be printed, false to print the log message only if the log messages are not disabled
     */
    public static void severe(@NotNull String content, boolean force) {
        SEVERE.print(content, force);
    }

    /**
     * Send an error log message with the given content.
     * @param content The content of the log message.
     */
    public static void error(@NotNull String content) {
        SEVERE.print(content);
    }

    /**
     * Send an error log message with the given content and force flag.
     * @param content The content of the log message.
     * @param force true to force the log message to be printed, false to print the log message only if the log messages are not disabled
     */
    public static void error(@NotNull String content, boolean force) {
        SEVERE.print(content, force);
    }

    /**
     * Broadcast a message to all players.
     * @param content The content of the message.
     * @param force true to force the message to be broadcasted, false to broadcast the message only if the log messages are not disabled
     */
    public static void broadcast(@NotNull String content, boolean force) {
        if (disabled && !force) return;

        Bukkit.broadcast(MiniMessageUtil.translate(content));
    }

    /**
     * Broadcast a message to all players.
     * @param content The content of the message.
     */
    public static void broadcast(@NotNull String content) {
        broadcast(content, false);
    }
}
