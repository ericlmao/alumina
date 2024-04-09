package games.negative.alumina.cache;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class BukkitCacheBuilder<K, V> {

    private boolean async = false;
    private Duration expireAfter = Duration.ofMinutes(1);
    private BukkitCacheInvalidateEvent<K, V> invalidationEvent = null;

    /**
     * Set whether the cache operations should be performed asynchronously.
     *
     * @param async {@code true} if the cache operations should be performed asynchronously, {@code false} otherwise
     * @return the updated BukkitCacheBuilder instance
     */
    @NotNull
    @CheckReturnValue
    public BukkitCacheBuilder<K, V> async(boolean async) {
        this.async = async;
        return this;
    }

    /**
     * Sets the duration after which cached entries will expire.
     *
     * @param expireAfter The duration after which cached entries will expire
     * @return The current instance of BukkitCacheBuilder
     */
    @NotNull
    @CheckReturnValue
    public BukkitCacheBuilder<K, V> expireAfter(Duration expireAfter) {
        this.expireAfter = expireAfter;
        return this;
    }

    /**
     * Sets the invalidation event for the cache.
     *
     * @param invalidationEvent the invalidation event to set
     * @return the updated cache builder instance
     */
    @NotNull
    @CheckReturnValue
    public BukkitCacheBuilder<K, V> onInvalidate(BukkitCacheInvalidateEvent<K, V> invalidationEvent) {
        this.invalidationEvent = invalidationEvent;
        return this;
    }

    /**
     * Builds a {@link BukkitCache} object with the specified configuration.
     *
     * @return the built {@link BukkitCache} object
     */
    @NotNull
    public BukkitCache<K, V> build() {
        return new BukkitCacheImpl<>(async, expireAfter, invalidationEvent);
    }

    /**
     * Builder class for creating instances of {@link BukkitCache}.
     * @param <K> the type of keys maintained by the cache
     * @param <V> the type of mapped values in the cache
     */
    @NotNull
    @CheckReturnValue
    public static <K, V> BukkitCacheBuilder<K, V> builder() {
        return new BukkitCacheBuilder<>();
    }

}
