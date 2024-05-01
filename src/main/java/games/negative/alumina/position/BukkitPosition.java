/*
 *  MIT License
 *
 * Copyright (C) 2024 Negative Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package games.negative.alumina.position;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The BukkitPosition class represents a position in a Bukkit world.
 */
public record BukkitPosition(@NotNull World world, double x, double y, double z) implements Locatable, ConfigurationSerializable {

    @Override
    public @NotNull Location getLocation() {
        return new Location(world, x, y, z);
    }

    public static @NotNull BukkitPosition fromLocation(@NotNull Location location) {
        World world = location.getWorld();
        Preconditions.checkNotNull(world, "Location world cannot be null");


        return new BukkitPosition(world, location.getX(), location.getY(), location.getZ());
    }

    public static BukkitPosition fromYaml(@NotNull Map<String, Object> yaml) {
        World world = Bukkit.getWorld((String) yaml.get("world"));
        Preconditions.checkNotNull(world, "World cannot be null");

        double x = (double) yaml.get("x");
        double y = (double) yaml.get("y");
        double z = (double) yaml.get("z");

        return new BukkitPosition(world, x, y, z);
    }

    public static BukkitPosition fromConfigurationSection(@NotNull ConfigurationSection section) {
        World world = Bukkit.getWorld(section.getString("world", "world"));
        Preconditions.checkNotNull(world, "World cannot be null");

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");

        return new BukkitPosition(world, x, y, z);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "world", world.getName(),
                "x", x,
                "y", y,
                "z", z
        );
    }
}
