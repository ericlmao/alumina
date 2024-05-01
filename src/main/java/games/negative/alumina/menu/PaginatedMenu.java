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

package games.negative.alumina.menu;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.menu.holder.PaginatedMenuHolder;
import games.negative.alumina.util.MathUtil;
import games.negative.alumina.util.MiniMessageUtil;
import games.negative.alumina.util.NBTEditor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * A paginated menu that can be used to display a large number of items in a Chest Menu environment.
 * @apiNote This system still uses the old Spigot method of formatting titles, such as &c for red.
 */
public abstract class PaginatedMenu implements InteractiveMenu {

    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 6;

    private static final NamespacedKey FUNCTION = new NamespacedKey(AluminaPlugin.getAluminaInstance(), "paginated-menu-function");

    @Setter
    private Component title = Component.text("Paginated Menu");
    private int rows = 1;
    protected int page = 1;

    @Setter
    private boolean cancelClicks = false;

    private MenuButton nextPageButton;
    private MenuButton previousPageButton;

    private final Set<MenuButton> buttons;
    private final Set<MenuButton> listings;
    private final Set<Integer> paginatedSlots;

    protected Inventory inventory;

    /**
     * Represents a paginated menu with a title and specified number of rows.
     * The menu uses a Bukkit inventory to display buttons and listings.
     */
    public PaginatedMenu(@NotNull String title, int rows) {
        Preconditions.checkNotNull(title, "Title cannot be null");
        Preconditions.checkArgument(MathUtil.between(rows, MIN_ROWS, MAX_ROWS), "Rows must be between " + MIN_ROWS + " and " + MAX_ROWS);

        this.title = MiniMessageUtil.translate(title);
        this.rows = rows;

        this.buttons = Sets.newLinkedHashSet();
        this.listings = Sets.newLinkedHashSet();
        this.paginatedSlots = Sets.newHashSet();

        this.inventory = Bukkit.createInventory(new PaginatedMenuHolder(this), rows * 9, this.title);
    }

    /**
     * Represents a paginated menu with a title and specified number of rows.
     * The menu uses a Bukkit inventory to display buttons and listings.
     */
    public PaginatedMenu(@NotNull Component title, int rows) {
        Preconditions.checkNotNull(title, "Title cannot be null");
        Preconditions.checkArgument(MathUtil.between(rows, MIN_ROWS, MAX_ROWS), "Rows must be between " + MIN_ROWS + " and " + MAX_ROWS);

        this.title = title;
        this.rows = rows;

        this.buttons = Sets.newLinkedHashSet();
        this.listings = Sets.newLinkedHashSet();
        this.paginatedSlots = Sets.newHashSet();

        this.inventory = Bukkit.createInventory(new PaginatedMenuHolder(this), rows * 9, this.title);
    }

    /**
     * Represents a paginated menu with a title and specified number of rows.
     * The menu uses a Bukkit inventory to display buttons and listings.
     */
    public PaginatedMenu() {
        this.buttons = Sets.newLinkedHashSet();
        this.listings = Sets.newLinkedHashSet();
        this.paginatedSlots = Sets.newHashSet();
    }

    /**
     * Called when a player opens the inventory associated with this menu.
     *
     * @param player The player who opened the inventory.
     * @param event The InventoryOpenEvent triggered by the player opening the inventory.
     * @apiNote Override this method to add custom functionality when the inventory is opened.
     */
    @Override
    public void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event) {

    }

    /**
     * Called when the player closes the inventory associated with this menu.
     *
     * @param player The player who closed the inventory.
     * @param event The InventoryCloseEvent triggered by the player closing the inventory.
     * @apiNote Override this method to add custom functionality when the inventory is closed.
     */
    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event) {

    }

    /**
     * Called when the player clicks on the inventory associated with this menu.
     * @param player the player who clicked on the inventory
     * @param event the inventory click event
     * @apiNote Please do not override this method!!!! pretty please!! :>
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

        List<MenuButton> all = Lists.newArrayList(buttons);
        all.addAll(listings);
        all.add(nextPageButton);
        all.add(previousPageButton);

        String function = NBTEditor.get(meta, FUNCTION, PersistentDataType.STRING);
        if (function == null) {
            // Check by slot.
            MenuButton button = all.stream().filter(menuButton -> menuButton.getSlot() == event.getSlot()).findFirst().orElse(null);
            if (button == null) return;

            button.process(player, event);
            return;
        }

        MenuButton button = all.stream().filter(menuButton -> menuButton.uuid().toString().equals(function)).findFirst().orElse(null);
        if (button == null) return;

        button.process(player, event);
    }

    /**
     * Opens the inventory associated with this PaginatedMenu for the specified player.
     *
     * @param player The player who will open the inventory.
     * @throws NullPointerException if the player parameter is null.
     */
    @Override
    public void open(@NotNull Player player) {
        Preconditions.checkNotNull(player, "Player cannot be null");

        if (inventory == null) inventory = Bukkit.createInventory(new PaginatedMenuHolder(this), rows * 9, title);

        refresh(player);

        player.openInventory(inventory);
    }

    /**
     * Refreshes the inventory of the menu for the specified player.
     * This method clears the inventory and populates it with updated buttons and listings.
     *
     * @param player The player for whom to refresh the inventory. Must not be null.
     * @throws NullPointerException if the player parameter is null.
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

        List<Integer> listingSlots = Lists.newArrayList(paginatedSlots);
        int limit = listingSlots.size();

        List<MenuButton> items = listings.stream().filter(button -> button.canView(player))
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .toList();

        for (MenuButton item : items) {
            int available = listingSlots.stream().min(Comparator.comparingInt(value -> value)).orElse(-1);
            if (available == -1) break;

            listingSlots.remove(Integer.valueOf(available));

            ItemStack itemStack = item.getItem();
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) continue;

            NBTEditor.set(meta, FUNCTION, PersistentDataType.STRING, item.uuid().toString());

            itemStack.setItemMeta(meta);

            inventory.setItem(available, itemStack);
        }

        if (page > 1) {
            Preconditions.checkNotNull(previousPageButton, "Previous page button cannot be null");

            ItemStack item = previousPageButton.getItem();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NBTEditor.set(meta, FUNCTION, PersistentDataType.STRING, previousPageButton.uuid().toString());

            item.setItemMeta(meta);

            inventory.setItem(previousPageButton.getSlot(), item);
        }

        if (listings.size() > (page * limit)) {
            Preconditions.checkNotNull(nextPageButton, "Next page button cannot be null");

            ItemStack item = nextPageButton.getItem();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NBTEditor.set(meta, FUNCTION, PersistentDataType.STRING, nextPageButton.uuid().toString());

            item.setItemMeta(meta);

            inventory.setItem(nextPageButton.getSlot(), item);
        }
    }

    /**
     * Generates a collection of MenuButtons based on the provided collection and function.
     *
     * @param <T>        the type of the collection
     * @param <K>        the type of elements in the collection
     * @param collection the collection of elements
     * @param function   the function to generate a MenuButton from an element
     * @return the generated collection of MenuButtons
     * @throws NullPointerException if the collection or function is null
     */
    public <T extends Iterable<K>, K> Collection<MenuButton> generatePaginatedButtons(@NotNull T collection, @NotNull Function<K, MenuButton> function) {
        Preconditions.checkNotNull(collection, "Collection cannot be null");
        Preconditions.checkNotNull(function, "Function cannot be null");

        List<MenuButton> generated = Lists.newArrayList();
        for (K key : collection) {
            MenuButton button = function.apply(key);
            if (button == null) continue;
            
            generated.add(button);
        }

        return generated;
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
            inventory = Bukkit.createInventory(new PaginatedMenuHolder(this), rows * 9, title);

        for (HumanEntity viewer : inventory.getViewers()) {
            InventoryView view = viewer.getOpenInventory();
            if (!(view.getTopInventory().getHolder() instanceof PaginatedMenuHolder)) continue;

            view.setTitle(ChestMenu.TITLE_SERIALIZER.serialize(title));
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
            inventory = Bukkit.createInventory(new PaginatedMenuHolder(this), rows * 9, title);

        for (HumanEntity viewer : inventory.getViewers()) {
            InventoryView view = viewer.getOpenInventory();
            if (!(view.getTopInventory().getHolder() instanceof PaginatedMenuHolder)) continue;

            view.setTitle(ChestMenu.TITLE_SERIALIZER.serialize(title));
        }
    }

    /**
     * Sets the next page button for the menu.
     *
     * @param button The next page button to set. Must not be null.
     * @throws NullPointerException if the button parameter is null
     */
    public void setNextPageButton(@NotNull MenuButton button) {
        Preconditions.checkNotNull(button, "Next page button cannot be null");

        this.nextPageButton = button;
    }

    /**
     * Sets the previous page button for the paginated menu.
     *
     * @param button The menu button to be set as the previous page button. Must not be null.
     * @throws NullPointerException if the button parameter is null.
     */
    public void setPreviousPageButton(@NotNull MenuButton button) {
        Preconditions.checkNotNull(button, "Previous page button cannot be null");

        this.previousPageButton = button;
    }

    /**
     * Sets the paginated slots for the menu.
     *
     * @param slots The collection of integer slots to set. Must not be null.
     * @throws NullPointerException if the slots parameter is null.
     */
    public void setPaginatedSlots(@NotNull Collection<Integer> slots) {
        Preconditions.checkNotNull(slots, "Slots cannot be null");

        this.paginatedSlots.clear();
        this.paginatedSlots.addAll(slots);
    }

    /**
     * Sets the slots that are used for pagination in the menu.
     *
     * @param slots The slots to set. Must not be null.
     * @throws NullPointerException if the slots parameter is null.
     */
    public void setPaginatedSlots(int... slots) {
        Preconditions.checkNotNull(slots, "Slots cannot be null");

        this.paginatedSlots.clear();
        for (int slot : slots) {
            this.paginatedSlots.add(slot);
        }
    }

    /**
     * Sets a collection of buttons as paginated buttons.
     *
     * @param buttons The collection of buttons to set as paginated buttons. Must not be null.
     * @throws NullPointerException If the buttons parameter is null.
     */
    public void setPaginatedButtons(@NotNull Collection<MenuButton> buttons) {
        Preconditions.checkNotNull(buttons, "Buttons cannot be null");

        this.listings.clear();
        this.listings.addAll(buttons);
    }

    /**
     * Removes a paginated button from the menu.
     *
     * @param button The button to be removed. Must not be null.
     * @throws NullPointerException if the button parameter is null.
     */
    public void removePaginatedButton(@NotNull MenuButton button) {
        Preconditions.checkNotNull(button, "Button cannot be null");

        this.listings.remove(button);
    }

    /**
     * Adds a menu button to the paginated menu.
     *
     * @param button The menu button to add. Must not be null.
     * @throws NullPointerException if the button is null.
     */
    public void addButton(@NotNull MenuButton button) {
        Preconditions.checkNotNull(button, "Button cannot be null");

        this.buttons.add(button);
    }

    /**
     * Removes a MenuButton from the PaginatedMenu.
     *
     * @param button The MenuButton to remove. Must not be null.
     * @throws NullPointerException if the button is null.
     */
    public void removeButton(@NotNull MenuButton button) {
        Preconditions.checkNotNull(button, "Button cannot be null");

        this.buttons.remove(button);
    }

    /**
     * Changes the page of the menu for the specified player.
     *
     * @param player The player for whom to change the page. Must not be null.
     * @param page The new page number. Must be greater than 0 and less than or equal to the total number of pages.
     * @throws IllegalArgumentException if the page is less than or equal to 0 or greater than the total number of pages.
     * @throws NullPointerException if the player parameter is null.
     */
    public void changePage(Player player, int page) {
        Preconditions.checkArgument(page > 0, "Page must be greater than 0");
        Preconditions.checkArgument(page <= Math.ceil((double) listings.size() / paginatedSlots.size()), "Page must be less than or equal to " + Math.ceil((double) listings.size() / paginatedSlots.size()));
        Preconditions.checkNotNull(player, "Player cannot be null");

        this.page = page;

        refresh(player);
    }

}
