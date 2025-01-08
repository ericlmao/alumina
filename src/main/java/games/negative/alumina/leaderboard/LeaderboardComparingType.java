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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public enum LeaderboardComparingType {

    HIGHEST_TO_LOWEST("highest_to_lowest"),
    LOWEST_TO_HIGHEST("lowest_to_highest");

    private final String id;

    LeaderboardComparingType(@NotNull final String id) {
        Preconditions.checkNotNull(id, "ID cannot be null!");

        this.id = id;
    }

    public static Optional<LeaderboardComparingType> getByString(@NotNull final String input) {
        Preconditions.checkNotNull(input, "Input cannot be null!");

        return Arrays.stream(values())
                .filter(leaderboardComparingType -> leaderboardComparingType.id.equalsIgnoreCase(input))
                .findFirst();
    }

}
