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

package games.negative.alumina.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    /**
     * The name of the command
     * @return the name of the command
     */
    String name();

    /**
     * The aliases of the command
     * @return the aliases of the command
     */
    String[] aliases() default {};

    /**
     * The description of the command
     * @return the description of the command
     */
    String description() default "";

    /**
     * The usage of the command
     * @return the usage of the command
     */
    String usage() default "";

    /**
     * The permissions of the command
     * @return the permissions of the command
     */
    String permission() default "";

    /**
     * The required parameters of the command
     * @return the required parameters of the command
     */
    String[] params() default {};

    /**
     * The shortcuts of the command
     * @return the shortcuts of the command
     */
    String[] shortcuts() default {};

    /**
     * Whether the command can only be executed by a player
     * @return whether the command can only be executed by a player
     */
    boolean playerOnly() default false;

    /**
     * Whether the command can only be executed by the console
     * @return whether the command can only be executed by the console
     */
    boolean consoleOnly() default false;

    /**
     * Whether the command should use smart tab completion
     * @return whether the command should use smart tab completion
     */
    boolean smartTabComplete() default false;

    /**
     * Whether the command should be executed asynchronously
     * @return whether the command should be executed
     */
    boolean async() default false;

}
