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

package games.negative.alumina.menu.holder;

import com.google.common.base.Preconditions;
import games.negative.alumina.menu.ChestMenu;
import games.negative.alumina.menu.InteractiveMenuHolder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ChestMenuHolder implements InteractiveMenuHolder<ChestMenu> {

    private final ChestMenu menu;
    private Inventory inventory;

    @Override
    public void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event) {
        Preconditions.checkNotNull(player, "Player cannot be null");
        Preconditions.checkNotNull(event, "Event cannot be null");

        menu.onOpen(player, event);
    }

    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event) {
        Preconditions.checkNotNull(player, "Player cannot be null");
        Preconditions.checkNotNull(event, "Event cannot be null");

        menu.onClose(player, event);
    }

    @Override
    public void onClick(@NotNull Player player, @NotNull InventoryClickEvent event) {
        Preconditions.checkNotNull(player, "Player cannot be null");
        Preconditions.checkNotNull(event, "Event cannot be null");

        menu.onClick(player, event);
    }

    @Override
    public @NotNull ChestMenu getMenu() {
        return menu;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(@NotNull Inventory inventory) {
        this.inventory = inventory;
    }
}
