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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents the context of a tab completion.
 * @param args The arguments of the command.
 * @param sender The sender of the command.
 */
public record TabContext(CommandSender sender, String[] args) {

    /**
     * Returns the player who executed the command.
     * @return the player who executed the command.
     */
    @NotNull
    public Optional<Player> player() {
        return sender() instanceof Player ? Optional.of((Player) sender()) : Optional.empty();
    }

    /**
     * Returns the argument at the specified index.
     * @param index The index of the argument.
     * @return the argument at the specified index.
     */
    @NotNull
    public Optional<String> argument(final int index) {
        return (index >= args.length ? Optional.empty() : Optional.of(args[index]));
    }

    /**
     * Returns the current argument used in the command.
     * @return the current argument.
     */
    @NotNull
    public String current() {
        return argument(args.length - 1).orElseThrow(() -> new IllegalStateException("No current argument"));
    }

    /**
     * Returns the index of the current argument.
     * @return the index of the current argument.
     */
    public int index() {
        return args.length - 1;
    }
}
