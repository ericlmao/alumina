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

import games.negative.alumina.util.Tasks;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A task to complete in the future with a result.
 * Typically used for asynchronous tasks that have a "callback" functionality of sorts.
 * Exactly the same as {@link java.util.concurrent.CompletableFuture} but using Bukkit Threads.
 * @param <T> The type of the result.
 */
public class BukkitCompletableFuture<T> implements BukkitFuture<T> {

    private static final Duration LIFETIME = Duration.ofMinutes(30);

    private AtomicReference<T> reference = null;
    private Consumer<T> task;
    private boolean async = false;
    private final BukkitTask bukkitTask;
    private final Instant initialization;

    public BukkitCompletableFuture() {
        this.initialization = Instant.now();

        this.bukkitTask = Tasks.async(() -> {
            // Cancel the task if it has been running for more than 30 minutes.
            if (Duration.between(initialization, Instant.now()).compareTo(LIFETIME) > 0) {
                this.cancel();
                return;
            }

            if (reference == null || task == null) return;

            T value = reference.get();
            if (async) {
                task.accept(value);
                this.cancel();
            } else {
                Tasks.run(() -> task.accept(value));
                this.cancel();
            }
        }, 0, 1);
    }

    @Override
    public BukkitFuture<T> supply(@NotNull Supplier<T> supplier) {
        if (this.reference == null) this.reference = new AtomicReference<>(null);
        Tasks.run(() -> this.reference.set(supplier.get()));
        return this;
    }

    @Override
    public BukkitFuture<T> supplyAsync(@NotNull Supplier<T> supplier) {
        if (this.reference == null) this.reference = new AtomicReference<>(null);
        Tasks.async(() -> this.reference.set(supplier.get()));
        return this;
    }

    @Override
    public void whenComplete(@NotNull Consumer<T> task) {
        this.task = task;
        this.async = false;
    }

    @Override
    public void whenCompleteAsync(@NotNull Consumer<T> task) {
        this.task = task;
        async = true;
    }

    @Override
    public void cancel() {
        this.reference = null;
        this.bukkitTask.cancel();
    }
}
