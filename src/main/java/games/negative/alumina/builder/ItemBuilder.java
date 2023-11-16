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

package games.negative.alumina.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import games.negative.alumina.util.ColorUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Represents a builder utility for {@link ItemStack} to make item creation easier for the developer.
 */
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    /**
     * Creates a new {@link ItemBuilder} instance from an existing {@link ItemStack}.
     * @param item The item to create the builder from.
     */
    public ItemBuilder(final ItemStack item) {
        Preconditions.checkNotNull(item, "Item cannot be null!");

        this.item = item.clone();
        this.meta = this.item.getItemMeta();

        Preconditions.checkNotNull(this.meta, "ItemMeta cannot be null!");
    }

    /**
     * Creates a new {@link ItemBuilder} instance from an existing {@link ItemBuilder}.
     * @param builder The builder to create the builder from.
     */
    public ItemBuilder(final ItemBuilder builder) {
        Preconditions.checkNotNull(builder, "ItemBuilder cannot be null!");

        this.item = builder.item.clone();
        this.meta = builder.meta.clone();
    }

    /**
     * Creates a new {@link ItemBuilder} instance from the provided {@link Material}.
     * @param material The material to create the builder from.
     */
    public ItemBuilder(final Material material) {
        this(new ItemStack(material));
    }

    /**
     * Creates a new {@link ItemBuilder} instance from the provided {@link Material} and amount.
     * @param material The material to create the builder from.
     * @param amount The amount of the item.
     */
    public ItemBuilder(final Material material, final int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * Set the display name of the item.
     * @param text The text to set the display name to.
     * @return The current instance of the builder.
     */
    public ItemBuilder setName(final String text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");

        this.meta.setDisplayName(ColorUtil.translate(text));
        return this;
    }

    /**
     * Replace a placeholder in the display name with a replacement.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    public ItemBuilder replaceName(final String placeholder, final String replacement) {
        Preconditions.checkNotNull(placeholder, "Placeholder cannot be null!");
        Preconditions.checkNotNull(replacement, "Replacement cannot be null!");

        return this.setName(this.meta.getDisplayName().replace(placeholder, replacement));
    }

    /**
     * Set the lore of the item.
     * @param text The text to set the lore to.
     * @return The current instance of the builder.
     */
    @NotNull
    public ItemBuilder setLore(final String... text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(text.length > 0, "Text cannot be empty!");

        List<String> parsed = Lists.newArrayList();

        for (String line : text) {
            parsed.add(ColorUtil.translate(line));
        }

        this.meta.setLore(parsed);
        return this;
    }

    /**
     * Set the lore of the item.
     * @param text The text to set the lore to.
     * @return The current instance of the builder.
     */
    public ItemBuilder setLore(final List<String> text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(!text.isEmpty(), "Text cannot be empty!");

        List<String> parsed = Lists.newArrayList();

        for (String line : text) {
            parsed.add(ColorUtil.translate(line));
        }

        this.meta.setLore(parsed);
        return this;
    }

    /**
     * Add a single line of lore to the item.
     * @param text The text to add to the lore.
     * @return The current instance of the builder.
     */
    public ItemBuilder addLoreLine(final String text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");

        List<String> lore = this.meta.getLore();
        if (lore == null) lore = Lists.newArrayList();

        lore.add(ColorUtil.translate(text));
        this.meta.setLore(lore);

        return this;
    }

    /**
     * Add multiple lines of lore to the item.
     * @param text The text to add to the lore.
     * @return The current instance of the builder.
     */
    public ItemBuilder addLoreLines(final String... text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(text.length > 0, "Text cannot be empty!");

        List<String> lore = this.meta.getLore();
        if (lore == null) lore = Lists.newArrayList();

        for (String line : text) {
            lore.add(ColorUtil.translate(line));
        }

        this.meta.setLore(lore);

        return this;
    }

    /**
     * Add multiple lines of lore to the item.
     * @param text The text to add to the lore.
     * @return The current instance of the builder.
     */
    public ItemBuilder addLoreLines(final List<String> text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(!text.isEmpty(), "Text cannot be empty!");

        List<String> lore = this.meta.getLore();
        if (lore == null) lore = Lists.newArrayList();

        for (String line : text) {
            lore.add(ColorUtil.translate(line));
        }

        this.meta.setLore(lore);

        return this;
    }

    /**
     * Replace the lore of the item.
     * @param function The function to replace the lore.
     * @return The current instance of the builder.
     */
    public ItemBuilder replaceLore(final UnaryOperator<String> function) {
        Preconditions.checkNotNull(function, "Function cannot be null!");

        List<String> lore = this.meta.getLore();
        if (lore == null) lore = Lists.newArrayList();

        lore.replaceAll(function);

        this.meta.setLore(lore);
        return this;
    }

    /**
     * Replace a single line of lore.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    public ItemBuilder replaceLore(final String placeholder, final String replacement) {
        Preconditions.checkNotNull(placeholder, "Placeholder cannot be null!");
        Preconditions.checkNotNull(replacement, "Replacement cannot be null!");

        return this.replaceLore(line -> line.replace(placeholder, replacement));
    }

    /**
     * Add an enchantment to the item.
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     * @return The current instance of the builder.
     */
    public ItemBuilder addEnchantment(final Enchantment enchantment, final int level) {
        Preconditions.checkNotNull(enchantment, "Enchantment cannot be null!");
        Preconditions.checkArgument(level > 0, "Level must be greater than 0!");

        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Remove an enchantment from the item.
     * @param enchantment The enchantment to remove.
     * @return The current instance of the builder.
     */
    public ItemBuilder removeEnchantment(final Enchantment enchantment) {
        Preconditions.checkNotNull(enchantment, "Enchantment cannot be null!");

        this.meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Set the item to be unbreakable.
     * @param unbreakable Whether the item should be unbreakable.
     * @return The current instance of the builder.
     */
    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Add an item flag to the item.
     * @param flags The flags to add.
     * @return The current instance of the builder.
     */
    public ItemBuilder addItemFlags(final ItemFlag... flags) {
        Preconditions.checkNotNull(flags, "Flags cannot be null!");
        Preconditions.checkArgument(flags.length > 0, "Flags cannot be empty!");

        this.meta.addItemFlags(flags);
        return this;
    }

    /**
     * Remove an item flag from the item.
     * @param flags The flags to remove.
     * @return The current instance of the builder.
     */
    public ItemBuilder removeItemFlags(final ItemFlag... flags) {
        Preconditions.checkNotNull(flags, "Flags cannot be null!");
        Preconditions.checkArgument(flags.length > 0, "Flags cannot be empty!");

        this.meta.removeItemFlags(flags);
        return this;
    }

    /**
     * Set the skull texture of the item.
     * @param player The player to set the skull texture to.
     * @return The current instance of the builder.
     * @throws ClassCastException If the item is not a skull.
     */
    public ItemBuilder setSkullOwner(final OfflinePlayer player) {
        Preconditions.checkNotNull(player, "Player cannot be null!");

        SkullMeta skullMeta = (SkullMeta) this.meta;
        skullMeta.setOwningPlayer(player);
        return this;
    }

    /**
     * Set the skull texture of the item.
     * @param profile The profile to set the skull texture to.
     * @return The current instance of the builder.
     * @throws ClassCastException If the item is not a skull.
     */
    public ItemBuilder setSkullOwner(final PlayerProfile profile) {
        Preconditions.checkNotNull(profile, "Profile cannot be null!");
        SkullMeta skullMeta = (SkullMeta) this.meta;
        skullMeta.setOwnerProfile(profile);
        return this;
    }

    /**
     * Set the custom model data of the item.
     * @param data The custom model data to set.
     * @return The current instance of the builder.
     */
    public ItemBuilder setCustomModelData(final Integer data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    /**
     * Set the color of the item.
     * @param color The color to set.
     * @return The current instance of the builder.
     * @throws ClassCastException If the item is not a leather armor piece.
     */
    public ItemBuilder setLeatherColor(final Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.meta;
        leatherArmorMeta.setColor(color);
        return this;
    }

    /**
     * Apply custom persistent data to the item.
     * @param function The function to apply the persistent data.
     * @return The current instance of the builder.
     */
    public ItemBuilder applyPersistentData(final Consumer<PersistentDataContainer> function) {
        Preconditions.checkNotNull(function, "Function cannot be null!");

        function.accept(this.meta.getPersistentDataContainer());
        return this;
    }

    /**
     * Add an attribute modifier to the item.
     * @param attribute The attribute to add the modifier to.
     * @param modifier The modifier to add.
     * @return The current instance of the builder.
     */
    public ItemBuilder addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null!");
        Preconditions.checkNotNull(modifier, "Modifier cannot be null!");

        this.meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * Remove an attribute modifier from the item.
     * @param attribute The attribute to remove the modifier from.
     * @return The current instance of the builder.
     */
    public ItemBuilder removeAttributeModifier(final Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null!");

        this.meta.removeAttributeModifier(attribute);
        return this;
    }

    /**
     * Remove an attribute modifier from the item.
     * @param attribute The attribute to remove the modifier from.
     * @param modifier The modifier to remove.
     * @return The current instance of the builder.
     */
    public ItemBuilder removeAttributeModifier(final Attribute attribute, final AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null!");
        Preconditions.checkNotNull(modifier, "Modifier cannot be null!");

        this.meta.removeAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * Construct the {@link ItemStack} from the builder.
     * @return The constructed {@link ItemStack}.
     */
    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }

}
