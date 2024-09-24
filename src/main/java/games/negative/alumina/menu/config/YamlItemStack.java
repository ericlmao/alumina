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

package games.negative.alumina.menu.config;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.exlll.configlib.Configuration;
import games.negative.alumina.builder.ItemBuilder;
import games.negative.alumina.model.Pair;
import games.negative.alumina.util.MiniMessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a configurable item stack.
 */
@Getter
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class YamlItemStack {

    private static Enchantment GLOWING;

    private String displayName = null;
    private Material material = null;
    private Integer amount = null;
    private Integer slot = null;
    private Boolean glowing = null;
    private List<String> lore = null;
    private String headTextureValue = null;
    private String headTextureSignature = null;
    private Integer customModelData = null;

    /**
     * Converts this YamlItemStack to an {@link ItemStack}.
     * @return The ItemStack.
     */
    @NotNull
    public ItemStack asItemStack(@Nullable String... placeholders) {
        Preconditions.checkNotNull(material, "Material must not be null");

        ItemBuilder builder = new ItemBuilder(material, (amount == null ? 1 : amount));

        if (displayName != null) {
            String name = displayName;

            if (placeholders != null && placeholders.length > 1) {
                Preconditions.checkArgument(placeholders.length % 2 == 0, "Placeholders must be in pairs");
                for (int i = 0; i < placeholders.length; i += 2) {
                    name = name.replace(placeholders[i], placeholders[i + 1]);
                }
            }

            builder.setName(name);
        }

        if (lore != null && !lore.isEmpty()) {
            builder.setLore(lore);

            if (placeholders != null && placeholders.length > 1) {
                List<Pair<String, String>> mapped = Lists.newArrayList();

                Preconditions.checkArgument(placeholders.length % 2 == 0, "Placeholders must be in pairs");
                for (int i = 0; i < placeholders.length; i += 2) {
                    mapped.add(new Pair<>(placeholders[i], placeholders[i + 1]));
                }

                for (Pair<String, String> pair : mapped) {
                    builder.replaceLore(pair.left(), pair.right());
                }
            }
        }

        if (material == Material.PLAYER_HEAD && headTextureValue != null && headTextureSignature != null) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", headTextureValue, headTextureSignature));

            builder.setSkullOwner(profile);
        }

        if (glowing != null && glowing) {
            builder.addEnchantment(GLOWING, 10);
            builder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (customModelData != null) {
            builder.setCustomModelData(customModelData);
        }

        return builder.build();
    }

    /**
     * Converts this YamlItemStack to an {@link ItemStack}.
     * @return The ItemStack.
     */
    @NotNull
    public ItemStack asItemStack(@Nullable Map.Entry<String, Component>... placeholders) {
        Preconditions.checkNotNull(material, "Material must not be null");

        ItemBuilder builder = new ItemBuilder(material, (amount == null ? 1 : amount));

        if (displayName != null) {
            builder.setName(displayName);

            if (placeholders != null) {
                for (Map.Entry<String, Component> entry : placeholders) {
                    builder.replaceName(entry.getKey(), entry.getValue());
                }
            }

        }

        if (lore != null && !lore.isEmpty()) {
            builder.setLore(lore);

            if (placeholders != null) {
                for (Map.Entry<String, Component> entry : placeholders) {
                    builder.replaceLore(entry.getKey(), entry.getValue());
                }
            }
        }

        if (material == Material.PLAYER_HEAD && headTextureValue != null && headTextureSignature != null) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", headTextureValue, headTextureSignature));

            builder.setSkullOwner(profile);
        }

        if (glowing != null && glowing) {
            builder.addEnchantment(GLOWING, 10);
            builder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (customModelData != null) {
            builder.setCustomModelData(customModelData);
        }

        return builder.build();
    }

    /**
     * Sets the glowing enchantment.
     * @param enchantment The enchantment.
     */
    public static void setGlowingEnchantment(@NotNull Enchantment enchantment) {
        GLOWING = enchantment;
    }
}
