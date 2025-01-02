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

package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileLoader {

    /**
     * Loads a file from the plugin's resources folder.
     *
     * @param plugin   The plugin to load the file from.
     * @param resource The name of the file to load.
     * @return The loaded file.
     */
    @Nullable
    public static File loadFile(@NotNull final JavaPlugin plugin, @NotNull final String resource) {
        Preconditions.checkNotNull(plugin, "Plugin cannot be null.");
        Preconditions.checkNotNull(resource, "Resource cannot be null.");

        File folder = plugin.getDataFolder();
        if (!folder.exists()) folder.mkdir();

        File resourceFile = new File(folder, resource);
        if (resourceFile.exists()) return resourceFile;

        resourceFile.getParentFile().mkdirs();

        try (InputStream in = plugin.getResource(resource)) {
            Preconditions.checkNotNull(in, "Plugin Resource " + resource + " does not exist.");

            OutputStream out = new FileOutputStream(resourceFile);
            ByteStreams.copy(in, out);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load resource " + resource + ".");
            return null;
        }

        return resourceFile;
    }

    /**
     * Loads a file configuration from the plugin's resources folder.
     * @param plugin The plugin to load the file from.
     * @param resource The name of the file to load.
     * @return The loaded file configuration.
     */
    @Nullable
    public static FileConfiguration loadFileConfiguration(@NotNull final JavaPlugin plugin, @NotNull final String resource) {
        File file = loadFile(plugin, resource);
        if (file == null) return null;

        return YamlConfiguration.loadConfiguration(file);
    }
}
