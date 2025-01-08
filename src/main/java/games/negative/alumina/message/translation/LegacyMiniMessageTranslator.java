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

package games.negative.alumina.message.translation;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@UtilityClass
@ApiStatus.Internal
public class LegacyMiniMessageTranslator {

    private final Map<String, String> legacyToMiniMessage = Maps.newConcurrentMap();

    /**
     * Translates a legacy message to a MiniMessage message.
     * @param content The content to translate.
     * @return The translated content.
     * @apiNote This method is internal and should not be used by external plugins.
     */
    @NotNull
    @ApiStatus.Internal
    public String legacyToMiniMessage(@NotNull String content) {
        if (legacyToMiniMessage.isEmpty()) {
            legacyToMiniMessage.put("0", "<black>");
            legacyToMiniMessage.put("1", "<dark_blue>");
            legacyToMiniMessage.put("2", "<dark_green>");
            legacyToMiniMessage.put("3", "<dark_aqua>");
            legacyToMiniMessage.put("4", "<dark_red>");
            legacyToMiniMessage.put("5", "<dark_purple>");
            legacyToMiniMessage.put("6", "<gold>");
            legacyToMiniMessage.put("7", "<gray>");
            legacyToMiniMessage.put("8", "<dark_gray>");
            legacyToMiniMessage.put("9", "<blue>");
            legacyToMiniMessage.put("a", "<green>");
            legacyToMiniMessage.put("b", "<aqua>");
            legacyToMiniMessage.put("c", "<red>");
            legacyToMiniMessage.put("d", "<light_purple>");
            legacyToMiniMessage.put("e", "<yellow>");
            legacyToMiniMessage.put("f", "<white>");
            legacyToMiniMessage.put("k", "<obfuscated>");
            legacyToMiniMessage.put("l", "<bold>");
            legacyToMiniMessage.put("m", "<strikethrough>");
            legacyToMiniMessage.put("n", "<underline>");
            legacyToMiniMessage.put("o", "<italic>");
            legacyToMiniMessage.put("r", "<reset>");
        }

        for (Map.Entry<String, String> entry : legacyToMiniMessage.entrySet()) {
            content = content.replaceAll("&" + entry.getKey(), entry.getValue());
        }

        return content;
    }

}
