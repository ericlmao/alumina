/*
 * MIT License
 *
 * Copyright (c) 2025 Negative Games
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
 */

package games.negative.alumina.position;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a block position in the world.
 * @param world the world the block is in
 * @param x The x-coordinate of the block
 * @param y The y-coordinate of the block
 * @param z The z-coordinate of the block
 */
public record BlockPosition(World world, int x, int y, int z) {

    /**
     * Gets the {@link Location} of this block position.
     * @return The location of this block position
     */
    @NotNull
    public Location location() {
        return new Location(world, x, y, z);
    }

    /**
     * Gets the {@link Block} at this position.
     * @return The block at this position
     */
    @NotNull
    public Block block() {
        return world.getBlockAt(x, y, z);
    }

    /**
     * Creates a new BlockPosition from a Location.
     *
     * @param location The location to create the BlockPosition from
     * @return A new BlockPosition
     */
    public static BlockPosition fromLocation(@NotNull Location location) {
        World world = location.getWorld();
        Preconditions.checkState(world != null, "World cannot be null!");

        return new BlockPosition(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
