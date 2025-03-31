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
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * This class represents a position in the world along with the yaw and pitch.
 * @param world the world the position is in
 * @param x The x-coordinate of the position
 * @param y The y-coordinate of the position
 * @param z The z-coordinate of the position
 * @param yaw the yaw of the position
 * @param pitch the pitch of the position
 */
public record Position(World world, double x, double y, double z, float yaw, float pitch) {

    /**
     * Gets the {@link BlockPosition} of this position.
     * @return The block position of this position
     */
    public BlockPosition blockPosition() {
        Location loc = new Location(world, x, y, z);
        return new BlockPosition(world, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    /**
     * Gets the {@link Location} of this position.
     * @return The location of this position
     */
    @NotNull
    public Location location() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Gets the {@link Block} at this position.
     * @return The block at this position
     */
    @NotNull
    public Block block() {
        return world.getBlockAt((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    /**
     * Teleports the given entity to this position.
     * @param entity The entity to teleport
     * @return True if the teleport was successful, false otherwise
     */
    public boolean teleport(@NotNull Entity entity) {
        return entity.teleport(location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    /**
     * Teleports the given entity to this position asynchronously.
     * @param entity The entity to teleport
     * @return A CompletableFuture that will be completed with true if the teleport was successful, false otherwise
     */
    @CheckReturnValue
    public CompletableFuture<Boolean> teleportAsync(@NotNull Entity entity) {
        return entity.teleportAsync(location(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    /**
     * Creates a new Position from a Location.
     * @param location The location to create the Position from
     * @return A new Position
     */
    public static Position fromLocation(@NotNull Location location) {
        World world = location.getWorld();
        Preconditions.checkState(world != null, "World cannot be null!");

        return new Position(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
