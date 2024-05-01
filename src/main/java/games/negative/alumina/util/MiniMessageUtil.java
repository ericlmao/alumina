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

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MiniMessageUtil is a utility class for translating messages using the MiniMessage library.
 */
@UtilityClass
public class MiniMessageUtil {

    private final MiniMessage mm = MiniMessage.miniMessage();

    /**
     * Translates the given message into a Component using the MiniMessage library.
     *
     * @param message the message to be translated
     * @return the translated Component
     */
    @NotNull
    public Component translate(@NotNull String message) {
        return mm.deserialize(message);
    }

    /**
     * Translates the given message into a Component using the MiniMessage library.
     *
     * @param message  the message to be translated
     * @param instance the MiniMessage instance to be used for translation.
     *                 If null, the default instance from MiniMessageUtil.mm will be used.
     * @return the translated Component
     */
    @NotNull
    public Component translate(@NotNull String message, @Nullable MiniMessage instance) {
        MiniMessage mm = instance == null ? MiniMessageUtil.mm : instance;
        return mm.deserialize(message);
    }

    /**
     * Searches for a specific piece of text in a given TextComponent.
     *
     * @param component The TextComponent to search in.
     * @param search The string to search for.
     * @return true if the component contains a matching TextComponent, false otherwise.
     */
    public boolean searchComponent(@NotNull TextComponent component, @NotNull String search) {
        return component.contains(Component.text(search), (component1, component2) -> {
            if (component1 instanceof TextComponent first && component2 instanceof TextComponent second) {
                return first.content().equalsIgnoreCase(search) && second.content().equalsIgnoreCase(search);
            }

            return false;
        });
    }
}
