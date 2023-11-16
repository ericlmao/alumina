package games.negative.alumina.leaderboard;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public record LeaderboardEntry<K, V extends Comparable<V>>(K key, V value, LeaderboardComparingType comparing) implements Comparable<LeaderboardEntry<K, V>> {

    public LeaderboardEntry {
        Preconditions.checkNotNull(key, "Key cannot be null!");
        Preconditions.checkNotNull(value, "Value cannot be null!");
    }

    @Override
    public int compareTo(@NotNull LeaderboardEntry<K, V> o) {
        Preconditions.checkNotNull(o, "Cannot compare to null!");

        switch (comparing) {

            case HIGHEST_TO_LOWEST -> {
                return Leaderboard.HIGHEST_TO_LOWEST.compare(this, o);
            }

            case LOWEST_TO_HIGHEST -> {
                return Leaderboard.LOWEST_TO_HIGHEST.compare(this, o);
            }

            default -> throw new UnsupportedOperationException("Unknown comparing type: " + comparing.name());
        }
    }
}
