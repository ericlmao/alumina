package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * This is a class used to reduce boilerplate when working with persistent data.
 */
public class NBTEditor {

    /**
     * This method is used to get a value from a {@link PersistentDataHolder}.
     *
     * @param holder The holder to get the value from.
     * @param key    The key to get the value from.
     * @param type   The type of the value.
     * @param <V>    The type of the value.
     * @param <T>    The type of the holder.
     * @return The value, or null if it does not exist.
     */
    public static <V, T extends PersistentDataHolder> V get(final T holder, final NamespacedKey key, final PersistentDataType<V, V> type) {
        Preconditions.checkNotNull(holder, "'holder' cannot be null!");
        Preconditions.checkNotNull(key, "'key' cannot be null!");
        Preconditions.checkNotNull(type, "'type' cannot be null!");

        PersistentDataContainer container = holder.getPersistentDataContainer();
        if (!container.has(key, type)) return null;

        return container.get(key, type);
    }

    /**
     * This method is used to get a value from a {@link PersistentDataHolder}.
     *
     * @param holder The holder to get the value from.
     * @param key    The key to get the value from.
     * @param type   The type of the value.
     * @param def    The default value to return if the value does not exist.
     * @param <V>    The type of the value.
     * @param <T>    The type of the holder.
     * @return The value, or the default value if it does not exist.
     */
    @NotNull
    public static <V, T extends PersistentDataHolder> V getOrDefault(final T holder, final NamespacedKey key, final PersistentDataType<V, V> type, final V def) {
        Preconditions.checkNotNull(holder, "'holder' cannot be null!");
        Preconditions.checkNotNull(key, "'key' cannot be null!");
        Preconditions.checkNotNull(type, "'type' cannot be null!");
        Preconditions.checkNotNull(def, "'def' cannot be null!");

        V value = get(holder, key, type);
        return value == null ? def : value;
    }

    /**
     * This method is used to remove a value from a {@link PersistentDataHolder}.
     *
     * @param holder The holder to remove the value from.
     * @param key    The key to remove the value from.
     * @param <T>    The type of the holder.
     */
    public static <T extends PersistentDataHolder> void remove(final T holder, final NamespacedKey key) {
        Preconditions.checkNotNull(holder, "'holder' cannot be null!");
        Preconditions.checkNotNull(key, "'key' cannot be null!");

        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.remove(key);
    }

    /**
     * This method is used to set a value to a {@link PersistentDataHolder}.
     *
     * @param holder The holder to set the value to.
     * @param key    The key to set the value to.
     * @param type   The type of the value.
     * @param value  The value to set.
     * @param <V>    The type of the value.
     * @param <T>    The type of the holder.
     */
    public static <V, T extends PersistentDataHolder> void set(final T holder, final NamespacedKey key, final PersistentDataType<V, V> type, final V value) {
        Preconditions.checkNotNull(holder, "'holder' cannot be null!");
        Preconditions.checkNotNull(key, "'key' cannot be null!");
        Preconditions.checkNotNull(type, "'type' cannot be null!");
        Preconditions.checkNotNull(value, "'value' cannot be null!");

        PersistentDataContainer container = holder.getPersistentDataContainer();
        container.set(key, type, value);
    }

    /**
     * This method is used to check if a {@link PersistentDataHolder} has a value.
     *
     * @param holder The holder to check.
     * @param key    The key to check.
     * @param type   The type of the value.
     * @param <V>    The type of the value.
     * @param <T>    The type of the holder.
     * @return If the holder has the value.
     */
    public static <V, T extends PersistentDataHolder> boolean has(final T holder, final NamespacedKey key, final PersistentDataType<V, V> type) {
        Preconditions.checkNotNull(holder, "'holder' cannot be null!");
        Preconditions.checkNotNull(key, "'key' cannot be null!");
        Preconditions.checkNotNull(type, "'type' cannot be null!");

        PersistentDataContainer container = holder.getPersistentDataContainer();
        return container.has(key, type);
    }

    /**
     * This method is used to check if a {@link PersistentDataHolder} has a value.
     *
     * @param holder The holder to check.
     * @param key    The key to check.
     * @param <T>    The type of the holder.
     * @return If the holder has the value.
     */
    public static <T extends PersistentDataHolder> boolean has(final T holder, final NamespacedKey key) {
        Preconditions.checkNotNull(holder, "'holder' cannot be null!");
        Preconditions.checkNotNull(key, "'key' cannot be null!");

        PersistentDataContainer container = holder.getPersistentDataContainer();
        return container.getKeys().contains(key);
    }
}
