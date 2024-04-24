package games.negative.alumina.menu.config;

import java.lang.annotation.*;

/**
 * Represents a footer for the menu file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface Footer {

    String value();

}
