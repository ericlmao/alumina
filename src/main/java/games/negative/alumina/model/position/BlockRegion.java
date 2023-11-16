package games.negative.alumina.model.position;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import games.negative.alumina.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Represents a simple cuboid block region.
 * @param name The name of the region.
 *             This is used to identify the region.
 *
 * @param world The world the region is in.
 *              This is used to check what world the block positions are in.
 *
 * @param min The minimum block position.
 * @param max The maximum block position.
 */
public record BlockRegion(String name, World world, BlockPosition min, BlockPosition max) implements ConfigurationSerializable {

    public BlockRegion {
        Preconditions.checkNotNull(name, "Name cannot be null.");
        Preconditions.checkNotNull(world, "World cannot be null.");
        Preconditions.checkNotNull(min, "Min cannot be null.");
        Preconditions.checkNotNull(max, "Max cannot be null.");
    }

    /**
     * Check if a location is inside the region.
     * @param location The location to check.
     * @return If the location is inside the region.
     */
    public boolean isInside(@NotNull Location location) {
        Location min = world.getBlockAt(this.min.x(), this.min.y(), this.min.z()).getLocation();
        Location max = world.getBlockAt(this.max.x(), this.max.y(), this.max.z()).getLocation();

        return LocationUtil.isInside(location, min, max);
    }

    /**
     * Get all the blocks in the region.
     * @return All the blocks in the region.
     */
    public Collection<Block> getBlocks() {
        Location min = world.getBlockAt(this.min.x(), this.min.y(), this.min.z()).getLocation();
        Location max = world.getBlockAt(this.max.x(), this.max.y(), this.max.z()).getLocation();

        List<Block> blocks = Lists.newArrayList();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    /**
     * Get all the blocks in the region that match the filter.
     * @param filter The filter to use.
     * @return All the blocks in the region that match the filter.
     */
    public Collection<Block> getBlocks(@NotNull Predicate<Block> filter) {
        Location min = world.getBlockAt(this.min.x(), this.min.y(), this.min.z()).getLocation();
        Location max = world.getBlockAt(this.max.x(), this.max.y(), this.max.z()).getLocation();

        List<Block> blocks = Lists.newArrayList();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (!filter.test(block)) continue;

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    /**
     * Serialize the region to Bukkit's configuration format.
     * @return The serialized region.
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "name", name,
                "world", world.getName(),
                "min", min,
                "max", max
        );
    }

    /**
     * Serialize the region to raw JSON.
     * @return The serialized region.
     */
    @Override
    public String toString() {
        return "BlockRegion{" +
                "name='" + name + '\'' +
                ", world=" + world +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
