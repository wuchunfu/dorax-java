package org.dorax.network;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 根据IP进行流控, 控制单个IP的数量以及IP总量
 *
 * @author wuchunfu
 * @date 2020-02-24
 */
public class SimpleIpFlowData {

    private AtomicInteger[] data;

    private int slotCount;

    private int averageCount;

    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    public SimpleIpFlowData(int slotCount, int interval) {
        if (slotCount <= 0) {
            this.slotCount = 1;
        } else {
            this.slotCount = slotCount;
        }
        data = new AtomicInteger[slotCount];
        for (int i = 0; i < data.length; i++) {
            data[i] = new AtomicInteger(0);
        }
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("ip flow control thread");
            t.setDaemon(true);
            return t;
        });
        timer.scheduleAtFixedRate(new DefaultIpFlowDataManagerTask(), interval, interval, TimeUnit.MILLISECONDS);
    }

    class DefaultIpFlowDataManagerTask implements Runnable {

        @Override
        public void run() {
            rotateSlot();
        }

    }

    public int incrementAndGet(String ip) {
        int index = 0;
        if (ip != null) {
            index = ip.hashCode() % slotCount;
        }
        if (index < 0) {
            index = -index;
        }
        return data[index].incrementAndGet();
    }

    public void rotateSlot() {
        int totalCount = 0;
        for (int i = 0; i < slotCount; i++) {
            totalCount += data[i].get();
            data[i].set(0);
        }
        this.averageCount = totalCount / this.slotCount;
    }

    public int getCurrentCount(String ip) {
        int index = 0;
        if (ip != null) {
            index = ip.hashCode() % slotCount;
        }
        if (index < 0) {
            index = -index;
        }
        return data[index].get();
    }

    public int getAverageCount() {
        return this.averageCount;
    }
}
