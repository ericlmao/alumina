package games.negative.alumina.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Map;

/**
 * The BukkitCache interface represents a cache implementation in a Bukkit server environment.
 *
 * @param <K> The type of the cache keys.
 * @param <V> The type of the cache values.
 */
public interface BukkitCache<K, V> {

    /**
     * Determines whether the method should be executed asynchronously.
     *
     * @return {@code true} if the method should be executed asynchronously, {@code false} otherwise.
     */
    boolean async();

    /**
     * Retrieves the duration after which entries in the cache will expire.
     *
     * @return The duration after which entries in the cache will expire.
     */
    @NotNull
    Duration expireAfter();

    /**
     * Retrieves the event fired when a value is invalidated from the cache.
     *
     * @return The {@code BukkitCacheInvalidateEvent} if available, {@code null} otherwise.
     */
    @Nullable
    BukkitCacheInvalidateEvent<K, V> onInvalidate();

    /**
     * Inserts a key-value pair into the cache.
     *
     * @param key   The key to associate the value with. Cannot be null.
     * @param value The value to be inserted into the cache. Cannot be null.
     */
    void put(@NotNull K key, @NotNull V value);

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key the key whose associated value is to be retrieved
     * @return the value to which the specified key is mapped, or {@code null} if the key is not present in the cache
     */
    @Nullable
    V get(@NotNull K key);

    /**
     * Removes the value associated with the specified key from the cache.
     *
     * @param key the key whose value should be removed from the cache
     */
    void remove(@NotNull K key);

    /**
     * Clears the cache, removing all key-value pairs.
     */
    void clear();

    /**
     * Returns the number of elements in the cache.
     *
     * @return The size of the cache.
     */
    int size();

    /**
     * Checks if the cache contains an entry with the specified key.
     *
     * @param key The key to check if it exists in the cache. Cannot be null.
     * @return true if the cache contains an entry with the specified key,
     *         false otherwise.
     */
    boolean containsKey(@NotNull K key);

    /**
     * Checks if the cache contains the specified value.
     *
     * @param value the value to be checked for existence in the cache
     * @return true if the value is found in the cache, false otherwise
     * @throws NullPointerException if the specified value is null
     */
    boolean containsValue(@NotNull V value);

    /**
     * Returns the cache content as a Map. The returned map is backed by the cache,
     * so changes to the map are reflected in the cache and vice versa.
     *
     * @return the cache content as a Map
     */
    @NotNull
    Map<K, V> asMap();

}
