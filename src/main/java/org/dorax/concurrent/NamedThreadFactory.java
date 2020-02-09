package org.dorax.concurrent;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuchunfu
 * @date 2020-02-08
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamedThreadFactory.class);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String mPrefix;

    private final boolean mDaemon;

    private final ThreadGroup mGroup;

    public NamedThreadFactory(String prefix) {
        this(prefix, true);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        mPrefix = prefix + "-thread-";
        mDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(final @NonNull Runnable runnable) {
        Runnable safeRunnable = () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                LOGGER.error("process error", e);
            }
        };
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, safeRunnable, name, 0);
        ret.setDaemon(mDaemon);
        return ret;
    }
}