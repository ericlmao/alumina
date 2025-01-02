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


package games.negative.alumina.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * The TextUtil class provides utility methods for working with text and strings.
 */
public class TextUtil {

    /**
     * Parses a string representation of a UUID and returns the corresponding UUID object.
     *
     * @param uuid The string representation of the UUID.
     * @return The parsed UUID object, or null if the string is not a valid UUID.
     */
    @Nullable
    public static UUID parseUUID(@NotNull String uuid) {
        if (uuid.length() == 32) {
            return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16)
                    + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
        } else if (uuid.length() == 36) {
            return UUID.fromString(uuid);
        } else {
            return null;
        }
    }

    /**
     * Retrieves a list of strings from the given list that are similar to the argument.
     *
     * @param list     The list of strings to search through.
     * @param argument The string to compare with the elements in the list.
     * @return A list of strings similar to the argument.
     * @deprecated Use {@link TabCompleteUtil#getSimilarStrings(List, String)}
     */
    @Deprecated
    public static List<String> getSimilarStrings(@NotNull List<String> list, @NotNull String argument) {
        return TabCompleteUtil.getSimilarStrings(list, argument);
    }

    /**
     * Check if the origin string contains the given string
     * @param origin The original string
     * @param substring The substring to check
     * @return Whether the origin string contains the given string
     */
    public static boolean containsIgnoreCase(@NotNull String origin, @NotNull String substring) {
        int strLength = origin.length();
        int subLength = substring.length();

        for (int i = 0; i <= strLength - subLength; i++) {
            if (origin.regionMatches(true, i, substring, 0, subLength)) {
                return true;
            }
        }

        return false;
    }

}
