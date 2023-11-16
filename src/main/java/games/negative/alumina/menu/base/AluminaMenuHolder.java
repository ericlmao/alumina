/*
 *
 *  *  MIT License
 *  *
 *  * Copyright (C) 2023 Negative Games
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *  *
 *
 */

package games.negative.alumina.menu.base;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents the base menu holder class for all custom menus.
 * @param <T> The menu type.
 */
public interface AluminaMenuHolder<T extends AluminaMenu> extends InventoryHolder {

    /**
     * This method will be called when the menu is opened.
     * @param player The player who opened the menu.
     * @param event The event that was called.
     */
    void onOpen(final Player player, final InventoryOpenEvent event);

    /**
     * This method will be called when the menu is closed.
     * @param player The player who closed the menu.
     * @param event The event that was called.
     */
    void onClose(final Player player, final InventoryCloseEvent event);

    /**
     * This method will be called when the player clicks on an item in the menu.
     * @param player The player who clicked on the item.
     * @param event The event that was called.
     */
    void onClick(final Player player, final InventoryClickEvent event);

    /**
     * Gets the menu instance.
     * @return The menu instance.
     */
    T getMenu();

}
