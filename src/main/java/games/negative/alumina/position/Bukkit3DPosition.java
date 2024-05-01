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
 * The Bukkit3DPosition class represents a 3D position in a Bukkit world.
 */
public record Bukkit3DPosition(@NotNull World world, double x, double y, double z, float yaw, float pitch) implements Locatable, ConfigurationSerializable {

    @Override
    public @NotNull Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static Bukkit3DPosition fromYaml(@NotNull Map<String, Object> yaml) {
        World world = Bukkit.getWorld((String) yaml.get("world"));
        Preconditions.checkNotNull(world, "World %s does not exist", yaml.get("world"));

        double x = (double) yaml.get("x");
        double y = (double) yaml.get("y");
        double z = (double) yaml.get("z");
        float yaw = ((Number) yaml.get("yaw")).floatValue();
        float pitch = ((Number) yaml.get("pitch")).floatValue();

        return new Bukkit3DPosition(world, x, y, z, yaw, pitch);
    }

    public static Bukkit3DPosition fromConfigurationSection(@NotNull ConfigurationSection section) {
        World world = Bukkit.getWorld(section.getString("world", "world"));
        Preconditions.checkNotNull(world, "World %s does not exist", section.getString("world"));

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");

        return new Bukkit3DPosition(world, x, y, z, yaw, pitch);
    }

    public static Bukkit3DPosition fromLocation(@NotNull Location location) {
        World world = location.getWorld();
        Preconditions.checkNotNull(world, "World %s does not exist", location.getWorld());

        return new Bukkit3DPosition(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "world", world.getName(),
                "x", x,
                "y", y,
                "z", z,
                "yaw", yaw,
                "pitch", pitch
        );
    }
}
