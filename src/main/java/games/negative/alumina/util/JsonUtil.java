package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    public static <T> void save(final T instance, final File file, Gson gson) throws IOException {
        Preconditions.checkNotNull(instance, "Instance cannot be null!");
        Preconditions.checkNotNull(file, "File cannot be null!");

        if (gson == null) gson = GSON;

        file.getParentFile().mkdirs();
        if (!file.exists()) file.createNewFile();


        try (Writer writer = new FileWriter(file)) {
            gson.toJson(instance, writer);
        } catch (IOException e) {
            throw new IOException("Failed to save file " + file.getName(), e);
        }
    }

    /**
     * This method is used to save an object to a file.
     * @param instance The object to save.
     * @param file The file to save to.
     * @param <T> The type of the object.
     * @throws IOException If the file could not be saved.
     */
    public static <T> void save(final T instance, final File file) throws IOException {
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
    public static <T> T loadOrCreate(final File file, final Class<T> clazz, final T clean, final Gson gson) throws IOException {
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
    public static <T> T loadOrCreate(final File file, final Class<T> clazz, final T clean) throws IOException {
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
    public static <T> T load(final File file, final Class<T> clazz, Gson gson) throws IOException {
        Preconditions.checkNotNull(file, "File cannot be null!");
        Preconditions.checkNotNull(clazz, "Class cannot be null!");

        if (gson == null) gson = GSON;

        file.getParentFile().mkdirs();
        if (!file.exists()) throw new IOException("File " + file.getName() + " does not exist.");

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            throw new IOException("Failed to load file " + file.getName(), e);
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
    public static <T> T load(final File file, final Class<T> clazz) throws IOException {
        Preconditions.checkNotNull(file, "File cannot be null!");
        Preconditions.checkNotNull(clazz, "Class cannot be null!");

        return load(file, clazz, null);
    }
}
