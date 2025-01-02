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

package games.negative.alumina.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The Unique interface represents an object that has a universally unique identifier (UUID).
 *
 * <p>
 * Implementing classes must provide an implementation for the 'uuid' method, which returns
 * the unique identifier for the object.
 * </p>
 *
 * @see UUID
 */
public interface Unique {

    /**
     * Generates a universally unique identifier (UUID) for this object.
     *
     * @return A UUID representing the unique identifier for this object.
     *
     * @throws NullPointerException if the UUID is null.
     *
     * @see UUID
     */
    @NotNull
    UUID uuid();

}
