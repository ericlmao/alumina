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


package games.negative.alumina.future;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A task to complete in the future with a result.
 * Typically used for asynchronous tasks that have a "callback" functionality of sorts.
 * Exactly the same as {@link CompletableFuture} but using Bukkit Threads.
 * @param <T> The type of the result.
 */
public interface BukkitFuture<T> {

    /**
     * Supply a value to the future.
     * @param supplier The supplier to supply the value.
     * @return The future.
     */
    BukkitFuture<T> supply(@NotNull Supplier<T> supplier);

    /**
     * Supply a value to the future asynchronously.
     * @param supplier The supplier to supply the value.
     * @return The future.
     */
    BukkitFuture<T> supplyAsync(@NotNull Supplier<T> supplier);

    /**
     * Supply a value to the future asynchronously.
     * @param task The task to supply the value.
     */
    void whenComplete(@NotNull Consumer<T> task);

    /**
     * Supply a value to the future asynchronously.
     * @param task The task to supply the value.
     */
    void whenCompleteAsync(@NotNull Consumer<T> task);

    /**
     * Cancel the future task.
     */
    void cancel();
}
