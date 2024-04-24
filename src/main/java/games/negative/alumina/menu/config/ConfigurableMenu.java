package games.negative.alumina.menu.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.Polymorphic;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a menu that can be configured.
 */
@Polymorphic
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public abstract class ConfigurableMenu {

    @Comment({"The title of the menu."})
    private String title = "Menu Title";

    @Comment({"", "The amount of rows in the menu.", "Can be between 1 and 6."})
    private int rows = 3;

    /**
     * Gets the title from the config.
     * @return The title.
     */
    @NotNull
    public String title() {
        return title;
    }

    /**
     * Gets the rows from the config.
     * @return The rows.
     */
    public int rows() {
        return rows;
    }
}
