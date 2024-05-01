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


package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a utility class used to update {@link ItemStack}s
 * without needing to write boilerplate code.
 */
public class ItemUpdater {

    /**
     * Update an item using a processor.
     * @param item Item to update
     * @param processor Processor to use
     */
    public static void of(@NotNull ItemStack item, @NotNull Consumer<ItemMeta> processor) {
        Preconditions.checkNotNull(item, "item cannot be null!");
        Preconditions.checkNotNull(processor, "processor cannot be null!");

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        processor.accept(meta);
        item.setItemMeta(meta);
    }

    /**
     * Update an item using a processor with a specified ItemMeta type.
     * @param item Item to update
     * @param type Type of the ItemMeta
     * @param processor Processor to use
     * @param <T> Type of the ItemMeta
     */
    public static <T extends ItemMeta> void of(@NotNull ItemStack item, @NotNull Class<T> type, @NotNull Consumer<T> processor) {
        Preconditions.checkNotNull(item, "item cannot be null!");
        Preconditions.checkNotNull(type, "type cannot be null!");
        Preconditions.checkNotNull(processor, "processor cannot be null!");

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (!type.isInstance(meta)) return;

        processor.accept(type.cast(meta));
        item.setItemMeta(meta);
    }

}
