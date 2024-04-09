package games.negative.alumina.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import games.negative.alumina.util.Tasks;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class BukkitCacheImpl<K, V> implements BukkitCache<K, V> {

    private final Map<K, CachedValue<V>> cache = Maps.newConcurrentMap();
    private final boolean async;
    private final Duration expireAfter;
    private final BukkitCacheInvalidateEvent<K, V> invalidationEvent;

    public BukkitCacheImpl(boolean async, Duration expireAfter, BukkitCacheInvalidateEvent<K, V> invalidationEvent) {
        this.async = async;
        this.expireAfter = expireAfter;
        this.invalidationEvent = invalidationEvent;

        if (async)
            Tasks.async(new CacheValidatorTask(), 0, 1);
        else
            Tasks.run(new CacheValidatorTask(), 0, 1);
    }

    @Override
    public boolean async() {
        return async;
    }

    @Override
    public @NotNull Duration expireAfter() {
        return expireAfter;
    }

    @Override
    public @Nullable BukkitCacheInvalidateEvent<K, V> onInvalidate() {
        return invalidationEvent;
    }

    @Override
    public void put(@NotNull K key, @NotNull V value) {
        if (cache.containsKey(key)) {
            cache.replace(key, new CachedValue<>(value, Instant.now()));
            return;
        }

        cache.put(key, new CachedValue<>(value, Instant.now()));
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        CachedValue<V> cachedValue = cache.get(key);
        if (cachedValue == null) return null;

        return cachedValue.value;
    }

    @Override
    public void remove(@NotNull K key) {
        invalidationEvent.onInvalidation(key, cache.get(key).value);

        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.forEach((key, value) -> invalidationEvent.onInvalidation(key, value.value));

        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(@NotNull V value) {
        return cache.values().stream().anyMatch(cachedValue -> cachedValue.value.equals(value));
    }

    @Override
    public @NotNull Map<K, V> asMap() {
        return Maps.transformValues(cache, CachedValue::value);
    }

    private record CachedValue<V>(V value, Instant timestamp) {
    }

    private class CacheValidatorTask extends BukkitRunnable {

        @Override
        public void run() {
            List<K> toRemove = Lists.newArrayList();
            for (Map.Entry<K, CachedValue<V>> entry : cache.entrySet()) {
                K key = entry.getKey();
                CachedValue<V> value = entry.getValue();

                if (!value.timestamp.isBefore(Instant.now().minus(expireAfter))) continue;

                invalidationEvent.onInvalidation(key, value.value);

                toRemove.add(key);
            }

            toRemove.forEach(cache::remove);
        }
    }
}
