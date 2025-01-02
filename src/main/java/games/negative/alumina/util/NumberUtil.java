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

import com.google.common.base.Preconditions;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a number utility class used to handle numbers, parse them, etc.
 */
public class NumberUtil {

    /*
     * Format a number to a fancy format.
     */
    private static final DecimalFormat FANCY_FORMAT = new DecimalFormat("###,###,###,###,###.##");

    private static final String SUFFIXES = "kMBTQqSsOND";

    /**
     * Parse a number to a fancy format.
     *
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(int number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * Parse a number to a fancy format.
     *
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(long number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * Parse a number to a fancy format.
     *
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(double number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * Parse a number to a fancy format.
     *
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(float number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * Parse a number to a fancy format.
     *
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(short number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * Parse a number to a fancy format.
     *
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(byte number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * Parse a number to a fancy format.
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(@NotNull BigDecimal number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * Parse a number to a fancy format.
     * @param number Number to parse
     * @return Parsed number
     */
    public static String decimalFormat(@NotNull BigInteger number) {
        return FANCY_FORMAT.format(number);
    }

    /**
     * This method will get an {@link Integer} from the provided text.
     *
     * @param text Text to get the {@link Integer} from
     * @return {@link Integer} from the text, or null if it cannot be parsed
     * @throws IllegalArgumentException If the text is null
     */
    @SuppressWarnings("ConstantValue")
    @Nullable
    public static Integer getInteger(@NotNull String text) {
        if (text == null) throw new IllegalArgumentException("'text' cannot be null!");

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * This method parses a string to a Long value.
     *
     * @param text The string to be parsed
     * @return The parsed Long value, or null if the string cannot be parsed
     * @throws IllegalArgumentException If the text parameter is null
     */
    @SuppressWarnings("ConstantValue")
    @Nullable
    public static Long getLong(@NotNull String text) {
        if (text == null) throw new IllegalArgumentException("'text' cannot be null!");

        try {
            return Long.parseLong(text);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * Parses the given text to a Double value.
     *
     * @param text The text to parse
     * @return The parsed Double value, or null if the text cannot be parsed
     * @throws IllegalArgumentException if the text is null
     */
    @SuppressWarnings("ConstantValue")
    @Nullable
    public static Double getDouble(@NotNull String text) {
        if (text == null) throw new IllegalArgumentException("'text' cannot be null!");

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * Parses the given string to a Float value.
     *
     * @param text The string to parse
     * @return The parsed Float value, or null if the string cannot be parsed
     * @throws IllegalArgumentException If the text parameter is null
     */
    @SuppressWarnings("ConstantValue")
    @Nullable
    public static Float getFloat(@NotNull String text) {
        if (text == null) throw new IllegalArgumentException("'text' cannot be null!");

        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * Parses the given text to a Short value.
     *
     * @param text The text to parse
     * @return The parsed Short value, or null if the text cannot be parsed
     * @throws IllegalArgumentException If the text parameter is null
     */
    @SuppressWarnings("ConstantValue")
    @Nullable
    public static Short getShort(@NotNull String text) {
        if (text == null) throw new IllegalArgumentException("'text' cannot be null!");

        try {
            return Short.parseShort(text);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * This method will convert a number to a fancy version of
     * the provided number such as 1st, 2nd, 3rd, 4th, etc.
     *
     * @param number Number to convert
     * @return Fancy version of the number
     */
    @NotNull
    public static String fancy(int number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return decimalFormat(number) + "th";
        }

        return switch (number % 10) {
            case 1 -> decimalFormat(number) + "st";
            case 2 -> decimalFormat(number) + "nd";
            case 3 -> decimalFormat(number) + "rd";
            default -> decimalFormat(number) + "th";
        };
    }

    /**
     * This method will convert a number to a fancy version of
     * the provided number such as 1st, 2nd, 3rd, 4th, etc.
     *
     * @param number Number to convert
     * @return Fancy version of the number
     */
    @NotNull
    public static String fancy(long number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return decimalFormat(number) + "th";
        }

        return switch ((int) (number % 10)) {
            case 1 -> decimalFormat(number) + "st";
            case 2 -> decimalFormat(number) + "nd";
            case 3 -> decimalFormat(number) + "rd";
            default -> decimalFormat(number) + "th";
        };
    }

    /**
     * This method will convert a number to a fancy version of
     * the provided number such as 1st, 2nd, 3rd, 4th, etc.
     *
     * @param number Number to convert
     * @return Fancy version of the number
     */
    @NotNull
    public static String fancy(double number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return decimalFormat(number) + "th";
        }

        return switch ((int) (number % 10)) {
            case 1 -> decimalFormat(number) + "st";
            case 2 -> decimalFormat(number) + "nd";
            case 3 -> decimalFormat(number) + "rd";
            default -> decimalFormat(number) + "th";
        };
    }

    /**
     * This method will convert a number to a fancy version of
     * the provided number such as 1st, 2nd, 3rd, 4th, etc.
     *
     * @param number Number to convert
     * @return Fancy version of the number
     */
    @NotNull
    public static String fancy(float number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }

        return switch ((int) (number % 10)) {
            case 1 -> decimalFormat(number) + "st";
            case 2 -> decimalFormat(number) + "nd";
            case 3 -> decimalFormat(number) + "rd";
            default -> decimalFormat(number) + "th";
        };
    }

    /**
     * This method will convert a number to a fancy version of
     * the provided number such as 1st, 2nd, 3rd, 4th, etc.
     *
     * @param number Number to convert
     * @return Fancy version of the number
     */
    @NotNull
    public static String fancy(short number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return decimalFormat(number) + "th";
        }

        return switch (number % 10) {
            case 1 -> decimalFormat(number) + "st";
            case 2 -> decimalFormat(number) + "nd";
            case 3 -> decimalFormat(number) + "rd";
            default -> decimalFormat(number) + "th";
        };
    }

    /**
     * This method will convert a number to a fancy version of
     * the provided number such as 1st, 2nd, 3rd, 4th, etc.
     *
     * @param number Number to convert
     * @return Fancy version of the number
     */
    @NotNull
    public static String fancy(byte number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return decimalFormat(number) + "th";
        }

        return switch (number % 10) {
            case 1 -> decimalFormat(number) + "st";
            case 2 -> decimalFormat(number) + "nd";
            case 3 -> decimalFormat(number) + "rd";
            default -> decimalFormat(number) + "th";
        };
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @return Condensed number
     */
    @NotNull
    public static String condense(int number) {
        return condense(number, null);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @param set Set of suffixes to use
     * @return Condensed number
     */
    @NotNull
    public static String condense(int number, final char[] set) {
        if (number < 1000) return String.valueOf(number); // Return the number itself if less than 1000.

        int exp = (int) (Math.log(number) / Math.log(1000));

        String suffixes = (set == null) ? SUFFIXES : new String(set);
        char suffix = suffixes.charAt(Math.min(exp - 1, suffixes.length() - 1));

        return String.format("%.1f%c", number / Math.pow(1000, exp), suffix);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @return Condensed number
     */
    @NotNull
    public static String condense(double number) {
        return condense(number, null);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @param set Set of suffixes to use
     * @return Condensed number
     */
    @NotNull
    public static String condense(double number, final char[] set) {
        if (number < 1000) return String.valueOf(number); // Return the number itself if less than 1000.

        int exp = (int) (Math.log(number) / Math.log(1000));

        String suffixes = (set == null) ? SUFFIXES : new String(set);
        char suffix = suffixes.charAt(Math.min(exp - 1, suffixes.length() - 1));

        return String.format("%.1f%c", number / Math.pow(1000, exp), suffix);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @return Condensed number
     */
    @NotNull
    public static String condense(long number) {
        return condense(number, null);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @param set Set of suffixes to use
     * @return Condensed number
     */
    @NotNull
    public static String condense(long number, final char[] set) {
        if (number < 1000) return String.valueOf(number); // Return the number itself if less than 1000.

        int exp = (int) (Math.log(number) / Math.log(1000));

        String suffixes = (set == null) ? SUFFIXES : new String(set);
        char suffix = suffixes.charAt(Math.min(exp - 1, suffixes.length() - 1));

        return String.format("%.1f%c", number / Math.pow(1000, exp), suffix);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @return Condensed number
     */
    @NotNull
    public static String condense(@NotNull BigDecimal number) {
        Preconditions.checkNotNull(number, "'number' cannot be null!");

        return condense(number, null);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @param set Set of suffixes to use
     * @return Condensed number
     */
    @NotNull
    public static String condense(@NotNull BigDecimal number, final char[] set) {
        Preconditions.checkNotNull(number, "'number' cannot be null!");

        BigDecimal thousand = BigDecimal.valueOf(1000);
        if (number.compareTo(thousand) < 0) {
            return number.stripTrailingZeros().toPlainString(); // Return the number itself if less than 1000.
        }

        int exp = (int) (Math.floor(Math.log10(number.doubleValue()) / 3));

        String suffixes = (set == null) ? SUFFIXES : new String(set);
        char suffix = suffixes.charAt(Math.min(exp - 1, suffixes.length() - 1));

        BigDecimal result = number.divide(thousand.pow(exp), 1, RoundingMode.HALF_UP);

        return String.format("%.1f%c", result, suffix);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @return Condensed number
     */
    @NotNull
    public static String condense(@NotNull BigInteger number) {
        Preconditions.checkNotNull(number, "'number' cannot be null!");

        return condense(number, null);
    }

    /**
     * Condenses a number into a shorter version using suffixes.
     * @param number Number to condense
     * @param set Set of suffixes to use
     * @return Condensed number
     */
    @NotNull
    public static String condense(@NotNull BigInteger number, final char[] set) {
        Preconditions.checkNotNull(number, "'number' cannot be null!");

        return condense(new BigDecimal(number), set);
    }

    /**
     * Simulates the result of the vanilla fortune enchantment.
     * @param item The item to simulate
     * @param tool The tool to simulate
     * @return The simulated result
     */
    public static int simulateFortune(@NotNull ItemStack tool, @NotNull ItemStack item) {
        Preconditions.checkNotNull(tool, "'tool' cannot be null!");
        Preconditions.checkNotNull(item, "'item' cannot be null!");

        int initial = item.getAmount();

        ItemMeta meta = tool.getItemMeta();
        if (meta == null) return initial;

        if (!meta.hasEnchant(Enchantment.FORTUNE)) return initial;

        int level = meta.getEnchantLevel(Enchantment.FORTUNE);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int i = Math.max((random.nextInt(1, level + 2) - 1), 0);
        return (initial * (i + 1));
    }

}
