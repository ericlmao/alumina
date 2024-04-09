package games.negative.alumina.cache;

@FunctionalInterface
public interface BukkitCacheInvalidateEvent<K, V> {

    /**
     * Called when a value is invalidated from the cache.
     * @param key Key of the value
     * @param value Value that was invalidated
     */
    void onInvalidation(K key, V value);

}
