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

package games.negative.alumina.dependency;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import games.negative.alumina.AluminaPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * The DependencyLoader class is responsible for dynamically loading dependencies into a Java plugin.
 */
public class DependencyLoader {

    public static final MavenRepository CENTRAL = new MavenRepository("https://repo1.maven.org/maven2");

    @SuppressWarnings("Guava")
    private static final Supplier<URLClassLoaderAccess> URL_INJECTOR = Suppliers.memoize(() -> URLClassLoaderAccess.create((URLClassLoader) AluminaPlugin.getAluminaInstance().getClass().getClassLoader()));

    /**
     * Loads a dependency into a JavaPlugin using the provided group id, artifact id, version.
     *
     * @param plugin     the JavaPlugin to load the dependency into
     * @param groupId    the group id of the dependency
     * @param artifactId the artifact id of the dependency
     * @param version    the version of the dependency
     */
    public static void loadDependency(@NotNull final JavaPlugin plugin, @NotNull final String groupId, @NotNull final String artifactId, @NotNull final String version) {
        Preconditions.checkNotNull(plugin, "'plugin' cannot be null!");
        Preconditions.checkNotNull(groupId, "'groupId' cannot be null!");
        Preconditions.checkNotNull(artifactId, "'artifactId' cannot be null!");
        Preconditions.checkNotNull(version, "'version' cannot be null!");

        loadDependency(plugin, groupId, artifactId, version, CENTRAL.url());
    }

    /**
     * Loads a dependency into a JavaPlugin using the provided group id, artifact id, version, and repository URL.
     *
     * @param plugin     the JavaPlugin to load the dependency into
     * @param groupId    the group id of the dependency
     * @param artifactId the artifact id of the dependency
     * @param version    the version of the dependency
     * @param repoUrl    the repository URL where the dependency is located
     */
    public static void loadDependency(@NotNull final JavaPlugin plugin, @NotNull final String groupId, @NotNull final String artifactId, @NotNull final String version, @NotNull final String repoUrl) {
        Preconditions.checkNotNull(plugin, "'plugin' cannot be null!");
        Preconditions.checkNotNull(groupId, "'groupId' cannot be null!");
        Preconditions.checkNotNull(artifactId, "'artifactId' cannot be null!");
        Preconditions.checkNotNull(version, "'version' cannot be null!");
        Preconditions.checkNotNull(repoUrl, "'repoUrl' cannot be null!");

        loadDependency(plugin, new MavenDependency(groupId, artifactId, version, new MavenRepository(repoUrl)));
    }

    /**
     * Loads a dependency into a JavaPlugin using the provided MavenDependency object.
     *
     * @param plugin     the JavaPlugin to load the dependency into
     * @param dependency the MavenDependency object representing the dependency to load
     * @throws NullPointerException if `plugin` or `dependency` is null
     * @throws RuntimeException     if unable to load the dependency
     */
    public static void loadDependency(@NotNull final JavaPlugin plugin, @NotNull final MavenDependency dependency) {
        Preconditions.checkNotNull(plugin, "'plugin' cannot be null!");
        Preconditions.checkNotNull(dependency, "'dependency' cannot be null!");

        Logger logger = plugin.getLogger();
        logger.info(String.format("Loading dependency %s:%s:%s from %s", dependency.group(), dependency.artifact(), dependency.version(), dependency.repository().url()));

        String name = dependency.artifact() + "-" + dependency.version();

        File storage = new File(plugin.getDataFolder(), "libraries");
        if (!storage.exists()) storage.mkdirs();

        File location = new File(storage, name + ".jar");
        if (!location.exists()) {
            try {
                logger.info("Dependency '" + name + "' is not already in the libraries folder. Attempting to download...");

                URL url = dependency.getURL();

                try (InputStream is = url.openStream()) {
                    Files.copy(is, location.toPath());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.info("Dependency '" + name + "' successfully downloaded.");
        }

        if (!location.exists()) throw new NullPointerException("Unable to download dependency: " + dependency);

        try {
            URL_INJECTOR.get().addURL(location.toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException("Unable to load dependency: " + location, e);
        }

        logger.info("Loaded dependency '" + name + "' successfully.");
    }
}
