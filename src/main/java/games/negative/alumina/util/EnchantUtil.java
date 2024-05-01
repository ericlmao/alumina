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
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * Utility class for enchantments which allows for easy access to enchantment keys and names
 */
@UtilityClass
public class EnchantUtil {

    private final Map<Enchantment, String> keys = Maps.newHashMap();
    private final Map<Enchantment, String> names = Maps.newHashMap();

    static {
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            keys.put(enchantment, enchantment.getKey().getKey());
            names.put(enchantment, formatName(enchantment));
        }
    }

    /**
     * Get the name of an enchantment
     * @param enchantment The enchantment
     * @return The name
     */
    @NotNull
    @CheckReturnValue
    public Optional<String> getKey(@NotNull Enchantment enchantment) {
        Preconditions.checkNotNull(enchantment, "enchantment cannot be null");

        return Optional.ofNullable(keys.get(enchantment));
    }

    /**
     * Get the formatted name of an enchantment
     * @param enchantment The enchantment
     * @return The formatted name
     */
    @NotNull
    @CheckReturnValue
    public Optional<String> getName(@NotNull Enchantment enchantment) {
        Preconditions.checkNotNull(enchantment, "enchantment cannot be null");

        return Optional.ofNullable(names.get(enchantment));
    }

    /**
     * Get the formatted name of an enchantment
     * @param enchantment The enchantment
     * @return The formatted name
     */
    public String formatName(@NotNull Enchantment enchantment) {
        String key = enchantment.getKey().getKey();

        // Split name by underscores and capitalize each word
        String[] split = key.split("_");
        StringBuilder builder = new StringBuilder();

        int index = 0;
        int last = split.length - 1;
        for (String s : split) {
            builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));

            if (index != last) builder.append(" ");

            index++;
        }

        return builder.toString().trim();
    }

}
