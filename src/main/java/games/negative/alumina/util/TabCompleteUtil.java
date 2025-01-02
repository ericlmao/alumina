/*
 * MIT License
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
 */

package games.negative.alumina.util;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class TabCompleteUtil {

    /**
     * Get a list of online players matching the input.
     * @param sender The player sending the tab completion request.
     * @param input The input to match.
     * @return A list of online players matching the input.
     */
    @NotNull
    public List<String> getOnlinePlayersMatching(@NotNull Player sender, @NotNull String input) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> sender.canSee(player) && StringUtil.startsWithIgnoreCase(player.getName(), input))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of online players matching the input and the predicate.
     * @param sender The player sending the tab completion request.
     * @param input The input to match.
     * @param predicate The predicate to test the players against.
     * @return A list of online players matching the input and the predicate.
     */
    @NotNull
    public List<String> getOnlinePlayersMatching(@NotNull Player sender, @NotNull String input, @NotNull Predicate<Player> predicate) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> sender.canSee(player) && StringUtil.startsWithIgnoreCase(player.getName(), input) && predicate.test(player))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of matching strings from a collection.
     * @param input The input to match.
     * @param strings The collection of strings to match against.
     * @return A list of matching strings from the collection.
     */
    @NotNull
    public Collection<String> getStringsMatching(@NotNull String input, @NotNull Collection<String> strings) {
        return strings.stream()
                .filter(string -> StringUtil.startsWithIgnoreCase(string, input))
                .collect(Collectors.toList());
    }

    /**
     * Get a list of matching strings from an array.
     * @param input The input to match.
     * @param strings The array of strings to match against.
     * @return A list of matching strings from the array.
     */
    @NotNull
    public Collection<String> getStringsMatching(@NotNull String input, @NotNull String... strings) {
        return Stream.of(strings)
                .filter(string -> StringUtil.startsWithIgnoreCase(string, input))
                .collect(Collectors.toList());
    }

    /**
     * Get a list of matching strings from a list.
     * @param input The input to match.
     * @param strings The list of strings to match against.
     * @return A list of matching strings from the list.
     */
    @NotNull
    public List<String> getStringsMatching(@NotNull String input, @NotNull List<String> strings) {
        return strings.stream()
                .filter(string -> StringUtil.startsWithIgnoreCase(string, input))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of strings from the given list that are similar to the argument.
     *
     * @param list     The list of strings to search through.
     * @param argument The string to compare with the elements in the list.
     * @return A list of strings similar to the argument.
     */
    public static List<String> getSimilarStrings(@NotNull List<String> list, @NotNull String argument) {
        List<String> similar = Lists.newArrayList();
        String lower = argument.toLowerCase();

        for (String option : list) {
            if (!TextUtil.containsIgnoreCase(option, argument)) continue;

            similar.add(option);
        }

        return similar;
    }
}
