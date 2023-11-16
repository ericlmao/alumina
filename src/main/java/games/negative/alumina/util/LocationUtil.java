package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

/**
 * A location utility to handle some location-related tasks.
 */
public class LocationUtil {

    /**
     * This method is used to convert a location to a yaml map.
     *
     * @param location The location to convert.
     * @return The yaml map.
     */
    public static Map<String, Object> toYaml(final Location location) {
        Preconditions.checkNotNull(location, "Location cannot be null!");

        World world = location.getWorld();
        Preconditions.checkNotNull(world, "World cannot be null!");

        return Map.of(
                "world", world.getName(),
                "x", location.getX(),
                "y", location.getY(),
                "z", location.getZ(),
                "yaw", location.getYaw(),
                "pitch", location.getPitch()
        );
    }

    /**
     * This method is used to convert a configuration section to a location.
     *
     * @param section The section to convert.
     * @return The location.
     */
    public static Location fromYaml(final ConfigurationSection section) {
        Preconditions.checkNotNull(section, "Section cannot be null!");

        World world = Bukkit.getWorld(section.getString("world", "world"));
        Preconditions.checkNotNull(world, "World cannot be null!");

        return new Location(
                world,
                section.getDouble("x", 0),
                section.getDouble("y", 0),
                section.getDouble("z", 0),
                (float) section.getDouble("yaw", 0),
                (float) section.getDouble("pitch", 0)
        );
    }

    /**
     * This method is used to convert a map to a location.
     *
     * @param map The map to convert.
     * @return The location.
     */
    public static Location fromYaml(final Map<String, Object> map) {
        Preconditions.checkNotNull(map, "Map cannot be null!");

        World world = Bukkit.getWorld((String) map.get("world"));
        Preconditions.checkNotNull(world, "World cannot be null!");

        return new Location(
                world,
                (double) map.get("x"),
                (double) map.get("y"),
                (double) map.get("z"),
                (float) map.get("yaw"),
                (float) map.get("pitch")
        );
    }

    /**
     * Check if a location is inside a cuboid.
     *
     * @param location The location to check.
     * @param min      The minimum location of the cuboid.
     * @param max      The maximum location of the cuboid.
     * @return Whether the location is inside the cuboid.
     */
    public static boolean isInside(final Location location, final Location min, final Location max) {
        Preconditions.checkNotNull(location, "'location' cannot be null!");
        Preconditions.checkNotNull(min, "'min' cannot be null!");
        Preconditions.checkNotNull(max, "'max' cannot be null!");

        World locationWorld = location.getWorld();
        World minWorld = min.getWorld();
        World maxWorld = max.getWorld();

        Preconditions.checkNotNull(locationWorld, "Location world cannot be null!");
        Preconditions.checkNotNull(minWorld, "Min world cannot be null!");
        Preconditions.checkNotNull(maxWorld, "Max world cannot be null!");

        Preconditions.checkArgument(minWorld.equals(maxWorld), "Min world must be the same as max world!");
        Preconditions.checkArgument(locationWorld.equals(minWorld), "Location world must be the same as min world!");
        Preconditions.checkArgument(locationWorld.equals(maxWorld), "Location world must be the same as max world!");

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        int x1 = Math.min(min.getBlockX(), max.getBlockX());
        int y1 = Math.min(min.getBlockY(), max.getBlockY());
        int z1 = Math.min(min.getBlockZ(), max.getBlockZ());

        int x2 = Math.max(min.getBlockX(), max.getBlockX());
        int y2 = Math.max(min.getBlockY(), max.getBlockY());
        int z2 = Math.max(min.getBlockZ(), max.getBlockZ());

        return (x >= x1 && x <= x2) && (y >= y1 && y <= y2) && (z >= z1 && z <= z2);
    }
}
