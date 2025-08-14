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

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Objects;

@UtilityClass
public class MaterialUtil {

    private final EnumMap<Material, String> PRETTY = Maps.newEnumMap(Material.class);

    static {
        for (Material m : Material.values()) {
            PRETTY.put(m, prettify(m));
        }
    }

    @NotNull
    public String getName(@NotNull Material material) {
        return Objects.requireNonNull(PRETTY.get(material));
    }

    private static String prettify(@NotNull Material material) {
        String raw = material.name();
        StringBuilder sb = new StringBuilder(raw.length() + 4);
        boolean capitalizeNext = true;

        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c == '_') {
                sb.append(' ');
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    sb.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

}
