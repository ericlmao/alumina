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
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * This interface represents an Interactive Menu Holder. It extends the InventoryHolder interface.
 * Classes implementing this interface should provide implementations for handling certain events related to the interactive menu.
 * It also provides a method to get the Interactive Menu associated with the holder.
 *
 * @param <T> The type of the Interactive Menu associated with the holder.
 */
public interface InteractiveMenuHolder<T extends InteractiveMenu> extends InventoryHolder {

    /**
     * Called when a player opens an inventory in an InteractiveMenuHolder.
     *
     * @param player The player who opened the inventory.
     * @param event The InventoryOpenEvent triggered by the player opening the inventory.
     */
    void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event);

    /**
     * Called when the player closes the inventory associated with this InteractiveMenuHolder.
     *
     * @param player The player who closed the inventory.
     * @param event The InventoryCloseEvent triggered by the player closing the inventory.
     */
    void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event);

    /**
     * Handles the click event when the player interacts with the inventory.
     *
     * @param player The player who clicked on the inventory.
     * @param event The InventoryClickEvent triggered by the player's click.
     */
    void onClick(@NotNull Player player, @NotNull InventoryClickEvent event);

    /**
     * Retrieves the Interactive Menu associated with the holder.
     *
     * @return The Interactive Menu associated with the holder.
     */
    @NotNull
    T getMenu();
}
