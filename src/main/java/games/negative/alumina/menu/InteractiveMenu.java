/*
 *  MIT License
 *
 * Copyright (C) 2025 Negative Games
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

package games.negative.alumina.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for an interactive menu.
 */
public interface InteractiveMenu {

    /**
     * Called when a player opens an inventory.
     *
     * @param player The player who opened the inventory.
     * @param event The InventoryOpenEvent triggered by the player opening the inventory.
     */
    void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event);

    /**
     * Called when the player closes the inventory associated with this menu.
     *
     * @param player the player who closed the inventory
     * @param event the close event
     */
    void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event);

    /**
     * Called when the player clicks on the inventory.
     *
     * @param player the player who clicked on the inventory
     * @param event the inventory click event
     */
    void onClick(@NotNull Player player, @NotNull InventoryClickEvent event);

    /**
     * Opens an interactive menu for the given player.
     *
     * @param player The player who will open the menu.
     */
    void open(@NotNull Player player);

    /**
     * Refreshes the inventory of the InteractiveMenu for the specified player.
     *
     * @param player The player whose inventory needs to be refreshed. Must not be null.
     */
    void refresh(@NotNull Player player);

}
