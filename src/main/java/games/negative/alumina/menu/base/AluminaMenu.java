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

import games.negative.alumina.AluminaPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the base class for all custom menus.
 */
public interface AluminaMenu {

    /*
     * This key will be used to store the function key in the item's metadata.
     */
    NamespacedKey FUNCTION_KEY = new NamespacedKey(AluminaPlugin.getAluminaInstance(), "menu-function-key");

    /**
     * This method will allow you to set a functional item in the menu.
     *
     * @param slot        The slot to set the item in.
     * @param item        The item to set.
     * @param functionKey The function key to set.
     *                    When the function key is null, the item will not be functional.
     */
    void setItem(int slot, @NotNull ItemStack item, @Nullable String functionKey);

    /**
     * This method will allow you to set a non-functional item in the menu.
     *
     * @param slot The slot to set the item in.
     * @param item The item to set.
     */
    default void setItem(int slot, @NotNull ItemStack item) {
        setItem(slot, item, null);
    }

    /**
     * This method will allow you to add a functional item to the menu.
     * @param item The item to add.
     * @param functionKey The function key to set.
     */
    void addItem(@NotNull ItemStack item, @Nullable String functionKey);

    /**
     * This method will allow you to add a non-functional item to the menu.
     * @param item The item to add.
     */
    default void addItem(@NotNull ItemStack item) {
        addItem(item, null);
    }

    /**
     * This method will allow you to clear a slot in the menu.
     *
     * @param slot The slot to clear.
     */
    void clearSlot(int slot);

    /**
     * This method will allow you to open the menu for a player.
     *
     * @param player The player to open the menu for.
     */
    void open(@NotNull Player player);

    /**
     * This method will allow you to refresh the menu.
     */
    void refresh(@Nullable Player player);

    /**
     * This method will allow you to listen to the open event of the menu.
     *
     * @param player The player who opened the menu.
     * @param event  The event.
     */
    void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event);

    /**
     * This method will allow you to listen to the close event of the menu.
     *
     * @param player The player who closed the menu.
     * @param event  The event.
     */
    void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event);

    /**
     * This method will allow you to listen to the click event of the menu.
     *
     * @param player The player who clicked the menu.
     * @param event  The event.
     */
    void onClick(@NotNull Player player, @NotNull InventoryClickEvent event);

    /**
     * This method will allow you to listen to the function click event of the menu.
     *
     * @param player The player who clicked the menu.
     * @param item   The item that was clicked.
     * @param event  The event.
     * @deprecated Use {@link #onFunctionClick(Player, String, InventoryClickEvent)} instead.
     */
    @Deprecated
    void onFunctionClick(@NotNull Player player, @NotNull MenuItem item, @NotNull InventoryClickEvent event);

    /**
     * This method will allow you to listen to the function click event of the menu.
     * @param player The player who clicked the menu.
     * @param key The function key.
     * @param event The event.
     */
    void onFunctionClick(@NotNull Player player, @NotNull String key, @NotNull InventoryClickEvent event);
}
