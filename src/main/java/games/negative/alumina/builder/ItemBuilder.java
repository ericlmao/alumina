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

package games.negative.alumina.builder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import games.negative.alumina.util.MiniMessageUtil;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Unbreakable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Represents a builder utility for {@link ItemStack} to make item creation easier for the developer.
 */
public class ItemBuilder {

    // This is required so item names will not have italics by default!
    public static final MiniMessage MINIMESSAGE = MiniMessage.builder().postProcessor(component -> component.decoration(TextDecoration.ITALIC, false)).build();

    private final ItemStack item;
    private ItemMeta meta;

    /**
     * Creates a new {@link ItemBuilder} instance from an existing {@link ItemStack}.
     * @param item The item to create the builder from.
     */
    public ItemBuilder(@NotNull final ItemStack item) {
        Preconditions.checkNotNull(item, "Item cannot be null!");

        this.item = item.clone();
        this.meta = this.item.getItemMeta();

        Preconditions.checkNotNull(this.meta, "ItemMeta cannot be null!");
    }

    /**
     * Creates a new {@link ItemBuilder} instance from an existing {@link ItemBuilder}.
     * @param builder The builder to create the builder from.
     */
    public ItemBuilder(@NotNull final ItemBuilder builder) {
        Preconditions.checkNotNull(builder, "ItemBuilder cannot be null!");

        this.item = builder.item.clone();
        this.meta = builder.meta.clone();
    }

    /**
     * Creates a new {@link ItemBuilder} instance from the provided {@link Material}.
     * @param material The material to create the builder from.
     */
    public ItemBuilder(@NotNull final Material material) {
        this(ItemStack.of(material));
    }

    /**
     * Creates a new {@link ItemBuilder} instance from the provided {@link Material} and amount.
     * @param material The material to create the builder from.
     * @param amount The amount of the item.
     */
    public ItemBuilder(@NotNull final Material material, final int amount) {
        this(ItemStack.of(material, amount));
    }

    /**
     * Set the display name of the item.
     * @param text The text to set the display name to.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setName(@NotNull final String text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");

        this.meta.displayName(MiniMessageUtil.translate(text, MINIMESSAGE));
        applyMeta();
        return this;
    }

    /**
     * Set the display name of the item.
     * @param component The component to set the display name to.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setName(@NotNull final Component component) {
        Preconditions.checkNotNull(component, "Component cannot be null!");

        this.meta.displayName(component);
        applyMeta();
        return this;
    }

    /**
     * Replace a placeholder in the display name with a replacement.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder replaceName(@NotNull final String placeholder, @NotNull final String replacement) {
        Preconditions.checkNotNull(placeholder, "Placeholder cannot be null!");
        Preconditions.checkNotNull(replacement, "Replacement cannot be null!");

        Component component = this.meta.displayName();
        if (component == null) return this;

        Component modified = component.replaceText(TextReplacementConfig.builder().matchLiteral(placeholder).replacement(replacement).build());
        this.meta.displayName(modified);
        applyMeta();
        return this;
    }

    /**
     * Replace a placeholder in the display name with a replacement.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder replaceName(@NotNull final String placeholder, @NotNull final Component replacement) {
        Preconditions.checkNotNull(placeholder, "Placeholder cannot be null!");
        Preconditions.checkNotNull(replacement, "Replacement cannot be null!");

        Component component = this.meta.displayName();
        if (component == null) return this;

        Component modified = component.replaceText(TextReplacementConfig.builder().matchLiteral(placeholder).replacement(replacement).build());
        this.meta.displayName(modified);
        applyMeta();
        return this;
    }

    /**
     * Set the lore of the item.
     * @param text The text to set the lore to.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setLore(@NotNull final String... text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(text.length > 0, "Text cannot be empty!");

        List<Component> components = Arrays.stream(text).map(s -> MiniMessageUtil.translate(s, MINIMESSAGE)).collect(Collectors.toList());
        this.meta.lore(components);
        applyMeta();
        return this;
    }

    /**
     * Set the lore of the item.
     * @param components The components to set the lore to.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setLore(@NotNull final Component... components) {
        Preconditions.checkNotNull(components, "Components cannot be null!");
        Preconditions.checkArgument(components.length > 0, "Components cannot be empty!");

        this.meta.lore(Arrays.asList(components));
        applyMeta();
        return this;
    }

    /**
     * Set the lore of the item.
     * @param text The text to set the lore to.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setLore(@NotNull final List<String> text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(!text.isEmpty(), "Text cannot be empty!");

        List<Component> components = text.stream().map(s -> MiniMessageUtil.translate(s, MINIMESSAGE)).collect(Collectors.toList());
        this.meta.lore(components);
        applyMeta();
        return this;
    }

    /**
     * Set the lore of the item.
     * @param components The components to set the lore to.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setLore(@NotNull final Collection<Component> components) {
        Preconditions.checkNotNull(components, "Components cannot be null!");
        Preconditions.checkArgument(!components.isEmpty(), "Components cannot be empty!");

        this.meta.lore(components.stream().toList());
        applyMeta();
        return this;
    }

    /**
     * Add a single line of lore to the item.
     * @param text The text to add to the lore.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addLoreLine(@NotNull final String text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        lore.add(MiniMessageUtil.translate(text, MINIMESSAGE));
        this.meta.lore(lore);

        applyMeta();
        return this;
    }

    /**
     * Add a single line of lore to the item.
     * @param component The component to add to the lore.
     * @return  The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addLoreLine(@NotNull final Component component) {
        Preconditions.checkNotNull(component, "Component cannot be null!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        lore.add(component);
        this.meta.lore(lore);

        applyMeta();
        return this;
    }

    /**
     * Add multiple lines of lore to the item.
     * @param text The text to add to the lore.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addLoreLines(@NotNull final String... text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(text.length > 0, "Text cannot be empty!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        List<Component> components = Arrays.stream(text).map(s -> MiniMessageUtil.translate(s, MINIMESSAGE)).collect(Collectors.toList());
        lore.addAll(components);

        this.meta.lore(lore);

        applyMeta();
        return this;
    }

    /**
     * Add multiple lines of lore to the item.
     * @param components The components to add to the lore.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addLoreLines(@NotNull Component... components) {
        Preconditions.checkNotNull(components, "Components cannot be null!");
        Preconditions.checkArgument(components.length > 0, "Components cannot be empty!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        lore.addAll(Arrays.asList(components));

        this.meta.lore(lore);
        applyMeta();
        return this;
    }

    /**
     * Add multiple lines of lore to the item.
     * @param text The text to add to the lore.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addLoreLines(@NotNull final List<String> text) {
        Preconditions.checkNotNull(text, "Text cannot be null!");
        Preconditions.checkArgument(!text.isEmpty(), "Text cannot be empty!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        List<Component> components = text.stream().map(s -> MiniMessageUtil.translate(s, MINIMESSAGE)).toList();
        lore.addAll(components);

        this.meta.lore(lore);
        applyMeta();
        return this;
    }

    @CheckReturnValue
    public ItemBuilder addLoreLines(@NotNull final Collection<Component> components) {
        Preconditions.checkNotNull(components, "Components cannot be null!");
        Preconditions.checkArgument(!components.isEmpty(), "Components cannot be empty!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        lore.addAll(components);
        this.meta.lore(lore);
        applyMeta();
        return this;
    }

    /**
     * Replace a single line of lore.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder replaceLore(@NotNull final String placeholder, @NotNull final String replacement) {
        Preconditions.checkNotNull(placeholder, "Placeholder cannot be null!");
        Preconditions.checkNotNull(replacement, "Replacement cannot be null!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        List<Component> modified = lore.stream()
                .map(component -> component.replaceText(TextReplacementConfig.builder().matchLiteral(placeholder).replacement(replacement).build()))
                .collect(Collectors.toList());

        this.meta.lore(modified);
        applyMeta();
        return this;
    }

    /**
     * Replace a single line of lore.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder replaceLore(@NotNull final String placeholder, @NotNull final Component replacement) {
        Preconditions.checkNotNull(placeholder, "Placeholder cannot be null!");
        Preconditions.checkNotNull(replacement, "Replacement cannot be null!");

        List<Component> lore = this.meta.lore();
        if (lore == null) lore = Lists.newArrayList();

        List<Component> modified = lore.stream()
                .map(component -> component.replaceText(TextReplacementConfig.builder().matchLiteral(placeholder).replacement(replacement).build()))
                .collect(Collectors.toList());

        this.meta.lore(modified);
        applyMeta();
        return this;
    }

    /**
     * Replace a single line of lore with multiple lines.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder replaceLore(@NotNull String placeholder, @NotNull List<String> replacement) {
        return replaceLore(placeholder, replacement.stream().map(s -> MiniMessageUtil.translate(s, MINIMESSAGE)).map(component -> (TextComponent) component).toList());
    }

    /**
     * Replace a single line of lore with multiple lines.
     * @param placeholder The placeholder to replace.
     * @param replacement The replacement for the placeholder.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder replaceLore(@NotNull final String placeholder, @NotNull final Collection<TextComponent> replacement) {
        Preconditions.checkNotNull(placeholder, "Placeholder cannot be null!");
        Preconditions.checkNotNull(replacement, "Replacement cannot be null!");

        List<TextComponent> lore = Objects.requireNonNull(this.meta.lore()).stream()
                .filter(component -> component instanceof TextComponent)
                .map(component -> (TextComponent) component)
                .toList();

        List<TextComponent> components = Lists.newArrayList();

        for (TextComponent component : lore) {
            if (MiniMessageUtil.searchComponent(component, placeholder)) {
                components.addAll(replacement);
                continue;
            }

            components.add(component);
        }

        this.meta.lore(components);
        applyMeta();
        return this;
    }

    /**
     * Add an enchantment to the item.
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addEnchantment(@NotNull final Enchantment enchantment, final int level) {
        Preconditions.checkNotNull(enchantment, "Enchantment cannot be null!");
        Preconditions.checkArgument(level > 0, "Level must be greater than 0!");

        this.meta.addEnchant(enchantment, level, true);
        applyMeta();
        return this;
    }

    /**
     * Remove an enchantment from the item.
     * @param enchantment The enchantment to remove.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder removeEnchantment(@NotNull final Enchantment enchantment) {
        Preconditions.checkNotNull(enchantment, "Enchantment cannot be null!");

        this.meta.removeEnchant(enchantment);
        applyMeta();
        return this;
    }

    /**
     * Set the item to be unbreakable.
     * @param showInToolTip Whether to show the unbreakable status in the tooltip.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setUnbreakable(boolean showInToolTip) {
        this.item.setData(DataComponentTypes.UNBREAKABLE, (DataComponentBuilder<Unbreakable>) () -> Unbreakable.unbreakable().showInTooltip(showInToolTip).build());
        return this;
    }

    /**
     * Add an item flag to the item.
     * @param flags The flags to add.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addItemFlags(@NotNull final ItemFlag... flags) {
        Preconditions.checkNotNull(flags, "Flags cannot be null!");
        Preconditions.checkArgument(flags.length > 0, "Flags cannot be empty!");

        this.meta.addItemFlags(flags);
        applyMeta();
        return this;
    }

    /**
     * Remove an item flag from the item.
     * @param flags The flags to remove.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder removeItemFlags(@NotNull final ItemFlag... flags) {
        Preconditions.checkNotNull(flags, "Flags cannot be null!");
        Preconditions.checkArgument(flags.length > 0, "Flags cannot be empty!");

        this.meta.removeItemFlags(flags);
        applyMeta();
        return this;
    }

    /**
     * Set the skull texture of the item.
     * @param player The player to set the skull texture to.
     * @return The current instance of the builder.
     * @throws ClassCastException If the item is not a skull.
     */
    @CheckReturnValue
    public ItemBuilder setSkullOwner(@NotNull final OfflinePlayer player) {
        Preconditions.checkNotNull(player, "Player cannot be null!");

        SkullMeta skullMeta = (SkullMeta) this.meta;
        skullMeta.setOwningPlayer(player);
        this.item.setItemMeta(skullMeta);
        applyMeta();
        return this;
    }

    /**
     * Set the skull texture of the item.
     * @param profile The profile to set the skull texture to.
     * @return The current instance of the builder.
     * @throws ClassCastException If the item is not a skull.
     */
    @CheckReturnValue
    public ItemBuilder setSkullOwner(@NotNull final PlayerProfile profile) {
        Preconditions.checkNotNull(profile, "Profile cannot be null!");
        SkullMeta skullMeta = (SkullMeta) this.meta;
        skullMeta.setPlayerProfile(profile);

        this.item.setItemMeta(skullMeta);
        applyMeta();
        return this;
    }

    /**
     * Set the custom model data of the item.
     * @param data The custom model data to set.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder setCustomModelData(@NotNull final Integer data) {
        this.meta.setCustomModelData(data);
        applyMeta();
        return this;
    }

    /**
     * Set the color of the item.
     * @param color The color to set.
     * @return The current instance of the builder.
     * @throws ClassCastException If the item is not a leather armor piece.
     */
    @CheckReturnValue
    public ItemBuilder setLeatherColor(@NotNull final Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.meta;
        leatherArmorMeta.setColor(color);

        this.item.setItemMeta(leatherArmorMeta);
        applyMeta();
        return this;
    }

    /**
     * Apply custom persistent data to the item.
     * @param function The function to apply the persistent data.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder applyPersistentData(@NotNull final Consumer<PersistentDataContainer> function) {
        Preconditions.checkNotNull(function, "Function cannot be null!");

        function.accept(this.meta.getPersistentDataContainer());
        applyMeta();
        return this;
    }

    /**
     * Add custom persistent data to the item.
     * @param key The key to add the data to.
     * @param type The type of the data.
     * @param value The value of the data.
     * @return The current instance of the builder.
     * @param <T> The type of the data.
     * @param <V> The value of the data.
     */
    @CheckReturnValue
    public <T extends PersistentDataType<V, V>, V> ItemBuilder addNamespacedKey(@NotNull NamespacedKey key, @NotNull T type, @NotNull V value) {
        this.meta.getPersistentDataContainer().set(key, type, value);
        applyMeta();
        return this;
    }

    /**
     * Add an attribute modifier to the item.
     * @param attribute The attribute to add the modifier to.
     * @param modifier The modifier to add.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null!");
        Preconditions.checkNotNull(modifier, "Modifier cannot be null!");

        this.meta.addAttributeModifier(attribute, modifier);
        applyMeta();
        return this;
    }

    /**
     * Remove an attribute modifier from the item.
     * @param attribute The attribute to remove the modifier from.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder removeAttributeModifier(@NotNull final Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null!");

        this.meta.removeAttributeModifier(attribute);
        applyMeta();
        return this;
    }

    /**
     * Remove an attribute modifier from the item.
     * @param attribute The attribute to remove the modifier from.
     * @param modifier The modifier to remove.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder removeAttributeModifier(@NotNull final Attribute attribute, @NotNull final AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null!");
        Preconditions.checkNotNull(modifier, "Modifier cannot be null!");

        this.meta.removeAttributeModifier(attribute, modifier);
        applyMeta();
        return this;
    }

    /**
     * Set the item to be glowing.
     * @param value Whether the item should be glowing.
     * @return The current instance of the builder.
     */
    @CheckReturnValue
    public ItemBuilder glowing(boolean value) {
        item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, value);
        return this;
    }
    
    private void applyMeta() {
        this.item.setItemMeta(this.meta);
        this.meta = item.getItemMeta();
    }

    /**
     * Construct the {@link ItemStack} from the builder.
     * @return The constructed {@link ItemStack}.
     */
    @NotNull
    public ItemStack build() {
        applyMeta();
        return this.item;
    }

    /**
     * Get the {@link ItemMeta} of the item.
     *
     * @return The {@link ItemMeta} object representing the metadata of the item.
     */
    @NotNull
    public ItemMeta meta() {
        return this.meta;
    }

    /**
     * Returns the ItemStack associated with this ItemBuilder.
     *
     * @return The ItemStack.
     */
    @NotNull
    public ItemStack item() {
        return this.item;
    }

}
