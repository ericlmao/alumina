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

package games.negative.alumina.menu.config;

import com.google.common.base.Preconditions;
import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Load {@link ConfigurableMenu} instances from the 'menus' directory.
 */
public class MenuLoader {

    private final File directory;
    private final PropertiesProcessor processor;

    /**
     * Create a new instance of {@link MenuLoader}.
     * @param plugin The plugin instance. Cannot be null.
     */
    public MenuLoader(@NotNull JavaPlugin plugin) {
        this(plugin, null);
    }

    /**
     * Create a new instance of {@link MenuLoader}.
     * @param plugin The plugin instance. Cannot be null.
     * @param processor The properties processor. Can be null.
     */
    public MenuLoader(@NotNull JavaPlugin plugin, @Nullable PropertiesProcessor processor) {
        Preconditions.checkNotNull(plugin, "'plugin' cannot be null.");

        this.directory = new File(plugin.getDataFolder(), "menus");
        if (!directory.exists()) directory.mkdirs();

        this.processor = processor;
    }

    /**
     * Load a menu from the menus' directory.
     * @param name The name of the menu file.
     * @param clazz The class of the menu.
     * @return The loaded menu.
     * @param <T> The type of the menu.
     */
    @NotNull
    public <T extends ConfigurableMenu> T loadMenu(@NotNull String name, @NotNull Class<T> clazz) {
        File file = new File(directory, name + (name.endsWith(".yml") ? "" : ".yml"));

        YamlConfigurationProperties.Builder<?> builder = YamlConfigurationProperties.newBuilder();
        builder.setNameFormatter(NameFormatters.LOWER_KEBAB_CASE).inputNulls(true);

        if (processor != null) processor.process(builder);

        if (clazz.isAnnotationPresent(Header.class)) {
            Header header = clazz.getAnnotation(Header.class);
            builder.header(header.value());
        }

        if (clazz.isAnnotationPresent(Footer.class)) {
            Footer footer = clazz.getAnnotation(Footer.class);
            builder.footer(footer.value());
        }

        YamlConfigurationStore<T> store = new YamlConfigurationStore<>(clazz, builder.build());
        return store.update(file.toPath());
    }

}
