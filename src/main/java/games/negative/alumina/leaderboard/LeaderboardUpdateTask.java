package games.negative.alumina.leaderboard;

import com.google.common.base.Preconditions;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Represents a task that can updates the leaderboard
 * @param <K> Key type which can be used for identification, such as {@link java.util.UUID}!
 * @param <V> Value type which can be used for sorting, such as {@link java.lang.Integer}!
 */
public class LeaderboardUpdateTask<K, V extends Comparable<V>> extends BukkitRunnable {

    private final Leaderboard<K, V> leaderboard;

    public LeaderboardUpdateTask(final Leaderboard<K, V> leaderboard) {
        Preconditions.checkNotNull(leaderboard, "Leaderboard cannot be null!");

        this.leaderboard = leaderboard;
    }

    @Override
    public void run() {
        leaderboard.update();
    }
}
