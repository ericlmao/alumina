package games.negative.alumina.menu.config;

import java.lang.annotation.*;

/**
 * Represents a header for the menu file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface Header {

    String value();

}
