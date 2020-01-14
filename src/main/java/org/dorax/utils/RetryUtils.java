package org.dorax.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Retry 工具类
 *
 * @author wuchunfu
 * @date 2020-01-14
 */
public class RetryUtils {
    private static final Logger log = LoggerFactory.getLogger(RetryUtils.class);

    private RetryUtils() {
    }

    public static void retryWithBackoff(
            final int maxRetries,
            final int initialWaitMs,
            final int maxWaitMs,
            final Runnable runnable,
            final Class<?>... passThroughExceptions) {
        retryWithBackoff(
                maxRetries,
                initialWaitMs,
                maxWaitMs,
                runnable,
                Arrays.stream(passThroughExceptions)
                        .map(c -> (Predicate<Exception>) c::isInstance)
                        .collect(Collectors.toList()));
    }

    public static void retryWithBackoff(
            final int maxRetries,
            final int initialWaitMs,
            final int maxWaitMs,
            final Runnable runnable,
            final List<Predicate<Exception>> passThroughExceptions) {
        retryWithBackoff(maxRetries, initialWaitMs, maxWaitMs, runnable, duration -> {
                    try {
                        Thread.sleep(duration);
                    } catch (final InterruptedException e) {
                        log.debug("retryWithBackoff interrupted while sleeping");
                    }
                },
                passThroughExceptions
        );
    }

    static void retryWithBackoff(
            final int maxRetries,
            final int initialWaitMs,
            final int maxWaitMs,
            final Runnable runnable,
            final Consumer<Long> sleep,
            final List<Predicate<Exception>> passThroughExceptions) {
        long wait = initialWaitMs;
        int i = 0;
        while (true) {
            try {
                runnable.run();
                return;
            } catch (final RuntimeException exception) {
                passThroughExceptions.stream()
                        .filter(pte -> pte.test(exception))
                        .findFirst()
                        .ifPresent(e -> {
                            throw exception;
                        });
                i++;
                if (i > maxRetries) {
                    throw exception;
                }
                final StringWriter stringWriter = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(stringWriter);
                exception.printStackTrace(printWriter);
                log.error("Exception encountered running command: {}. Retrying in {} ms", exception.getMessage(), wait);
                log.error("Stack trace: " + stringWriter.toString());
                sleep.accept(wait);
                wait = wait * 2 > maxWaitMs ? maxWaitMs : wait * 2;
            }
        }
    }

}
