package games.negative.alumina.leaderboard;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

public enum LeaderboardComparingType {

    HIGHEST_TO_LOWEST("highest_to_lowest"),
    LOWEST_TO_HIGHEST("lowest_to_highest");

    private final String id;

    LeaderboardComparingType(final String id) {
        Preconditions.checkNotNull(id, "ID cannot be null!");

        this.id = id;
    }

    public static Optional<LeaderboardComparingType> getByString(final String input) {
        Preconditions.checkNotNull(input, "Input cannot be null!");

        return Arrays.stream(values())
                .filter(leaderboardComparingType -> leaderboardComparingType.id.equalsIgnoreCase(input))
                .findFirst();
    }

}
