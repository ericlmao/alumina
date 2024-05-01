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

import com.google.common.base.Preconditions;
import games.negative.alumina.model.Unique;
import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a menu button that can be clicked and triggers an action.
 */
@Builder
public class MenuButton implements Unique {

    private ItemStack item;
    private final int slot;
    private final ClickAction action;
    private final Predicate<Player> viewCondition;
    private final UUID uuid = UUID.randomUUID();

    /**
     * Processes the click event for a menu button.
     *
     * @param player The player who clicked the button.
     * @param event The click event that occurred.
     */
    public void process(@NotNull Player player, @NotNull InventoryClickEvent event) {
        Preconditions.checkNotNull(player, "Player cannot be null");
        Preconditions.checkNotNull(event, "Event cannot be null");

        if (action == null) return;

        action.onClick(this, player, event);
    }

    /**
     * Updates the item associated with the menu button using the provided function.
     * The function takes the current item as input and returns the updated item.
     *
     * @param function The function to update the item. Cannot be null.
     * @throws NullPointerException if the function is null.
     */
    public void updateItem(@NotNull Function<ItemStack, ItemStack> function) {
        Preconditions.checkNotNull(function, "Function cannot be null");

        item = function.apply(item);
    }

    /**
     * Checks if a player can view a menu button.
     *
     * @param player The player to check.
     * @return {@code true} if the player can view the menu button, {@code false} otherwise.
     * @throws NullPointerException if the player is null.
     */
    public boolean canView(@NotNull Player player) {
        Preconditions.checkNotNull(player, "Player cannot be null");

        return viewCondition == null || viewCondition.test(player);
    }

    /**
     * Returns the universally unique identifier (UUID) for this object.
     *
     * @return A UUID representing the unique identifier for this object.
     *
     * @throws NullPointerException if the UUID is null.
     *
     * @see UUID
     */
    @Override
    public @NotNull UUID uuid() {
        return uuid;
    }

    /**
     * Retrieves the slot number of the MenuButton.
     *
     * @return The slot number of the MenuButton.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Retrieves the ItemStack associated with this MenuButton.
     *
     * @return The ItemStack associated with this MenuButton.
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Represents an action that can be performed when a player clicks on a menu button.
     */
    @FunctionalInterface
    public interface ClickAction {

        void onClick(@NotNull MenuButton button, @NotNull Player player, @NotNull InventoryClickEvent event);

    }
}
