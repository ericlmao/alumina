package games.negative.alumina.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PluginUtil {

    /**
     * Checks if a plugin is on the server.
     * @param name The name of the plugin.
     * @return True if the plugin is on the server.
     */
    public boolean hasPlugin(@NotNull String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

}
