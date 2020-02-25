package org.dorax.network;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SimpleFlowData
 *
 * @author wuchunfu
 * @date 2020-02-24
 */
public class SimpleFlowData {
    private int index = 0;
    private AtomicInteger[] data;
    private int average;
    private int slotCount;

    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    public SimpleFlowData(int slotCount, int interval) {
        this.slotCount = slotCount;
        data = new AtomicInteger[slotCount];
        for (int i = 0; i < data.length; i++) {
            data[i] = new AtomicInteger(0);
        }
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("flow control thread");
            t.setDaemon(true);
            return t;
        });
        timer.scheduleAtFixedRate(this::rotateSlot, interval, interval, TimeUnit.MILLISECONDS);
    }

    public int addAndGet(int count) {
        return data[index].addAndGet(count);
    }

    public int incrementAndGet() {
        return data[index].incrementAndGet();
    }

    public void rotateSlot() {
        int total = 0;
        for (int i = 0; i < slotCount; i++) {
            total += data[i].get();
        }
        average = total / slotCount;
        index = (index + 1) % slotCount;
        data[index].set(0);
    }

    public int getCurrentCount() {
        return data[index].get();
    }

    public int getAverageCount() {
        return this.average;
    }

    public int getSlotCount() {
        return this.slotCount;
    }

    public String getSlotInfo() {
        StringBuilder sb = new StringBuilder();
        int index = this.index + 1;
        for (int i = 0; i < slotCount; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(this.data[(i + index) % slotCount].get());
        }
        return sb.toString();
    }

    public int getCount(int prevStep) {
        prevStep = prevStep % this.slotCount;
        int index = (this.index + this.slotCount - prevStep) % this.slotCount;
        return this.data[index].intValue();
    }
}
