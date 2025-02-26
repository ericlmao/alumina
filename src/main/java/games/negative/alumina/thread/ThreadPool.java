package games.negative.alumina.thread;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
public final class ThreadPool {

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * Get the thread pool
     * @return The thread pool
     */
    public ExecutorService get() {
        return pool;
    }
}
