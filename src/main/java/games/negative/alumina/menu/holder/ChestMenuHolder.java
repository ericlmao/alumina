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

package games.negative.alumina.menu.holder;

import com.google.common.base.Preconditions;
import games.negative.alumina.menu.ChestMenu;
import games.negative.alumina.menu.base.AluminaMenu;
import games.negative.alumina.menu.base.AluminaMenuHolder;
import games.negative.alumina.menu.base.MenuItem;
import games.negative.alumina.util.NBTEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ChestMenuHolder implements AluminaMenuHolder<ChestMenu> {

    private final ChestMenu gui;
    private Inventory inventory;

    public ChestMenuHolder(final ChestMenu gui) {
        this.gui = gui;
    }

    @Override
    public void onOpen(final Player player, final InventoryOpenEvent event) {
        Preconditions.checkNotNull(player, "player cannot be null");
        Preconditions.checkNotNull(event, "event cannot be null");

        gui.onOpen(player, event);
    }

    @Override
    public void onClose(final Player player, final InventoryCloseEvent event) {
        Preconditions.checkNotNull(player, "player cannot be null");
        Preconditions.checkNotNull(event, "event cannot be null");

        gui.onClose(player, event);
    }

    @Override
    public void onClick(@NotNull Player player, @NotNull InventoryClickEvent event) {
        gui.onClick(player, event);
        
        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (!NBTEditor.has(meta, AluminaMenu.FUNCTION_KEY, PersistentDataType.STRING)) return;

        String function = NBTEditor.get(meta, AluminaMenu.FUNCTION_KEY, PersistentDataType.STRING);
        if (function == null) return;

        MenuItem menuItem = gui.byKey(function);
        if (menuItem == null) return;

        gui.onFunctionClick(player, menuItem, event); // This is deprecated but we will still support it for now.
        gui.onFunctionClick(player, function, event);
    }

    @Override
    public @NotNull ChestMenu getMenu() {
        return gui;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
