package org.dorax.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * Retry 工具类
 *
 * @author wuchunfu
 * @date 2020-01-14
 */
public class ExecutorUtils {

    private static final int NUM_RETRIES = 5;
    private static final Duration RETRY_BACKOFF_MS = Duration.ofMillis(500);
    private static final Logger log = LoggerFactory.getLogger(ExecutorUtils.class);

    private ExecutorUtils() {
    }

    public enum RetryBehaviour {
        /**
         * 1. always
         * 2. on_retryable
         */
        ALWAYS,
        ON_RETRYABLE
    }

    @FunctionalInterface
    public interface Function {
        void call() throws Exception;
    }

    public static void executeWithRetries(final Function function, final RetryBehaviour retryBehaviour) throws Exception {
        executeWithRetries(() -> {
            function.call();
            return null;
        }, retryBehaviour);
    }

    public static <T> T executeWithRetries(final Callable<T> executable, final RetryBehaviour retryBehaviour) throws Exception {
        return executeWithRetries(executable, retryBehaviour, () -> RETRY_BACKOFF_MS);
    }

    static <T> T executeWithRetries(
            final Callable<T> executable,
            final RetryBehaviour retryBehaviour,
            final Supplier<Duration> retryBackOff
    ) throws Exception {
        Exception lastException = null;
        for (int retries = 0; retries < NUM_RETRIES; ++retries) {
            try {
                if (retries != 0) {
                    Thread.sleep(retryBackOff.get().toMillis());
                }
                return executable.call();
            } catch (final Exception e) {
                final Throwable cause = e instanceof ExecutionException ? e.getCause() : e;
                if (cause instanceof Exception && retryBehaviour == RetryBehaviour.ALWAYS) {
                    log.info("Retrying request. Retry no: " + retries, e);
                    lastException = e;
                } else if (cause instanceof Exception) {
                    throw (Exception) cause;
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        throw lastException;
    }
}
