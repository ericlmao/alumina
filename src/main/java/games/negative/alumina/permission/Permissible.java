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


package games.negative.alumina.permission;

import org.bukkit.permissions.Permission;

/**
 * The Permissible interface represents an entity that has a permission associated with it.
 *
 * <p>
 * Implementing classes are expected to provide an implementation for the {@link #getPermission()} method, which should
 * return the associated permission of the entity.
 * </p>
 *
 * <p>
 * Additionally, the interface provides a default implementation for the {@link #setPermission()} method. However,
 * this method throws an {@link UnsupportedOperationException} as it is not supported for this class.
 * Implementing classes can choose to override this default implementation if needed.
 * </p>
 *
 */
public interface Permissible {

    /**
     * Returns the permission associated with the entity.
     *
     * @return the permission associated with the entity
     */
    Permission getPermission();

    /**
     * Sets the permission for the entity.
     *
     * <p>
     * By default, this method throws an {@link UnsupportedOperationException} as it is not supported for this class.
     * Implementing classes can choose to override this default implementation if needed.
     * </p>
     *
     * @throws UnsupportedOperationException if the method is not supported for this class
     */
    default void setPermission(Permission permission) {
        throw new UnsupportedOperationException("This method is not supported for this class");
    }

}
