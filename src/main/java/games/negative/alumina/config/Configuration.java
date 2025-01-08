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

package games.negative.alumina.config;

import com.google.common.base.Preconditions;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Function;

/**
 * Represents a configuration file that can be loaded, reloaded and saved.
 * @param <T> the type of the configuration object
 */
public class Configuration<T> {

    private final File file;
    private final Class<T> clazz;
    private final Function<YamlConfigurationProperties.Builder<?>, YamlConfigurationProperties.Builder<?>> propertiesFunction;

    private T object;
    private YamlConfigurationStore<T> store;

    /**
     * Creates a new configuration object.
     * @param file the file to load the configuration from
     * @param clazz Class type of the configuration object
     * @param propertiesFunction a function to configure the properties of the configuration
     */
    public Configuration(@NotNull File file, @NotNull Class<T> clazz, @Nullable Function<YamlConfigurationProperties.Builder<?>, YamlConfigurationProperties.Builder<?>> propertiesFunction) {
        this.file = file;
        this.clazz = clazz;
        this.propertiesFunction = propertiesFunction;

        reload();
    }

    /**
     * Reloads the configuration from the file.
     */
    public void reload() {
        if (this.store == null) {
            YamlConfigurationProperties.Builder<?> builder = YamlConfigurationProperties.newBuilder();
            builder = propertiesFunction.apply(builder);

            this.store = new YamlConfigurationStore<>(clazz, builder.build());
        }

        this.object = store.update(file.toPath());
    }

    /**
     * Saves the configuration to the file.
     */
    public void save() {
        store.save(object, file.toPath());
        reload();
    }

    /**
     * Returns the configuration object.
     * @return the configuration object
     */
    @NotNull
    public T get() {
        // throw exception if object is actually null
        Preconditions.checkNotNull(object, "Configuration object for \"%s\" has not been set yet.".formatted(file.getName()));

        return object;
    }

    /**
     * Creates a new configuration object.
     * @param file the file to load the configuration from
     * @param clazz Class type of the configuration object
     * @return a new configuration object
     * @param <A> the type of the configuration object
     */
    @NotNull
    public static <A> Configuration<A> config(@NotNull File file, @NotNull Class<A> clazz) {
        return new Configuration<>(file, clazz, null);
    }

    /**
     * Creates a new configuration object.
     * @param file the file to load the configuration from
     * @param clazz Class type of the configuration object
     * @param propertiesFunction a function to configure the properties of the configuration
     * @return a new configuration object
     * @param <A> the type of the configuration object
     */
    @NotNull
    public static <A> Configuration<A> config(@NotNull File file, @NotNull Class<A> clazz, @Nullable Function<YamlConfigurationProperties.Builder<?>, YamlConfigurationProperties.Builder<?>> propertiesFunction) {
        return new Configuration<>(file, clazz, propertiesFunction);
    }
}
