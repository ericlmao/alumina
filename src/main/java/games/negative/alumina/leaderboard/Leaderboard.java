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


package games.negative.alumina.leaderboard;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a sorted leaderboard which can be updated and queried.
 * @param <K> Key type which can be used for identification, such as {@link java.util.UUID}!
 * @param <V> Value type which can be used for sorting, such as {@link java.lang.Integer}!
 */
@SuppressWarnings("all")
public abstract class Leaderboard<K, V extends Comparable<V>> {

    public static final Comparator<LeaderboardEntry> HIGHEST_TO_LOWEST = Comparator.<LeaderboardEntry, Comparable>comparing(LeaderboardEntry::value).reversed();
    public static final Comparator<LeaderboardEntry> LOWEST_TO_HIGHEST = Comparator.comparing(LeaderboardEntry::value);

    private final List<K> keys = Lists.newArrayList();
    private List<LeaderboardEntry<K, V>> sorted = Collections.synchronizedList(Lists.newArrayList());

    private final String name;

    private final LeaderboardComparingType comparing;

    /**
     * Creates a new leaderboard with the given name and comparing type.
     * @param name Name of the leaderboard.
     * @param comparing Comparing type of the leaderboard.
     */
    public Leaderboard(@NotNull final String name, @NotNull final LeaderboardComparingType comparing) {
        Preconditions.checkNotNull(name, "Name cannot be null!");
        Preconditions.checkNotNull(comparing, "Comparing cannot be null!");

        this.name = name;
        this.comparing = comparing;
    }

    /**
     * Gets the value of the given key.
     * @param key Key to get the value of.
     * @return Value of the given key.
     */
    public abstract V getValue(@NotNull final K key);

    /**
     * Parses the key to a string.
     * @param key Key to parse.
     * @return Parsed key.
     */
    @NotNull
    public abstract String parseKey(@NotNull final K key);

    /**
     * Parses the value to a string.
     * @param value Value to parse.
     * @return Parsed value.
     */
    @NotNull
    public abstract String parseValue(@NotNull final V value);

    /**
     * Updates the leaderboard without replacing any keys.
     */
    public void update() {
        update(keys, false);
    }

    /**
     * Updates the leaderboard with the given keys.
     * @param keys Keys to update the leaderboard with.
     * @param replace Whether to replace the current keys with the given keys.
     */
    public void update(@NotNull final List<K> keys, final boolean replace) {
        final List<LeaderboardEntry<K, V>> entries = Lists.newArrayList(sorted);

        for (K key : keys) {
            entries.removeIf(e -> e.key().equals(key));
            entries.add(new LeaderboardEntry<>(key, getValue(key), comparing));
        }

        if (replace)
            this.keys.addAll(keys);

        Collections.sort(entries);
        sorted = Collections.synchronizedList(entries);
    }

    /**
     * Gets an entry from the leaderboard using a specialized filter.
     * @param filter Filter to use.
     * @return Optional of the entry.
     */
    @NotNull
    public Optional<LeaderboardEntry<K, V>> getEntry(@NotNull final Predicate<LeaderboardEntry<K, V>> filter) {
        Preconditions.checkNotNull(filter, "Filter cannot be null!");

        return sorted.stream().filter(filter).findFirst();
    }

    /**
     * Gets a list of entries from the leaderboard using a specialized filter.
     * @param filter Filter to use.
     * @return List of entries.
     */
    @NotNull
    public List<LeaderboardEntry<K, V>> getEntries(@NotNull final Predicate<LeaderboardEntry<K, V>> filter) {
        Stream<LeaderboardEntry<K, V>> stream = sorted.stream();

        if (filter != null)
            stream = stream.filter(filter);

        return stream.toList();
    }

    /**
     * Gets a parsed entry from the leaderboard using a specialized filter.
     * @param filter Filter to use.
     * @return Optional of the parsed entry.
     */
    @NotNull
    public Optional<LeaderboardEntry<String, String>> getParsedEntry(@NotNull final Predicate<LeaderboardEntry<K, V>> filter) {
        Preconditions.checkNotNull(filter, "Filter cannot be null!");

        return sorted.stream().filter(filter).map(e -> new LeaderboardEntry<>(parseKey(e.key()), parseValue(e.value()), comparing)).findFirst();
    }

    /**
     * Gets a list of parsed entries from the leaderboard using a specialized filter.
     * @param filter Filter to use.
     * @return List of parsed entries.
     */
    @NotNull
    public List<LeaderboardEntry<String, String>> getParsedEntries(@NotNull final Predicate<LeaderboardEntry<K, V>> filter) {
        Stream<LeaderboardEntry<K, V>> stream = sorted.stream();
        if (filter != null)
            stream = stream.filter(filter);

        return stream.map(e -> new LeaderboardEntry<>(parseKey(e.key()), parseValue(e.value()), comparing)).toList();
    }

    /**
     * Gets the position and other data of the given key.
     * @param key Key to get the position of.
     * @return Positioned leaderboard entry.
     */
    @Nullable
    public PositionedLeaderboardEntry<K, V> getPosition(@NotNull final K key) {
        int position = 1;
        for (LeaderboardEntry<K, V> entry : sorted) {
            if (entry.key().equals(key))
                return new PositionedLeaderboardEntry<>(entry.key(), entry.value(), position);

            position++;
        }

        return null;
    }

    /**
     * Gets the position and other data of the given key.
     * @param index Index to get the position of.
     * @return Positioned leaderboard entry.
     */
    @Nullable
    public PositionedLeaderboardEntry<K, V> getSelectedPosition(@NotNull int index) {
        int position = 1;
        for (LeaderboardEntry<K, V> entry : sorted) {
            if (position == index)
                return new PositionedLeaderboardEntry<>(entry.key(), entry.value(), position);

            position++;
        }
        return null;
    }

    /**
     * Gets the position and other data of the given key.
     * @param index Index to get the position of.
     * @return Positioned leaderboard entry.
     */
    @Nullable
    public PositionedLeaderboardEntry<String, String> getParsedSelectedPosition(@NotNull int index) {
        int position = 1;
        for (LeaderboardEntry<K, V> entry : sorted) {
            if (position == index)
                return new PositionedLeaderboardEntry<>(parseKey(entry.key()), parseValue(entry.value()), position);

            position++;
        }
        return null;
    }

    @NotNull
    public String name() {
        return this.name;
    }
}
