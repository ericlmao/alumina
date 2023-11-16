package games.negative.alumina.message.color;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Alumina's default color agent.
 */
public class AluminaColorAgent implements ColorAgent {

    /*
     * This pattern is used to match hex colors.
     *
     * Example &[FFFFFF] would be matched as #FFFFFF
     */
    private final Pattern PATTERN = Pattern.compile("&\\[([A-Fa-f0-9]{6})]");

    /*
     * This is the fallback color agent.
     */
    private final ColorAgent fallback;

    public AluminaColorAgent() {
        this.fallback = new MinecraftColorAgent();
    }

    @Override
    public String translate(String input) {
        Preconditions.checkNotNull(input, "input cannot be null!");

        Matcher matcher = PATTERN.matcher(input);
        while (matcher.find()) {
            String color = input.substring(matcher.start(), matcher.end());
            input = input.replace(color, String.valueOf(ChatColor.of(color)));
            matcher = PATTERN.matcher(input);
        }
        return fallback.translate(input);
    }
}
