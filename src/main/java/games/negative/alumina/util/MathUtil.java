/*
 *  MIT License
 *
 * Copyright (C) 2024 Negative Games
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

/**
 * A utility class with simple mathematical functions.
 */
public class MathUtil {

    /**
     * This method will return the absolute difference between the given values.
     * @param a The first value.
     * @param b The second value.
     * @return The absolute difference between the given values.
     */
    public static int absDiff(int a, int b) {
        return Math.abs(a - b);
    }

    /**
     * This method will return the absolute difference between the given values.
     * @param a The first value.
     * @param b The second value.
     * @return The absolute difference between the given values.
     */
    public static double absDiff(double a, double b) {
        return Math.abs(a - b);
    }

    /**
     * This method will return the absolute difference between the given values.
     * @param a The first value.
     * @param b The second value.
     * @return The absolute difference between the given values.
     */
    public static float absDiff(float a, float b) {
        return Math.abs(a - b);
    }

    /**
     * This method will return the absolute difference between the given values.
     * @param a The first value.
     * @param b The second value.
     * @return The absolute difference between the given values.
     */
    public static long absDiff(long a, long b) {
        return Math.abs(a - b);
    }

    /**
     * This method will return the absolute difference between the given values.
     * @param a The first value.
     * @param b The second value.
     * @return The absolute difference between the given values.
     */
    public static short absDiff(short a, short b) {
        return (short) Math.abs(a - b);
    }

    /**
     * This method will check if the given value is between the given min and max.
     * @param value The value to check.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return True if the value is between the min and max, otherwise false.
     */
    public static boolean between(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * This method will check if the given value is between the given min and max.
     * @param value The value to check.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return True if the value is between the min and max, otherwise false.
     */
    public static boolean between(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * This method will check if the given value is between the given min and max.
     * @param value The value to check.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return True if the value is between the min and max, otherwise false.
     */
    public static boolean between(float value, float min, float max) {
        return value >= min && value <= max;
    }

    /**
     * This method will check if the given value is between the given min and max.
     * @param value The value to check.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return True if the value is between the min and max, otherwise false.
     */
    public static boolean between(long value, long min, long max) {
        return value >= min && value <= max;
    }

    /**
     * This method will check if the given value is between the given min and max.
     * @param value The value to check.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return True if the value is between the min and max, otherwise false.
     */
    public static boolean between(short value, short min, short max) {
        return value >= min && value <= max;
    }

    /**
     * This method will check if the given value is between the given min and max.
     * @param value The value to check.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return True if the value is between the min and max, otherwise false.
     */
    public static boolean between(byte value, byte min, byte max) {
        return value >= min && value <= max;
    }

}
