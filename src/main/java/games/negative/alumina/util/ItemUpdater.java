package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    public static void of(final ItemStack item, final Consumer<ItemMeta> processor) {
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
    public static <T extends ItemMeta> void of(final ItemStack item, final Class<T> type, final Consumer<T> processor) {
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
