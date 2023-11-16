/*
 *
 *  *  MIT License
 *  *
 *  * Copyright (C) 2023 Negative Games
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *  *
 *
 */


package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Represents a time-formatting utility which is used to format time into
 * a human-readable format.
 */
public class TimeUtil {

    /**
     * Format a time into a human-readable format.
     * @param time Time to format
     * @param small Whether to use a small format such as 1s, 1d, etc.
     * @return Formatted time
     */
    @NotNull
    public static String format(long time, boolean small) {
        Preconditions.checkArgument(time >= 0, "Time cannot be smaller than 0.");

        int toSec = (int) (time / 1000) % 60;
        int toMin = (int) ((time / (1000 * 60)) % 60);
        int toHour = (int) ((time / (1000 * 60 * 60)) % 24);
        int toDays = (int) (time / (1000 * 60 * 60 * 24));

        String day = (small ? "d" : (toDays == 1 ? "day" : "days"));
        String hour = (small ? "h" : (toHour == 1 ? "hour" : "hours"));
        String minute = (small ? "m" : (toMin == 1 ? "minute" : "minutes"));
        String second = (small ? "s" : (toSec == 1 ? "second" : "seconds"));

        String dayFormat = (toDays != 0 ? toDays + (small ? "" : " ") + day + " " : "");
        String hourFormat = (toHour != 0 ? toHour + (small ? "" : " ") + hour + " " : "");
        String minuteFormat = (toMin != 0 ? toMin + (small ? "" : " ") + minute + " " : "");
        String secondFormat = (toSec != 0 ? toSec + (small ? "" : " ") + second : "");

        TimeFormatter formatter = new TimeFormatter(dayFormat, hourFormat, minuteFormat, secondFormat);
        return formatter.toString();
    }

    /**
     * Format a time into a human-readable format.
     * @param time Time to format
     * @return Formatted time
     */
    public static String format(long time) {
        return format(time, false);
    }

    /**
     * Format time from a string.
     * @param input Input string
     * @return Formatted time
     */
    public static long fromString(final String input) {
        Preconditions.checkNotNull(input, "'input' cannot be null!");
        Preconditions.checkArgument(!input.isEmpty(), "'input' cannot be empty!");

        StringBuilder builder = new StringBuilder();
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int weeks = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                builder.append(c);
            } else {
                switch (c) {
                    case 's' -> {
                        if (!builder.isEmpty()) {
                            seconds += Integer.parseInt(builder.toString());
                            builder = new StringBuilder();
                        }
                    }
                    case 'm' -> {
                        if (!builder.isEmpty()) {
                            minutes += Integer.parseInt(builder.toString());
                            builder = new StringBuilder();
                        }
                    }
                    case 'h' -> {
                        if (!builder.isEmpty()) {
                            hours += Integer.parseInt(builder.toString());
                            builder = new StringBuilder();
                        }
                    }
                    case 'd' -> {
                        if (!builder.isEmpty()) {
                            days += Integer.parseInt(builder.toString());
                            builder = new StringBuilder();
                        }
                    }
                    case 'w' -> {
                        if (!builder.isEmpty()) {
                            weeks += Integer.parseInt(builder.toString());
                            builder = new StringBuilder();
                        }
                    }
                    default -> throw new IllegalArgumentException("Not a valid duration format.");
                }
            }
        }
        return 1000L * (seconds + minutes * 60L + hours * 60 * 60L + days * 24 * 60 * 60L + weeks * 7 * 24 * 60 * 60L);
    }

    /**
     * The class to format time
     */
    private static class TimeFormatter {
        private final LinkedList<String> entries = new LinkedList<>();

        public TimeFormatter(final String... entries) {
            Preconditions.checkNotNull(entries, "'entries' cannot be null!");

            this.entries.addAll(Arrays.asList(entries));
        }


        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (String entry : entries) {
                if (entry.isBlank() || entry.isEmpty()) continue;

                builder.append(entry);
            }

            return builder.toString();
        }
    }
}
