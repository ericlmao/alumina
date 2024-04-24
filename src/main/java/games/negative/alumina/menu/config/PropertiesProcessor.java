package games.negative.alumina.menu.config;

import de.exlll.configlib.YamlConfigurationProperties;

/**
 * A functional interface for processing properties.
 */
@FunctionalInterface
public interface PropertiesProcessor {

    /**
     * Process the properties.
     * @param builder The properties builder.
     */
    void process(YamlConfigurationProperties.Builder<?> builder);

}
