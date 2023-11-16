/*
 *  MIT License
 *
 * Copyright (C) 2023 Negative Games
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

import java.util.List;

/**
 * This interface is used to mark a class as a command.
 *
 *  <p>
 * This class is a wrapper for {@link org.bukkit.command.Command} that
 * allows for easier command creation.
 * <p>
 *
 */
public interface Command {

    /**
     * This method is called when the command is executed.
     * @param context The context of the command.
     */
    void execute(Context context);

    /**
     * This method is called when the command is tab completed.
     * @param context The context of the command.
     * @return a list of possible completions for the command.
     * @apiNote This method is optional, override to add proper functionality.
     */
    default List<String> onTabComplete(TabContext context) {
        return null;
    }
}
