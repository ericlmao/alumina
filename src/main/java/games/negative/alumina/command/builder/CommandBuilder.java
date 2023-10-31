/*
 *  MIT License
 *
 * Copyright (C) 2023 Negative Games
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
import com.google.common.collect.Lists;
import games.negative.alumina.command.Command;
import games.negative.alumina.command.structure.AluminaCommand;
import lombok.Getter;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * This class represents a command builder, which is used to build commands.
 */
@Getter
public class CommandBuilder {

    // Command Component
    private final Command component;

    // Command Settings
    private String name;

    private String description;

    private String usage;

    private List<String> aliases;

    private boolean playerOnly;

    private Permission[] permissions;

    private String[] params;

    private String[] shortcuts;
    private boolean smartTabComplete;

    private final List<CommandBuilder> subCommands;

    /**
     * Creates a new command builder.
     * @param component The command component.
     */
    public CommandBuilder(@NotNull Command component) {
        this.component = component;
        this.subCommands = Lists.newArrayList();
        this.aliases = Lists.newArrayList();
        this.usage = "";
        this.description = "";
        this.smartTabComplete = false;
    }

    /**
     * Set the name of the command.
     * @param name The name of the command.
     * @return The command builder.
     */
    public CommandBuilder name(@NotNull String name) {
        Preconditions.checkNotNull(name, "Command name cannot be null.");
        this.name = name;
        return this;
    }

    /**
     * Set the description of the command.
     * @param description The description of the command.
     * @return The command builder.
     */
    public CommandBuilder description(@NotNull String description) {
        Preconditions.checkNotNull(description, "Command description cannot be null.");
        this.description = description;
        return this;
    }

    /**
     * Set the usage message of the command.
     * @param usage The usage of the command.
     * @return The command builder.
     */
    public CommandBuilder usage(@NotNull String usage) {
        Preconditions.checkNotNull(usage, "Command usage message cannot be null.");
        this.usage = usage;
        return this;
    }

    /**
     * Set the aliases of the command.
     * @param aliases The aliases of the command.
     * @return The command builder.
     */
    public CommandBuilder aliases(@NotNull String... aliases) {
        Preconditions.checkNotNull(aliases, "Command aliases cannot be null.");
        return aliases(Lists.newArrayList(aliases));
    }

    /**
     * Set the aliases of the command.
     * @param aliases The aliases of the command.
     * @return The command builder.
     */
    public CommandBuilder aliases(@NotNull List<String> aliases) {
        Preconditions.checkNotNull(aliases, "Command aliases cannot be null.");
        this.aliases = aliases;
        return this;
    }

    /**
     * Set the permissions of the command.
     * @param permissions The permissions of the command.
     * @return The command builder.
     */
    public CommandBuilder permission(@NotNull Permission... permissions) {
        Preconditions.checkNotNull(permissions, "Command permissions cannot be null.");
        this.permissions = permissions;
        return this;
    }

    /**
     * Set the permissions of the command.
     * @param permissions The permissions of the command.
     * @return The command builder.
     */
    public CommandBuilder permission(@NotNull String... permissions) {
        Preconditions.checkNotNull(permissions, "Command permissions cannot be null.");
        this.permissions = Arrays.stream(permissions).map(Permission::new).toArray(Permission[]::new);
        return this;
    }

    /**
     * Set the permission of the command.
     * @param permission The permission of the command.
     * @return The command builder.
     */
    public CommandBuilder permission(@NotNull Permission permission) {
        Preconditions.checkNotNull(permission, "Command permission cannot be null.");
        this.permissions = new Permission[] { permission };
        return this;
    }

    /**
     * Set the permission of the command.
     * @param permission The permission of the command.
     * @return The command builder.
     */
    public CommandBuilder permission(@NotNull String permission) {
        return this.permission(new Permission(permission));
    }

    /**
     * Set the permissions of the command.
     * @param permission The permissions of the command.
     * @return The command builder.
     */
    public CommandBuilder permission(@NotNull List<Permission> permission) {
        Preconditions.checkNotNull(permission, "Command permission cannot be null.");
        this.permissions = permission.toArray(new Permission[0]);
        return this;
    }

    /**
     * Set the parameters of the command.
     * @param params The parameters of the command.
     * @return The command builder.
     */
    public CommandBuilder params(@NotNull String... params) {
        Preconditions.checkNotNull(params, "Command params cannot be null.");
        this.params = params;
        return this;
    }

    /**
     * Set the shortcuts of the command.
     * @param shortcuts The shortcuts of the command.
     * @return The command builder.
     */
    public CommandBuilder shortcuts(@NotNull String... shortcuts) {
        Preconditions.checkNotNull(shortcuts, "Command shortcuts cannot be null.");
        this.shortcuts = shortcuts;
        return this;
    }

    /**
     * Set the state of the command to "player only", meaning
     * the command can only be executed by players.
     *
     * @return The command builder.
     */
    public CommandBuilder playerOnly() {
        this.playerOnly = true;
        return this;
    }

    public CommandBuilder subcommands(CommandBuilder... commands) {
        Preconditions.checkNotNull(commands, "Command subcommands cannot be null.");
        subCommands.addAll(Arrays.asList(commands));
        return this;
    }

    /**
     * Set the state of the command to "smart tab complete", meaning
     * the command will automatically tab complete the sub commands.
     * @return The command builder.
     */
    public CommandBuilder smartTabComplete() {
        return smartTabComplete(true);
    }

    /**
     * Set the state of the command to "smart tab complete", meaning
     * the command will automatically tab complete the sub commands.
     * @param value The value of the smart tab complete.
     * @return The command builder.
     */
    public CommandBuilder smartTabComplete(boolean value) {
        this.smartTabComplete = value;
        return this;
    }

    /**
     * Set the parameters of the command.
     * @return The command builder.
     */
    public AluminaCommand build() {
        return build(null);
    }

    /**
     * Set the parameters of the command.
     * @param parent The parent command.
     * @return The command builder.
     */
    public AluminaCommand build(@Nullable AluminaCommand parent) {
        return new AluminaCommand(parent, this);
    }

}
