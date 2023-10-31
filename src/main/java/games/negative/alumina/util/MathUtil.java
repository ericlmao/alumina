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
