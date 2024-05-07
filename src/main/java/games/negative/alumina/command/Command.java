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

package games.negative.alumina.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import games.negative.alumina.message.Message;
import games.negative.alumina.util.MathUtil;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@SuppressWarnings("unused")
public abstract class Command extends org.bukkit.command.Command {

    /**
     * The message for when the player does not have permission to use the command.
     */
    private static final Message NO_PERMISSION = Message.of("<red>You do not have permission to use this command.");

    /**
     * The message for when a player-only command is used as a console.
     */
    private static final Message CANNOT_USE_AS_CONSOLE = Message.of("<red>You cannot use this command as console.");

    /**
     * The message for when a console-only command is used as a player.
     */
    private static final Message CANNOT_USE_AS_PLAYER = Message.of("<red>You cannot use this command as a player.");

    /**
     * The message for when a command is used incorrectly.
     */
    private static final Message USAGE = Message.of("<click:suggest_command:'/%command% %usage%'><red>Usage: <gray>/%command% %usage%</click>");

    /**
     * The sub commands of this command.
     */
    private final List<Command> subCommands;

    /**
     * The permissions of this command.
     */
    private final List<Permission> permissions;

    /**
     * The required parameters of this command.
     */
    private final List<String> params;

    /**
     * The shortcuts of this command.
     */
    private final List<String> shortcuts;

    /**
     * The aliases of this command. Only applicable for subcommands
     */
    private List<String> subAliases;

    /**
     * Whether this command is player-only.
     */
    private final boolean playerOnly;

    /**
     * Whether this command is console-only.
     */
    private final boolean consoleOnly;

    /**
     * Whether this command should use smart tab completion.
     */
    private final boolean smartTabComplete;

    /**
     * The parent command of this command.
     */
    private Command parent;

    /**
     * Constructs a new Command object with the given CommandProperties and optional parent Command.
     *
     * @param properties The properties of the command. Must not be null.
     */
    public Command(@NotNull CommandProperties properties) {
        this(properties, null);
    }

    /**
     * The Command class represents a command with specific properties.
     */
    public Command(@NotNull CommandProperties properties, @Nullable Command parent) {
        super(properties.name());
        this.parent = parent;
        this.subCommands = Lists.newArrayList();
        this.permissions = properties.permissions();
        this.params = properties.params();
        this.shortcuts = properties.shortcuts();
        this.playerOnly = properties.playerOnly();
        this.consoleOnly = properties.consoleOnly();
        this.smartTabComplete = properties.smartTabComplete();

        if (properties.aliases() != null)
            applyAliases(properties);

        if (properties.description() != null)
            this.setDescription(properties.description());

        if (properties.usage() != null)
            this.setUsage(properties.usage());
    }

    /**
     * This method is called when the command is executed.
     *
     * @param context The context of the command.
     */
    public abstract void execute(@NotNull Context context);

    /**
     * Executes the command.
     *
     * @param sender       The command sender.
     * @param commandLabel The label of the command.
     * @param args         The arguments of the command.
     * @return True if the command is executed successfully, false otherwise.
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // Long if-check, here's the breakdown:
        // 1. Check if the console is using a player-only command,
        //    or if the player is using a console-only command.
        // 2. Check if the sender has the required permissions.
        // 3. Check if there are any required parameters to be filled in.
        // 4. Check if there are any subcommands to be executed before parent commands are executed.
        if (checkConsolePlayerCommand(sender) || hasInvalidPermissions(sender, true) || !checkParams(sender, args) || checkSubCommands(sender, args))
            return true;

        // If all requirements are met, execute the command.
        Context context = new Context(args, sender);
        execute(context);
        return true;
    }

    /**
     * Adds a subcommand to the command.
     *
     * @param command The subcommand to be added.
     * @return The modified command with the added subcommand.
     * @throws NullPointerException if the subcommand is null.
     */
    public Command addSubCommand(@NotNull Command command) {
        Preconditions.checkNotNull(command, "Command cannot be null.");

        command.parent = this;

        subCommands.add(command);
        return this;
    }

    /**
     * Injects a subcommand with the given properties and context processor into the command.
     *
     * @param properties The properties of the subcommand.
     * @param processor  The context processor for the subcommand.
     * @return The modified command with the injected subcommand.
     * @throws NullPointerException if either properties or processor is null.
     */
    public Command injectSubCommand(@NotNull CommandProperties properties, @NotNull ContextProcessor processor) {
        Preconditions.checkNotNull(properties, "Properties cannot be null.");
        Preconditions.checkNotNull(processor, "Processor cannot be null.");

        Command command = new Command(properties, this) {
            @Override
            public void execute(@NotNull Context context) {
                processor.process(context);
            }
        };

        return addSubCommand(command);
    }

    /**
     * Injects a sub command into the current command.
     *
     * @param properties       The properties of the sub command.
     * @param contextProcessor The context processor for the sub command.
     * @param tabProcessor     The tab completion processor for the sub command.
     * @return The modified command with the injected sub command.
     * @throws NullPointerException if any of the parameters are null.
     */
    public Command injectSubCommand(@NotNull CommandProperties properties, @NotNull ContextProcessor contextProcessor, @NotNull TabCompleteProcessor tabProcessor) {
        Preconditions.checkNotNull(properties, "Properties cannot be null.");
        Preconditions.checkNotNull(contextProcessor, "Context processor cannot be null.");
        Preconditions.checkNotNull(tabProcessor, "Tab processor cannot be null.");

        Command command = new Command(properties, this) {
            @Override
            public void execute(@NotNull Context context) {
                contextProcessor.process(context);
            }

            @Override
            public List<String> onTabComplete(@NotNull TabContext context) {
                return tabProcessor.onTabComplete(context);
            }
        };

        return addSubCommand(command);
    }

    /**
     * This method is called when the command is tab completed.
     *
     * @param context The context of the command.
     * @return a list of possible completions for the command.
     * @apiNote This method is optional, override to add proper functionality.
     */
    public List<String> onTabComplete(@NotNull TabContext context) {
        return null;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        TabContext context = new TabContext(sender, args);
        List<String> completions = onTabComplete(context);
        if (completions != null) return completions;

        if (!smartTabComplete) return super.tabComplete(sender, alias, args);

        String current = context.current();
        int placement = args.length - 1;

        List<String> result = Lists.newArrayList();

        Multimap<Integer, Command> subMap = getRecursive(this, 0);
        if (subMap.isEmpty()) return super.tabComplete(sender, alias, args);

        Collection<Command> commands = subMap.get(placement);
        for (Command command : commands) {
            if (hasInvalidPermissions(sender, false)) continue;

            List<String> match = Lists.newArrayList(command.getName());
            match.addAll(command.getAliases());
            if (command.parent != null && command.subAliases != null) match.addAll(command.subAliases);

            List<String> matched = match.stream().filter(entry -> entry.toLowerCase().contains(current)).toList();
            result.addAll(matched);
        }

        if (!result.isEmpty()) return result;

        for (int i = args.length - 1; i >= 0; i--) {
            if (i == placement) continue;

            String arg = args[i];
            if (arg.isEmpty()) continue;

            Command cmd = subMap.get(i).stream()
                    .filter(command -> command.getName().equalsIgnoreCase(arg) || command.getAliases().contains(arg.toLowerCase()) || (command.subAliases != null && command.subAliases.contains(arg.toLowerCase())))
                    .findFirst().orElse(null);

            if (cmd == null || hasInvalidPermissions(sender, false)) continue;

            List<String> completion = cmd.onTabComplete(context);
            if (completion != null && !completion.isEmpty()) {
                result.addAll(completion);
                continue;
            }

            int depth = (MathUtil.absDiff(i, placement) - 1);
            try {
                String param = cmd.getParams().get(depth);
                result.add("[<" + param + ">]");
            } catch (Exception ignored) {
            }
        }

        return result;
    }

    /**
     * Retrieves all the recursive subcommands of a parent command up to a specified depth.
     *
     * @param parent The parent command. Must not be null.
     * @param depth  The maximum depth to retrieve the subcommands. Must be a positive integer.
     * @return A Multimap containing the subcommands at each depth level under the parent command.
     */
    private Multimap<Integer, Command> getRecursive(@NotNull final Command parent, final int depth) {
        Preconditions.checkNotNull(parent, "Parent command cannot be null.");

        Multimap<Integer, Command> map = ArrayListMultimap.create();

        List<Command> list = parent.getSubCommands();
        for (Command cmd : list) {
            int next = depth + 1;
            map.put(depth, cmd);

            Multimap<Integer, Command> recursive = getRecursive(cmd, next);
            if (recursive.isEmpty()) continue;

            map.putAll(recursive);
        }

        return map;
    }


    /**
     * Checks if the console is using a player-only command
     *
     * @param sender The sender
     * @return Whether the console is using a player-only command
     */
    private boolean checkConsolePlayerCommand(@NotNull final CommandSender sender) {
        Preconditions.checkNotNull(sender, "Sender cannot be null.");

        if (!(sender instanceof Player) && playerOnly) {
            CANNOT_USE_AS_CONSOLE.send(sender);
            return true;
        }

        if (sender instanceof Player && consoleOnly) {
            CANNOT_USE_AS_PLAYER.send(sender);
            return true;
        }

        return false;
    }

    /**
     * Checks the permissions of a command sender.
     *
     * @param sender  The command sender.
     * @param message Flag indicating whether to send a no permission message to the sender.
     * @return True if the sender has the required permissions, false otherwise.
     */
    private boolean hasInvalidPermissions(@NotNull final CommandSender sender, final boolean message) {
        Preconditions.checkNotNull(sender, "Sender cannot be null.");

        if (this.permissions == null) return false;

        for (Permission permission : this.permissions) {
            if (sender.hasPermission(permission))
                return false;
        }

        if (message) NO_PERMISSION.send(sender);
        return true;
    }

    /**
     * Checks the subcommands based on the provided arguments and executes the corresponding subcommand.
     *
     * @param sender The command sender. Must not be null.
     * @param args   The arguments of the command. Must not be null.
     * @return True if a subcommand is executed successfully, false otherwise.
     */
    private boolean checkSubCommands(@NotNull final CommandSender sender, @NotNull final String[] args) {
        Preconditions.checkNotNull(sender, "Sender cannot be null.");
        Preconditions.checkNotNull(args, "Arguments cannot be null.");

        if (args.length == 0 || subCommands.isEmpty()) return false;

        String begin = args[0];
        String[] snippet = Arrays.copyOfRange(args, 1, args.length);

        Command subCommand = getAvailableSubCommand(begin);
        return (subCommand != null && subCommand.execute(sender, begin, snippet));
    }

    /**
     * Retrieves the available subcommand based on the given argument.
     *
     * @param argument The argument to check for the available subcommand. Must not be null.
     * @return The available subcommand that matches the argument, or null if no subcommand is found.
     * @throws NullPointerException if the argument is null.
     */
    public Command getAvailableSubCommand(@NotNull final String argument) {
        Preconditions.checkNotNull(argument, "Argument cannot be null.");

        for (Command subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(argument) || subCommand.getAliases().contains(argument.toLowerCase()) || (subAliases != null && subAliases.contains(argument.toLowerCase())))
                return subCommand;
        }
        return null;
    }

    /**
     * Checks if the given CommandSender and String array of arguments meet the parameters required for the command.
     *
     * @param sender The CommandSender executing the command. Must not be null.
     * @param args   The arguments of the command. Must not be null.
     * @return True if the parameters are valid, false otherwise.
     * @throws NullPointerException if either sender or args is null.
     */
    public boolean checkParams(@NotNull final CommandSender sender, @NotNull final String[] args) {
        Preconditions.checkNotNull(sender, "Sender cannot be null.");
        Preconditions.checkNotNull(args, "Arguments cannot be null.");

        if (this.params == null)
            return true;

        if (args.length < params.size()) {
            StringBuilder builder = new StringBuilder();
            for (String param : params)
                builder.append("<").append(param).append(">").append(" ");

            List<String> parentNames = Lists.newArrayList();
            parentNames.add(getName());

            Command search = this;
            while (search.getParent() != null) {
                parentNames.add(search.getParent().getName());
                search = search.getParent();
            }

            Collections.reverse(parentNames);

            StringBuilder parentBuilder = new StringBuilder();

            int iteration = 0;
            for (String parentName : parentNames) {
                if (iteration != 0)
                    parentBuilder.append(" ");

                parentBuilder.append(parentName);

                iteration++;
            }

            USAGE.send(sender, "%command%", parentBuilder.toString(), "%usage%", builder.toString());
            return false;
        }

        return true;
    }

    /**
     * Apply the aliases from the given CommandProperties to the command.
     *
     * @param properties The properties of the command. Must not be null.
     */
    private void applyAliases(@NotNull CommandProperties properties) {
        List<String> aliases = properties.aliases();
        assert aliases != null; // Checked in the constructor

        if (parent == null) {
            this.setAliases(aliases);
            return;
        }

        this.subAliases = properties.aliases();
    }
}
