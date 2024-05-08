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

package games.negative.alumina.command;

import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * The properties of a command.
 * @param name The name of the command.
 * @param description The description of the command.
 * @param usage The usage of the command.
 * @param aliases The aliases of the command.
 * @param permissions The permissions required to execute the command.
 * @param params The required parameters of the command.
 * @param shortcuts The shortcuts of the command.
 * @param playerOnly Whether the command can only be executed by players.
 * @param consoleOnly Whether the command can only be executed by the console.
 * @param smartTabComplete Whether the command should use smart tab completion.
 * @param tabCompleteViewRequirement The predicate to test the players against for tab completion.
 */
@Builder
public record CommandProperties(
        @NotNull String name,
        @Nullable String description,
        @Nullable String usage,
        @Nullable List<String> aliases,
        @Nullable List<Permission> permissions,
        @Nullable List<String> params,
        @Nullable List<String> shortcuts,
        boolean playerOnly,
        boolean consoleOnly,
        boolean smartTabComplete,
        @Nullable Predicate<Player> tabCompleteViewRequirement
        ) {

}
