package org.example;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IPv4BlockingQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition queueFullCondition = lock.newCondition();
    private final Condition queueEmptyCondition = lock.newCondition();

    private final int upperThreshold;
    private final int lowerThreshold;
    private final int delta;

    private final long[] queue;
    //TODO size should be atomicity
    private final AtomicInteger size = new AtomicInteger();
    private int offset;
    private int nextIndex;
    private boolean finish;

    /**
     * Creating IPv4BlockingQueue
     * @param percentOfUpperThreshold - percent from Integer.MAX_VALUE that is max value of elements in queue. Should be from 0.0 to 1.0
     * @param percentOfLowerThreshold - percent from Integer.MAX_VALUE that is min value of elements in queue. Should be from 0.0 to 1.0
     */
    //TODO добавить валидацию
    public IPv4BlockingQueue(int maxCapacity, double percentOfUpperThreshold, double percentOfLowerThreshold) {
        this.upperThreshold = (int) Math.ceil(maxCapacity * percentOfUpperThreshold);
        this.lowerThreshold = (int) Math.ceil(maxCapacity * percentOfLowerThreshold);
        this.queue = new long[maxCapacity];
        this.delta = ((upperThreshold - lowerThreshold) / 3);
    }


    public void put(long ip) throws InterruptedException {
        lock.lock();
        try {
            while (size.get() > upperThreshold) {
                queueFullCondition.await();
            }

            if (nextIndex > upperThreshold) {
                nextIndex = 0;
            }

            queue[nextIndex++] = ip;
            size.addAndGet(1);
            if (size.get() >= lowerThreshold + delta) {
                lock.lock();
                try {
                    queueEmptyCondition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public long take() throws InterruptedException {
        lock.lock();
        try {
            while (size.get() < lowerThreshold && !finish) {
                queueEmptyCondition.await();
            }

            if (offset > upperThreshold) {
                offset = 0;
            }

            long ip = queue[offset++];
            size.addAndGet(-1);
            if (size.get() <= upperThreshold - delta) {
                lock.lock();
                try {
                    queueFullCondition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
            return ip;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return size.get() == 0;
    }

    public void finish() {
        lock.lock();
        try {
            finish = true;
            queueEmptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
