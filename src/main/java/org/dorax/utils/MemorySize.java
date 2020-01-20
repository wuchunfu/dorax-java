package org.dorax.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;

/**
 * 获取内存大小的工具类
 *
 * @author wuchunfu
 * @date 2020-01-11
 */
public class MemorySize {

    private final static Logger logger = LoggerFactory.getLogger(MemorySize.class);
    private static final int NUMBER_OF_OBJECTS = 10000;

    private static Object newObject(final ObjectFactory factory) {
        return factory.createObject();
    }

    public static boolean is64bitArch() {
        // Default to 64 e.g. if can't retrieve property
        boolean is64bit = true;
        try {
            String arch = System.getProperty("os.arch");
            if (arch != null) {
                is64bit = arch.contains("64");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is64bit;
    }

    public interface ObjectFactory {
        Object createObject();
    }

    public static int calculateSize(final ObjectFactory factory) {
        final Runtime runtime = Runtime.getRuntime();
        MemorySize.getMemorySize(runtime);
        MemorySize.newObject(factory);
        int i;
        long heap1;
        long heap2;
        long totalMemory1;
        long totalMemory2;
        // First we do a dry run with twice as many then throw away the results
        Object[] obj = new Object[MemorySize.NUMBER_OF_OBJECTS * 2];
        for (i = 0; i < MemorySize.NUMBER_OF_OBJECTS * 2; i++) {
            obj[i] = MemorySize.newObject(factory);
        }
        obj = new Object[MemorySize.NUMBER_OF_OBJECTS * 2];
        heap1 = MemorySize.getMemorySize(runtime);
        totalMemory1 = runtime.totalMemory();
        for (i = 0; i < MemorySize.NUMBER_OF_OBJECTS; i++) {
            obj[i] = MemorySize.newObject(factory);
        }
        heap2 = MemorySize.getMemorySize(runtime);
        totalMemory2 = runtime.totalMemory();
        final int size = Math.round((float) (heap2 - heap1) / MemorySize.NUMBER_OF_OBJECTS);
        if (totalMemory1 != totalMemory2) {
            logger.warn("Warning: JVM allocated more data what would make results invalid {}:{}", totalMemory1, totalMemory2);
        }
        return size;
    }

    private static long getMemorySize(final Runtime runtime) {
        for (int i = 0; i < 5; i++) {
            MemorySize.forceGC();
        }
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static void forceGC() {
        WeakReference<Object> dumbReference = new WeakReference<>(new Object());
        // A loop that will wait GC, using the minimal time as possible
        while (dumbReference.get() != null) {
            System.gc();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
