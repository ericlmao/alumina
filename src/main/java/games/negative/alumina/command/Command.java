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

package games.negative.alumina.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import games.negative.alumina.command.annotation.CommandInfo;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.alumina.command.task.AsyncCommandRunner;
import games.negative.alumina.message.Message;
import games.negative.alumina.util.MathUtil;
import games.negative.alumina.util.TabCompleteUtil;
import games.negative.alumina.util.Tasks;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@SuppressWarnings("unused")
public abstract class Command extends org.bukkit.command.Command {

    /**
     * The message for when the player does not have permission to use the command.
     */
    private static final Message NO_PERMISSION = new Message("<red>You do not have permission to use this command.");

    /**
     * The message for when a player-only command is used as a console.
     */
    private static final Message CANNOT_USE_AS_CONSOLE = new Message("<red>You cannot use this command as console.");

    /**
     * The message for when a console-only command is used as a player.
     */
    private static final Message CANNOT_USE_AS_PLAYER = new Message("<red>You cannot use this command as a player.");

    /**
     * The message for when a command is used incorrectly.
     */
    private static final Message USAGE = new Message("<click:suggest_command:'/%command% %usage%'><red>Usage: <gray>/%command% %usage%</click>");


    private final List<Command> subCommands;
    private final List<String> params;
    private final Map<String, Function<CommandSender, List<String>>> parameters;
    private final List<String> shortcuts;
    private List<String> subAliases;
    private final boolean playerOnly;
    private final boolean consoleOnly;
    private final boolean smartTabComplete;
    private final boolean async;

    private Command parent;

    public Command() {
        this((Command) null);
    }

    public Command(@Nullable Command parent) {
        super("1");

        this.parent = parent;

        CommandInfo annotation = getClass().getDeclaredAnnotation(CommandInfo.class);
        Preconditions.checkNotNull(annotation, "If using empty constructor, class must have @CommandInfo annotation");

        setName(annotation.name());
        setAliases(Arrays.asList(annotation.aliases()));
        setDescription(annotation.description());

        this.subCommands = Lists.newArrayList();

        setPermission((annotation.permission().isBlank()) ? null : annotation.permission());
        if (!annotation.permission().isBlank()) {
            try {
                Bukkit.getPluginManager().addPermission(new Permission(annotation.permission()));
            } catch (Exception ignored) {
            }
        }

        this.params = Arrays.stream(annotation.params()).collect(Collectors.toCollection(Lists::newArrayList));
        this.shortcuts = Arrays.stream(annotation.shortcuts()).collect(Collectors.toCollection(Lists::newArrayList));
        this.playerOnly = annotation.playerOnly();
        this.consoleOnly = annotation.consoleOnly();
        this.smartTabComplete = annotation.smartTabComplete();
        this.async = annotation.async();
        this.parameters = null; // builder-style only
    }

    public Command(@NotNull CommandBuilder builder) {
        this(builder, null);
    }

    public Command(@NotNull CommandBuilder builder, @Nullable Command parent) {
        super(
                builder.name(),
                Optional.ofNullable(builder.description()).orElse(""),
                Optional.ofNullable(builder.usage()).orElse(""),
                Optional.ofNullable(builder.aliases()).orElse(Lists.newArrayList())
        );

        this.parent = parent;

        this.subCommands = Lists.newArrayList();

        setPermission((builder.permission() == null) ? null : Objects.requireNonNull(builder.permission()).getName());
        if (builder.permission() != null) {
            try {
                Bukkit.getPluginManager().addPermission(Objects.requireNonNull(builder.permission()));
            } catch (Exception ignored) {
            }
        }

        this.shortcuts = Optional.ofNullable(builder.shortcuts()).orElse(Lists.newArrayList());
        this.playerOnly = builder.playerOnly();
        this.consoleOnly = builder.consoleOnly();
        this.smartTabComplete = builder.smartTabComplete();
        this.async = builder.async();
        this.parameters = builder.parameters();
        this.params = Optional.ofNullable(parameters).stream()
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(Lists::newArrayList));
    }

    /**
     * This method is called when the command is executed.
     *
     * @param context The context of the command.
     */
    public abstract void execute(@NotNull CommandContext context);

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
        if (!testPermissionSilent(sender)) {
            NO_PERMISSION.create().send(sender);
            return true;
        }

        if (this.async) {
            Tasks.async(new AsyncCommandRunner(this, sender, args));
            return true;
        }
        return runCommand(sender, args);
    }

    public boolean runCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (checkConsolePlayerCommand(sender) || !checkParams(sender, args) || checkSubCommands(sender, args))
            return true;

        // If all requirements are met, execute the command.
        CommandContext context = new CommandContext(args, sender);
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
    public Command injectSubCommand(@NotNull CommandBuilder properties, @NotNull Consumer<CommandContext> processor) {
        Preconditions.checkNotNull(properties, "Properties cannot be null.");
        Preconditions.checkNotNull(processor, "Processor cannot be null.");

        Command command = new Command(properties, this) {
            @Override
            public void execute(@NotNull CommandContext context) {
                processor.accept(context);
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
        if (subMap.isEmpty()) {
            try {
                String param = getParams().get(placement);
                if (parameters != null) {
                    Function<CommandSender, List<String>> function = parameters.getOrDefault(param, null);
                    if (function == null) return List.of("[<" + param + ">]");

                    List<String> suggestions = function.apply(sender);
                    if (suggestions == null || suggestions.isEmpty()) return List.of("[<" + param + ">]");

                    result.addAll(suggestions);
                } else {
                    result.add("[<" + param + ">]");
                }
            } catch (Exception ignored) {
            }

            return TabCompleteUtil.getSimilarStrings(result, current);
        }

        Collection<Command> commands = subMap.get(placement);
        for (Command command : commands) {
            if (!testPermissionSilent(sender)) continue;

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

            if (cmd == null || !testPermissionSilent(sender)) continue;

            List<String> completion = cmd.onTabComplete(context);
            if (completion != null && !completion.isEmpty()) {
                result.addAll(completion);
                continue;
            }

            int depth = (MathUtil.absDiff(i, placement) - 1);
            try {
                String param = cmd.getParams().get(depth);
                if (cmd.parameters != null) {
                    Function<CommandSender, List<String>> function = cmd.parameters.getOrDefault(param, null);
                    if (function == null) {
                        result.add("[<" + param + ">]");
                        continue;
                    }

                    List<String> suggestions = function.apply(sender);
                    if (suggestions == null || suggestions.isEmpty()) {
                        result.add("[<" + param + ">]");
                        continue;
                    }

                    result.addAll(suggestions);
                    continue;
                }

                result.add("[<" + param + ">]");
            } catch (Exception ignored) {
            }
        }

        return TabCompleteUtil.getSimilarStrings(result, current);
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
            CANNOT_USE_AS_CONSOLE.create().send(sender);
            return true;
        }

        if (sender instanceof Player && consoleOnly) {
            CANNOT_USE_AS_PLAYER.create().send(sender);
            return true;
        }

        return false;
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
        if (subCommand == null) return false;

        return subCommand.execute(sender, begin, snippet);
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

        if (this.params == null || this.params.isEmpty()) return true;

        if (args.length >= params.size()) return true;

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

        USAGE.create().replace("%command%", parentBuilder.toString())
                .replace("%usage%", builder.toString())
                .send(sender);
        return false;
    }

    /**
     * Apply the aliases from the given CommandProperties to the command.
     *
     * @param properties The properties of the command. Must not be null.
     */
    private void applyAliases(@NotNull CommandBuilder properties) {
        List<String> aliases = properties.aliases();
        assert aliases != null; // Checked in the constructor

        if (parent == null) {
            this.setAliases(aliases);
            return;
        }

        this.subAliases = properties.aliases();
    }
}