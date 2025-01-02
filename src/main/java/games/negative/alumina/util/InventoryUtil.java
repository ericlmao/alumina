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


package games.negative.alumina.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Represents a utility class for inventories.
 */
public class InventoryUtil {

    /**
     * Convert the player inventory to a base64 string.
     * @param playerInventory The player inventory to convert.
     * @return A {@link String} array, the first element is the main content, the second element is the armor.
     * @throws IllegalStateException If the inventory cannot be converted.
     */
    public static String[] playerInventoryToBase64(@NotNull final PlayerInventory playerInventory) throws IllegalStateException {
        Preconditions.checkNotNull(playerInventory, "Player inventory cannot be null.");

        //get the main content part, this doesn't return the armor
        String content = inventoryToBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[]{content, armor};
    }

    /**
     * Convert an {@link ItemStack} array to a base64 string.
     * @param items The item stack array to convert.
     * @return The base64 string.
     * @throws IllegalStateException If the item stack array cannot be converted.
     */
    public static String itemStackArrayToBase64(@NotNull final ItemStack[] items) throws IllegalStateException {
        Preconditions.checkNotNull(items, "Item stack array cannot be null.");

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Convert an inventory to a base64 string.
     * @param inventory The inventory to convert.
     * @return The base64 string.
     * @throws IllegalStateException If the inventory cannot be converted.
     */
    public static String inventoryToBase64(@NotNull final Inventory inventory) throws IllegalStateException {
        Preconditions.checkNotNull(inventory, "Inventory cannot be null.");

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Convert a base64 string to an inventory.
     * @param data The base64 string to convert.
     * @return The inventory.
     * @throws IllegalStateException If the base64 string cannot be converted.
     */
    public static Inventory inventoryFromBase64(@NotNull final String data) throws IllegalStateException, IOException {
        Preconditions.checkNotNull(data, "Base64 string cannot be null.");
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a base64 string to an item stack array.
     * @param data The base64 string to convert.
     * @return The item stack array.
     * @throws IllegalStateException If the base64 string cannot be converted.
     */
    public static ItemStack[] itemStackArrayFromBase64(@NotNull final String data) throws IllegalStateException, IOException {
        Preconditions.checkNotNull(data, "Base64 string cannot be null.");

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
