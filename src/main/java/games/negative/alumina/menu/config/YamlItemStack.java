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

package games.negative.alumina.menu.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.exlll.configlib.Configuration;
import games.negative.alumina.builder.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a configurable item stack.
 */
@Getter
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class YamlItemStack {

    private String displayName = null;
    private Material material = null;
    private Integer amount = null;
    private Integer slot = null;
    private Boolean glowing = null;
    private List<String> lore = null;
    private Integer customModelData = null;

    /**
     * Creates a {@link ItemStack} in accordance with the properties of this YamlItemStack.
     * @return The ItemStack.
     */
    @NotNull
    @CheckReturnValue
    public Creator create() {
        return new Creator(this);
    }

    /**
     * Creates a new YamlItemStack with the specified properties.
     * @return The YamlItemStack.
     */
    @NotNull
    @CheckReturnValue
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link YamlItemStack}
     */
    public static class Builder {
        private String displayName = null;
        private Material material = null;
        private Integer amount = null;
        private Integer slot = null;
        private Boolean glowing = null;
        private List<String> lore = null;
        private Integer customModelData = null;

        /**
         * Sets the display name of the item.
         * @param displayName The display name.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder displayName(@Nullable String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the material of the item.
         * @param material The material.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder material(@NotNull Material material) {
            this.material = material;
            return this;
        }

        /**
         * Sets the amount of the item.
         * @param amount The amount.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder amount(int amount) {
            Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");

            this.amount = amount;
            return this;
        }

        /**
         * Sets the slot of the item.
         * @param slot The slot.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder slot(int slot) {
            Preconditions.checkArgument(slot >= 0, "Slot must be greater than or equal to 0");
            Preconditions.checkArgument(slot < 54, "Slot must be less than 54");

            this.slot = slot;
            return this;
        }

        /**
         * Sets the glowing state of the item.
         * @param glowing The glowing state.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder glowing(boolean glowing) {
            this.glowing = glowing;
            return this;
        }

        /**
         * Sets the lore of the item.
         * @param lore The lore.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder lore(@NotNull List<String> lore) {
            this.lore = lore;
            return this;
        }

        /**
         * Sets the lore of the item.
         * @param lore The lore.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder lore(@NotNull String... lore) {
            return lore(Lists.newArrayList(lore));
        }

        /**
         * Adds a line to the lore of the item.
         * @param line The line to add.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder addLoreLine(@NotNull String line) {
            if (lore == null) lore = Lists.newArrayList();

            lore.add(line);
            return this;
        }

        /**
         * Sets the custom model data of the item.
         * @param data The custom model data.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Builder customModelData(@NotNull Integer data) {
            Preconditions.checkArgument(data > 0, "custom-model-data must be greater than 0");

            this.customModelData = data;
            return this;
        }

        /**
         * Builds the item stack.
         * @return The item stack.
         */
        @NotNull
        @CheckReturnValue
        public YamlItemStack build() {
            return new YamlItemStack(
                displayName,
                material,
                amount,
                slot,
                glowing,
                lore,
                customModelData
            );
        }
    }

    /**
     * Creator class to map {@link YamlItemStack} into {@link ItemStack}
     */
    @RequiredArgsConstructor
    public static class Creator {

        private final YamlItemStack stack;
        private final Map<String, String> textPlaceholders = Maps.newConcurrentMap();
        private final Map<String, Component> componentPlaceholders = Maps.newConcurrentMap();

        /**
         * Replaces a placeholder with a string.
         * @param placeholder The placeholder to replace.
         * @param replacement The replacement string.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Creator replace(@NotNull String placeholder, @NotNull String replacement) {
            this.textPlaceholders.put(placeholder, replacement);
            return this;
        }

        /**
         * Replaces a placeholder with a component.
         * @param placeholder The placeholder to replace.
         * @param replacement The replacement component.
         * @return The builder instance.
         */
        @NotNull
        @CheckReturnValue
        public Creator replace(@NotNull String placeholder, @NotNull Component replacement) {
            this.componentPlaceholders.put(placeholder, replacement);
            return this;
        }

        /**
         * Creates the item stack.
         * @return The item stack.
         */
        @NotNull
        public ItemStack create() {
            Material material = stack.material;
            Preconditions.checkNotNull(material, "Material must not be null");

            Integer amount = stack.amount;
            ItemBuilder builder = new ItemBuilder(material, (amount == null ? 1 : amount));

            String displayName = stack.displayName;
            if (displayName != null) {
                String name = displayName;

                for (Map.Entry<String, String> entry : textPlaceholders.entrySet()) {
                    name = name.replace(entry.getKey(), entry.getValue());
                }

                Component component = ItemBuilder.MINIMESSAGE.deserialize(name);
                for (Map.Entry<String, Component> entry : componentPlaceholders.entrySet()) {
                    component = component.replaceText(TextReplacementConfig.builder().match(entry.getKey()).replacement(entry.getValue()).build());
                }

                builder.setName(component);
            }

            List<String> lore = stack.lore;
            if (lore != null && !lore.isEmpty()) {
                // Use java streams to replace placeholders in lore
                List<Component> components = lore.stream().map(s -> {
                    for (Map.Entry<String, String> entry : textPlaceholders.entrySet()) {
                        s = s.replace(entry.getKey(), entry.getValue());
                    }
                    return s;
                }).map(s -> {
                    Component component = ItemBuilder.MINIMESSAGE.deserialize(s);
                    for (Map.Entry<String, Component> entry : componentPlaceholders.entrySet()) {
                        component = component.replaceText(TextReplacementConfig.builder().match(entry.getKey()).replacement(entry.getValue()).build());
                    }
                    return component;
                }).collect(Collectors.toCollection(Lists::newArrayList));

                builder.setLore(components);
            }

            Integer customModelData = stack.customModelData;
            if (customModelData != null) {
                builder.setCustomModelData(customModelData);
            }

            return builder.build();
        }

    }

}
