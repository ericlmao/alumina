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

package games.negative.alumina;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.alumina.command.structure.AluminaCommand;
import games.negative.alumina.dependency.DependencyLoader;
import games.negative.alumina.dependency.MavenDependency;
import games.negative.alumina.dependency.MavenRepository;
import games.negative.alumina.listener.MenuListener;
import games.negative.alumina.message.color.AluminaColorAgent;
import games.negative.alumina.message.color.ColorAgent;
import games.negative.alumina.util.FileLoader;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * This is considered the main class of any project using alumina.
 */
public abstract class AluminaPlugin extends JavaPlugin {

    private static AluminaPlugin instance;
    private ColorAgent colorAgent;

    /**
     * This method is called when the plugin is initially loaded.
     */
    public abstract void load();

    /**
     * This method is called when the plugin is enabled.
     */
    public abstract void enable();

    /**
     * This method is called when the plugin is disabled.
     */
    public abstract void disable();

    /**
     * This method is used to register a command.
     * You do not need to register commands in the plugin.yml because this method will do it for you.
     * This method will also unregister any existing commands with the same name.
     *
     * @param builder The builder used to create the command.
     */
    public void registerCommand(final CommandBuilder builder) {
        Preconditions.checkNotNull(builder, "Command builder cannot be null!");

        CommandMap commandMap = initCommandMap();
        if (commandMap == null) return;

        String name = builder.getName();

        Command existing = commandMap.getCommand(name);
        if (existing != null) {
            cleanse(name, existing, commandMap);
        }

        AluminaCommand command = builder.build();
        commandMap.register(getName(), command);

        List<AluminaCommand> sub = getRecursiveSubCommand(command);
        if (sub.isEmpty()) return;

        for (AluminaCommand cmd : sub) {
            String[] shortcuts = cmd.getShortcuts();
            if (shortcuts == null) continue;

            for (String shortcut : shortcuts) {
                Command existingShortcut = commandMap.getCommand(shortcut);
                if (existingShortcut != null) {
                    cleanse(shortcut, existingShortcut, commandMap);
                }

                commandMap.register(getName(), cmd);
            }
        }
    }

    /**
     * This method is used to get all subcommands of a command.
     * @param parent The parent command.
     * @return A list of all subcommands.
     */
    private List<AluminaCommand> getRecursiveSubCommand(final AluminaCommand parent) {
        Preconditions.checkNotNull(parent, "Parent command cannot be null!");

        // Recursively get all subcommands of all subcommands.
        List<AluminaCommand> list = Lists.newArrayList(parent.getSubCommands());

        for (AluminaCommand subCommand : parent.getSubCommands()) {
            List<AluminaCommand> recursiveSubCommands = getRecursiveSubCommand(subCommand);
            if (recursiveSubCommands.isEmpty())
                break;

            list.addAll(recursiveSubCommands);
        }

        return list;
    }

    /**
     * This method is used to remove a command from the command map.
     * @param name The name of the command.
     * @param existing The existing command.
     * @param commandMap The command map.
     */
    private void cleanse(final String name, final Command existing, final CommandMap commandMap) {
        Preconditions.checkNotNull(name, "Command name cannot be null!");
        Preconditions.checkNotNull(existing, "Existing command cannot be null!");
        Preconditions.checkNotNull(commandMap, "Command map cannot be null!");

        Map<String, Command> map;
        try {
            map = (Map<String, Command>) commandMap.getClass().getDeclaredMethod("getKnownCommands").invoke(commandMap);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            getLogger().severe("Could not retrieve the command map. (Illegal Access, Invocation Target, No Such Method)");
            return;
        }

        existing.unregister(commandMap);
        map.remove(name);
        existing.getAliases().forEach(map::remove);
    }

    /**
     * This method is used to initialize the command map.
     * @return The command map.
     */
    private CommandMap initCommandMap() {
        Server server = Bukkit.getServer();
        Field field;
        try {
            field = server.getClass().getDeclaredField("commandMap");
        } catch (NoSuchFieldException e) {
            getLogger().severe("Could not retrieve the command map. (No Such Field)");
            return null;
        }

        field.setAccessible(true);

        CommandMap commandMap;
        try {
            commandMap = (CommandMap) field.get(server);
        } catch (IllegalAccessException e) {
            getLogger().severe("Could not retrieve the command map. (Illegal Access)");
            return null;
        }

        return commandMap;
    }
    
    public void registerListeners(final Listener... listeners) {
        Preconditions.checkNotNull(listeners, "Listeners cannot be null!");
        Preconditions.checkArgument(listeners.length > 0, "Listeners cannot be empty!");

        PluginManager manager = Bukkit.getPluginManager();

        for (Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }
    }

    /**
     * This method is used to load a file from the plugin's resources folder.
     * @param name The name of the file to load.
     */
    public void loadFile(final String name) {
        FileLoader.loadFile(this, name);
    }

    /**
     * Loads a dependency into a JavaPlugin using the provided group id, artifact id, version.
     *
     * @param plugin     the JavaPlugin to load the dependency into
     * @param groupId    the group id of the dependency
     * @param artifactId the artifact id of the dependency
     * @param version    the version of the dependency
     */
    public void loadDependency(final JavaPlugin plugin, final String groupId, final String artifactId, final String version) {
        loadDependency(plugin, groupId, artifactId, version, DependencyLoader.CENTRAL.url());
    }

    /**
     * Loads a dependency into a JavaPlugin using the provided group id, artifact id, version, and repository URL.
     *
     * @param plugin     The JavaPlugin to load the dependency into.
     * @param groupId    The group id of the dependency.
     * @param artifactId The artifact id of the dependency.
     * @param version    The version of the dependency.
     * @param repoUrl    The repository URL where the dependency is located.
     * @throws NullPointerException if `plugin`, `groupId`, `artifactId`, `version`, or `repoUrl` is null.
     * @throws RuntimeException if unable to load the dependency.
     */
    public void loadDependency(final JavaPlugin plugin, final String groupId, final String artifactId, final String version, final String repoUrl) {
        Preconditions.checkNotNull(plugin, "'plugin' cannot be null!");
        Preconditions.checkNotNull(groupId, "'groupId' cannot be null!");
        Preconditions.checkNotNull(artifactId, "'artifactId' cannot be null!");
        Preconditions.checkNotNull(version, "'version' cannot be null!");
        Preconditions.checkNotNull(repoUrl, "'repoUrl' cannot be null!");

        DependencyLoader.loadDependency(plugin, new MavenDependency(groupId, artifactId, version, new MavenRepository(repoUrl)));
    }


    @Override
    public void onLoad() {
        instance = this;

        load();
    }

    @Override
    public void onEnable() {
        this.colorAgent = new AluminaColorAgent();
        registerListeners(
                new MenuListener()
        );

        enable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    public static AluminaPlugin getAluminaInstance() {
        return instance;
    }

    public ColorAgent getColorAgent() {
        // Complete null-safety check.
        if (this.colorAgent == null)
            this.colorAgent = new AluminaColorAgent();

        return colorAgent;
    }

    public void setColorAgent(final ColorAgent colorAgent) {
        Preconditions.checkNotNull(colorAgent, "Color Agent cannot be null!");

        this.colorAgent = colorAgent;
    }
}
