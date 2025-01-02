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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * This class is used to convert objects to and from json.
 */
public class JsonUtil {

    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    /**
     * This method is used to save an object to a file.
     * @param instance The object to save.
     * @param file The file to save to.
     * @param gson The gson instance to use.
     * @param <T> The type of the object.
     * @throws IOException If the file could not be saved.
     */
    public static <T> void save(@NotNull T instance, @NotNull File file, @Nullable Gson gson) throws IOException {
        Preconditions.checkNotNull(instance, "Instance cannot be null!");
        Preconditions.checkNotNull(file, "File cannot be null!");

        if (gson == null) gson = GSON;

        file.getParentFile().mkdirs();
        if (!file.exists()) file.createNewFile();

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(instance, writer);
        }
    }

    /**
     * This method is used to save an object to a file.
     * @param instance The object to save.
     * @param file The file to save to.
     * @param <T> The type of the object.
     * @throws IOException If the file could not be saved.
     */
    public static <T> void save(@NotNull T instance, @NotNull File file) throws IOException {
        Preconditions.checkNotNull(instance, "Instance cannot be null!");
        Preconditions.checkNotNull(file, "File cannot be null!");

        save(instance, file, null);
    }

    /**
     * This method is used to load an object from a file.
     * If the file does not exist, it will be created with pre-defined values ({@param clean})
     * @param file The file to load from.
     * @param clazz The class of the object.
     * @param clean The clean instance of the object.
     * @return The loaded object.
     * @param <T> The type of the object.
     * @throws IOException If the file could not be loaded.
     */
    public static <T> T loadOrCreate(@NotNull File file, @NotNull Class<T> clazz, @NotNull T clean, @Nullable Gson gson) throws IOException {
        Preconditions.checkNotNull(file, "File cannot be null!");
        Preconditions.checkNotNull(clazz, "Class cannot be null!");
        Preconditions.checkNotNull(clean, "Clean instance cannot be null!");

        if (!file.exists()) {
            save(clean, file, gson);
            return clean;
        }

        return load(file, clazz, gson);
    }

    /**
     * This method is used to load an object from a file.
     * If the file does not exist, it will be created with pre-defined values ({@param clean})
     * @param file The file to load from.
     * @param clazz The class of the object.
     * @param clean The clean instance of the object.
     * @return The loaded object.
     * @param <T> The type of the object.
     * @throws IOException If the file could not be loaded.
     */
    public static <T> T loadOrCreate(@NotNull File file, @NotNull Class<T> clazz, @NotNull T clean) throws IOException {
        Preconditions.checkNotNull(file, "File cannot be null!");
        Preconditions.checkNotNull(clazz, "Class cannot be null!");
        Preconditions.checkNotNull(clean, "Clean instance cannot be null!");

        return loadOrCreate(file, clazz, clean, null);
    }

    /**
     * This method is used to load an object from a file.
     * @param file The file to load from.
     * @param clazz The class of the object.
     * @param gson The gson instance to use.
     * @return The loaded object.
     * @param <T> The type of the object.
     * @throws IOException If the file could not be loaded.
     */
    public static <T> T load(@NotNull File file, @NotNull Class<T> clazz, @Nullable Gson gson) throws IOException {
        Preconditions.checkNotNull(file, "File cannot be null!");
        Preconditions.checkNotNull(clazz, "Class cannot be null!");

        if (gson == null) gson = GSON;

        file.getParentFile().mkdirs();
        Preconditions.checkArgument(file.exists(), "File " + file.getName() + " does not exist.");

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, clazz);
        }
    }

    /**
     * This method is used to load an object from a file.
     * @param file The file to load from.
     * @param clazz The class of the object.
     * @return The loaded object.
     * @param <T> The type of the object.
     * @throws IOException If the file could not be loaded.
     */
    public static <T> T load(@NotNull File file, @NotNull Class<T> clazz) throws IOException {
        Preconditions.checkNotNull(file, "File cannot be null!");
        Preconditions.checkNotNull(clazz, "Class cannot be null!");

        return load(file, clazz, null);
    }
}
