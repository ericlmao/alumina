package games.negative.alumina.message.color;

/**
 * This interface is used to translate color codes, and external plugins can implement this interface to use their own color codes.
 * If they do not like the way that alumina implements color codes.
 */
public interface ColorAgent {

    /**
     * This method is used to translate color codes.
     * @param input The input string.
     * @return The translated string.
     */
    String translate(String input);
}
