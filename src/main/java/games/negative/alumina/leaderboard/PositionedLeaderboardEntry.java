package games.negative.alumina.leaderboard;

import com.google.common.base.Preconditions;

public record PositionedLeaderboardEntry<K, V extends Comparable<V>>(K key, V value, int position) {

    public PositionedLeaderboardEntry {
        Preconditions.checkNotNull(key, "Key cannot be null!");
        Preconditions.checkNotNull(value, "Value cannot be null!");
    }
}
