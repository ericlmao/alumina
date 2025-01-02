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

package games.negative.alumina.command.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CommandBuilder {

    private String name;
    private List<String> aliases = null;
    private String description = null;
    private String usage = null;
    private Permission permission = null;
    private Map<String, Function<CommandSender, List<String>>> parameters = null;
    private List<String> shortcuts = null;
    private boolean playerOnly = false;
    private boolean consoleOnly = false;
    private boolean smartTabComplete = false;
    private boolean async = false;

    /**
     * Statically create a new CommandBuilder
     * @return a new CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public static CommandBuilder builder() {
        return new CommandBuilder();
    }

    /**
     * Get the name of the command
     * @return the name of the command
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * Set the name of the command
     * @param name the name of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder name(@NotNull String name) {
        Preconditions.checkNotNull(name, "name cannot be null");

        this.name = name;
        return this;
    }

    /**
     * Get the aliases of the command
     * @return the aliases of the command
     */
    @Nullable
    public List<String> aliases() {
        return aliases;
    }

    /**
     * Set the aliases of the command
     * @param aliases the aliases of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder aliases(@NotNull List<String> aliases) {
        Preconditions.checkNotNull(aliases, "aliases cannot be null");

        this.aliases = aliases;
        return this;
    }

    /**
     * Set the aliases of the command
     * @param aliases the aliases of the command
     * @return the CommandBuilder
     */
    public CommandBuilder aliases(@NotNull String ... aliases) {
        Preconditions.checkNotNull(aliases, "aliases cannot be null");

        this.aliases = List.of(aliases);
        return this;
    }

    /**
     * Get the description of the command
     * @return the description of the command
     */
    @Nullable
    public String description() {
        return description;
    }

    /**
     * Set the description of the command
     * @param description the description of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder description(@NotNull String description) {
        Preconditions.checkNotNull(description, "description cannot be null");

        this.description = description;
        return this;
    }

    /**
     * Get the usage of the command
     * @return the usage of the command
     */
    @Nullable
    public String usage() {
        return usage;
    }

    /**
     * Set the usage of the command
     * @param usage the usage of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder usage(@NotNull String usage) {
        Preconditions.checkNotNull(usage, "usage cannot be null");

        this.usage = usage;
        return this;
    }

    /**
     * Get the required permissions of the command
     * @return the required permissions of the command
     */
    @Nullable
    public Permission permission() {
        return permission;
    }

    /**
     * Set the permission of the command
     * @param permission the permissions of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder permission(@NotNull Permission permission) {
        Preconditions.checkNotNull(permission, "permissions cannot be null");

        this.permission = permission;
        return this;
    }

    /**
     * Set the permissions of the command
     * @param permission the permissions of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder permission(@NotNull String permission) {
        Preconditions.checkNotNull(permission, "permissions cannot be null");

        this.permission = new Permission(permission);
        return this;
    }

    /**
     * Get the shortcuts of the command
     * @return the shortcuts of the command
     */
    @Nullable
    public List<String> shortcuts() {
        return shortcuts;
    }

    /**
     * Set the shortcuts of the command
     * @param shortcuts the shortcuts of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder shortcuts(@NotNull List<String> shortcuts) {
        Preconditions.checkNotNull(shortcuts, "shortcuts cannot be null");

        this.shortcuts = shortcuts;
        return this;
    }

    /**
     * Set the shortcuts of the command
     * @param shortcuts the shortcuts of the command
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder shortcuts(@NotNull String... shortcuts) {
        Preconditions.checkNotNull(shortcuts, "shortcuts cannot be null");

        this.shortcuts = List.of(shortcuts);
        return this;
    }

    /**
     * Get whether the command is player only
     * @return whether the command is player only
     */
    public boolean playerOnly() {
        return playerOnly;
    }

    /**
     * Set whether the command is player only
     * @param playerOnly whether the command is player only
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder playerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
        return this;
    }

    /**
     * Get whether the command is console only
     * @return whether the command is console only
     */
    public boolean consoleOnly() {
        return consoleOnly;
    }

    /**
     * Set whether the command is console only
     * @param consoleOnly whether the command is console only
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder consoleOnly(boolean consoleOnly) {
        this.consoleOnly = consoleOnly;
        return this;
    }

    /**
     * Get whether the command has smart tab complete
     * @return whether the command has smart tab complete
     */
    public boolean smartTabComplete() {
        return smartTabComplete;
    }

    /**
     * Set whether the command has smart tab complete
     * @param smartTabComplete whether the command has smart tab complete
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder smartTabComplete(boolean smartTabComplete) {
        this.smartTabComplete = smartTabComplete;
        return this;
    }

    /**
     * Get whether the command execution should be asynchronous
     * @return whether the command execution should be asynchronous
     */
    public boolean async() {
        return async;
    }

    /**
     * Set whether the command execution should be asynchronous
     * @param async whether the command execution should be asynchronous
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder async(boolean async) {
        this.async = async;
        return this;
    }

    /**
     * Get the parameters of the command
     * @return the parameters of the command
     */
    @Nullable
    public Map<String, Function<CommandSender, List<String>>> parameters() {
        return parameters;
    }

    /**
     * Add a required parameter to the command
     * @param name the name of the parameter
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder parameter(@NotNull String name) {
        return parameter(name, null);
    }

    /**
     * Add a required parameter to the command
     * @param name the name of the parameter
     * @param suggestions the suggestions of the parameter
     * @return the CommandBuilder
     */
    @NotNull
    @CheckReturnValue
    public CommandBuilder parameter(@NotNull String name, @Nullable Function<CommandSender, List<String>> suggestions) {
        Preconditions.checkNotNull(name, "name cannot be null");

        // Init map if null
        if (parameters == null) parameters = Maps.newLinkedHashMap();

        parameters.put(name, suggestions);
        return this;
    }


}
