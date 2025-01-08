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

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.menu.holder.ChestMenuHolder;
import games.negative.alumina.util.MathUtil;
import games.negative.alumina.util.MiniMessageUtil;
import games.negative.alumina.util.NBTEditor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @apiNote This system still uses the old Spigot method of formatting titles, such as &c for red.
 */
public abstract class ChestMenu implements InteractiveMenu {

    public static final LegacyComponentSerializer TITLE_SERIALIZER = LegacyComponentSerializer.legacySection();

    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 6;

    private static final NamespacedKey FUNCTION = new NamespacedKey(AluminaPlugin.getAluminaInstance(), "chest-menu-function");

    @Setter
    private Component title = Component.text("Chest Menu");
    private int rows = 1;

    @Setter
    private boolean cancelClicks = false;

    protected final Set<MenuButton> buttons;

    protected Inventory inventory;

    /**
     * Represents a chest menu with a specific title and number of rows.
     */
    public ChestMenu(@NotNull String title, int rows) {
        Preconditions.checkNotNull(title, "Title cannot be null");
        Preconditions.checkArgument(MathUtil.between(rows, MIN_ROWS, MAX_ROWS), "Rows must be between " + MIN_ROWS + " and " + MAX_ROWS);

        this.title = MiniMessageUtil.translate(title);
        this.rows = rows;

        this.buttons = Sets.newHashSet();
        this.inventory = Bukkit.createInventory(new ChestMenuHolder(this), rows * 9, this.title);
    }

    /**
     * Represents a chest menu with a specific title and number of rows.
     */
    public ChestMenu(@NotNull Component title, int rows) {
        Preconditions.checkNotNull(title, "Title cannot be null");
        Preconditions.checkArgument(MathUtil.between(rows, MIN_ROWS, MAX_ROWS), "Rows must be between " + MIN_ROWS + " and " + MAX_ROWS);

        this.title = title;
        this.rows = rows;

        this.buttons = Sets.newHashSet();
        this.inventory = Bukkit.createInventory(new ChestMenuHolder(this), rows * 9, this.title);
    }

    /**
     * Represents a chest menu without a title or number of rows.
     */
    public ChestMenu() {
        this.buttons = Sets.newHashSet();
    }

    /**
     * Opens the menu for the specified player.
     *
     * @param player The player for whom the menu should be opened.
     */
    @Override
    public void open(@NotNull Player player) {
        Preconditions.checkNotNull(player, "Player cannot be null");

        if (inventory == null) inventory = Bukkit.createInventory(new ChestMenuHolder(this), rows * 9, title);

        refresh(player);

        player.openInventory(inventory);
    }

    /**
     * Refreshes the inventory of the ChestMenu for the specified player.
     *
     * @param player The player whose inventory needs to be refreshed. Must not be null.
     */
    @Override
    public void refresh(@NotNull Player player) {
        inventory.clear();

        for (MenuButton button : buttons) {
            int slot = button.getSlot();
            if (isSlotOccupied(slot) || !button.canView(player)) continue;

            ItemStack item = button.getItem();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;

            NBTEditor.set(meta, FUNCTION, PersistentDataType.STRING, button.uuid().toString());

            item.setItemMeta(meta);

            if (slot == -1) slot = getFreeSlot();
            if (slot == -1) continue;

            inventory.setItem(slot, item);
        }
    }

    /**
     * Refreshes the button at the specified slot in the inventory.
     *
     * @param slot The slot number of the button to refresh. Must be between 0 and (rows * 9).
     */
    public void refreshButton(int slot) {
        Preconditions.checkArgument(MathUtil.between(slot, 0, rows * 9), "Slot must be between 0 and " + (rows * 9));

        MenuButton button = buttons.stream().filter(menuButton -> menuButton.getSlot() == slot).findFirst().orElse(null);
        if (button == null) return;

        ItemStack item = button.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        NBTEditor.set(meta, FUNCTION, PersistentDataType.STRING, button.uuid().toString());

        item.setItemMeta(meta);

        inventory.setItem(slot, item);
    }

    /**
     * Called when a player opens an inventory.
     *
     * @param player The player who opened the inventory.
     * @param event The InventoryOpenEvent triggered by the player opening the inventory.
     * @apiNote Override this method to perform actions when the player opens the inventory.
     */
    @Override
    public void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event) {

    }

    /**
     * Called when the player closes the inventory associated with this menu.
     *
     * @param player The player who closed the inventory.
     * @param event The InventoryCloseEvent triggered by the player closing the inventory.
     * @apiNote Override this method to perform actions when the player closes the inventory.
     */
    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event) {

    }

    /**
     * Called when a player clicks on an inventory item.
     *
     * @param player The player who clicked.
     * @param event  The inventory click event.
     * @apiNote Do not override this method!!!!!! pretty please :)
     */
    @Override
    public void onClick(@NotNull Player player, @NotNull InventoryClickEvent event) {
        if (cancelClicks) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

        ItemStack current = event.getCurrentItem();
        if (current == null) return;

        ItemMeta meta = current.getItemMeta();
        if (meta == null) return;

        String function = NBTEditor.get(meta, FUNCTION, PersistentDataType.STRING);
        if (function == null) {
            // Check by slot.
            MenuButton button = buttons.stream().filter(menuButton -> menuButton.getSlot() == event.getSlot()).findFirst().orElse(null);
            if (button == null) return;

            button.process(player, event);
            return;
        }

        MenuButton button = buttons.stream().filter(menuButton -> menuButton.uuid().toString().equals(function)).findFirst().orElse(null);
        if (button == null) return;

        button.process(player, event);
    }

    /**
     * Adds a menu button to the list of buttons in the menu.
     *
     * @param button The menu button to be added. Cannot be null.
     * @throws NullPointerException if the button parameter is null.
     */
    public void addButton(@NotNull MenuButton button) {
        Preconditions.checkNotNull(button, "Button cannot be null");

        buttons.add(button);
    }

    /**
     * Removes the specified menu button from the list of buttons in the chest menu.
     *
     * @param button The menu button to remove. Cannot be null.
     * @throws NullPointerException if the button is null.
     */
    public void removeButton(@NotNull MenuButton button) {
        Preconditions.checkNotNull(button, "Button cannot be null");

        buttons.remove(button);
    }

    /**
     * Returns the index of the first free slot in the inventory.
     *
     * @return The index of the first free slot, or -1 if there are no free slots.
     */
    private int getFreeSlot() {
        int index = 0;
        for (ItemStack content : inventory.getContents()) {
            if (content == null) return index;

            index++;
        }
        return -1;
    }

    /**
     * Checks if a slot in the inventory is occupied.
     *
     * @param slot The index of the slot to check. Use -1 to check if any free slot is available.
     * @return True if the slot is occupied, false otherwise.
     */
    private boolean isSlotOccupied(int slot) {
        if (slot == -1) return getFreeSlot() == -1;

        Preconditions.checkArgument(MathUtil.between(slot, 0, rows * 9), "Slot must be between 0 and " + (rows * 9));

        return inventory.getItem(slot) != null;
    }

    /**
     * Sets the number of rows in the menu.
     *
     * @param rows The number of rows to set. Must be between MIN_ROWS and MAX_ROWS.
     * @throws IllegalArgumentException if the rows value is not between MIN_ROWS and MAX_ROWS.
     */
    public void setRows(int rows) {
        Preconditions.checkArgument(MathUtil.between(rows, MIN_ROWS, MAX_ROWS), "Rows must be between " + MIN_ROWS + " and " + MAX_ROWS);

        this.rows = rows;
    }

    /**
     * Updates the title of the chest menu and refreshes the inventory for all viewers.
     *
     * @param input The new title to set. Must not be null.
     * @throws NullPointerException if the input parameter is null.
     */
    public void updateTitle(@NotNull String input) {
        Preconditions.checkNotNull(input, "Title cannot be null");

        this.title = MiniMessageUtil.translate(input);

        if (inventory == null)
            inventory = Bukkit.createInventory(new ChestMenuHolder(this), rows * 9, title);

        for (HumanEntity viewer : inventory.getViewers()) {
            InventoryView view = viewer.getOpenInventory();
            if (!(view.getTopInventory().getHolder() instanceof ChestMenuHolder)) continue;

            view.setTitle(TITLE_SERIALIZER.serialize(title));
        }
    }

    /**
     * Updates the title of the chest menu and refreshes the inventory for all viewers.
     *
     * @param input The new title to set. Must not be null.
     * @throws NullPointerException if the input parameter is null.
     */
    public void updateTitle(@NotNull Component input) {
        Preconditions.checkNotNull(input, "Title cannot be null");

        this.title = input;

        if (inventory == null)
            inventory = Bukkit.createInventory(new ChestMenuHolder(this), rows * 9, title);

        for (HumanEntity viewer : inventory.getViewers()) {
            InventoryView view = viewer.getOpenInventory();
            if (!(view.getTopInventory().getHolder() instanceof ChestMenuHolder)) continue;

            view.setTitle(TITLE_SERIALIZER.serialize(title));
        }
    }

    /**
     * Returns the {@link MenuButton} set of this chest menu.
     * @return The set of buttons in this chest menu.
     */
    @NotNull
    public Set<MenuButton> buttons() {
        return buttons;
    }
}
