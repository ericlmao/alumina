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

package games.negative.alumina.event;

import com.google.common.base.Preconditions;
import games.negative.alumina.AluminaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Interface for more simply working with events and calling events.
 */
public class Events<T extends Event> implements Listener, EventExecutor {

    private final UUID uuid = UUID.randomUUID();
    private final Class<T> type;
    private final Consumer<T> listener;
    private final EventPriority priority;
    private final boolean ignoreCancelled;

    public Events(@NotNull Class<T> type, @NotNull Consumer<T> listener, @Nullable EventPriority priority, boolean ignoreCancelled) {
        this.type = type;
        this.listener = listener;
        this.priority = (priority == null ? EventPriority.NORMAL : priority);
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * Invoke an event.
     * @param event Event to invoke
     * @param <T> Event type
     */
    public static <T extends Event> void call(@NotNull final T event) {
        Preconditions.checkNotNull(event, "Event cannot be null");

        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Listen to an event.
     * @param type Event type
     * @param listener Event listener
     * @return Events
     * @param <T> Event type
     */
    public static <T extends Event> Events<T> listen(@NotNull final Class<T> type, @NotNull final Consumer<T> listener) {
        Preconditions.checkNotNull(type, "Event type cannot be null");
        Preconditions.checkNotNull(listener, "Event listener cannot be null");

        return listen(type, EventPriority.NORMAL, listener);
    }

    /**
     * Listen to an event.
     * @param type Event type
     * @param priority Event priority
     * @param listener Event listener
     * @return Events
     * @param <T> Event type
     */
    public static <T extends Event> Events<T> listen(@NotNull final Class<T> type, @NotNull final EventPriority priority, @NotNull final Consumer<T> listener) {
        Preconditions.checkNotNull(type, "Event type cannot be null");
        Preconditions.checkNotNull(priority, "Event priority cannot be null");
        Preconditions.checkNotNull(listener, "Event listener cannot be null");

        Events<T> events = new Events<>(type, listener, priority, false);

        Bukkit.getPluginManager().registerEvent(type, events, priority, events, AluminaPlugin.getAluminaInstance());

        return events;
    }

    /**
     * Listen to an event.
     * @param type Event type
     * @param priority Event priority
     * @param ignoreCancelled Ignore cancelled events
     * @param listener Event listener
     * @return Events
     * @param <T> Event type
     */
    public static <T extends Event> Events<T> listen(@NotNull final Class<T> type, @NotNull final EventPriority priority, final boolean ignoreCancelled, @NotNull final Consumer<T> listener) {
        Preconditions.checkNotNull(type, "Event type cannot be null");
        Preconditions.checkNotNull(priority, "Event priority cannot be null");
        Preconditions.checkNotNull(listener, "Event listener cannot be null");

        Events<T> events = new Events<>(type, listener, priority, ignoreCancelled);

        Bukkit.getPluginManager().registerEvent(type, events, priority, events, AluminaPlugin.getAluminaInstance(), ignoreCancelled);

        return events;
    }

    /**
     * Listen to an event.
     * @param listener Event listener
     */
    public static void listen(@NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, AluminaPlugin.getAluminaInstance());
    }

    /**
     * Listen to multiple events.
     * @param listeners Event listeners
     */
    public static void listen(@NotNull Listener... listeners) {
        for (Listener listener : listeners) {
            listen(listener);
        }
    }

    /**
     * Listen to an event.
     * @param bridge Event bridge
     * @param <T> Event type
     */
    public static <T extends Event> void listen(@NotNull Events<T> bridge) {
        Bukkit.getPluginManager().registerEvent(bridge.type, bridge, bridge.priority, bridge, AluminaPlugin.getAluminaInstance(), bridge.ignoreCancelled);
    }

    /**
     * Check if the event is registered.
     * @return true if the event is registered, false otherwise
     */
    public boolean isRegistered() {
        return HandlerList.getRegisteredListeners(AluminaPlugin.getAluminaInstance()).stream()
                .filter(registeredListener -> registeredListener.getListener() instanceof Events)
                .map(registeredListener -> (Events<?>) registeredListener.getListener())
                .anyMatch(events -> events.uuid.equals(this.uuid));

    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        if (!type.isInstance(event)) return;

        this.listener.accept(type.cast(event));
    }
}
