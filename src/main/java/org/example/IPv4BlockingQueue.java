package org.example;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IPv4BlockingQueue {

    private final Lock writeLock = new ReentrantLock();
    private final Lock readLock = new ReentrantLock();
    private final Condition notFull = writeLock.newCondition();
    private final Condition notEmpty = readLock.newCondition();

    private final int maxCapacity;
    private final double upperThreshold;
    private final double lowerThreshold;

    private final long[] queue;
    //TODO size should be atomicity
    private final AtomicInteger size = new AtomicInteger();
    private int offset;
    private int nextIndex;
    private boolean finish;

    /**
     * Creating IPv4BlockingQueue
     * @param upperThreshold - percent from Integer.MAX_VALUE that is max value of elements in queue. Should be from 0.0 to 1.0
     * @param lowerThreshold - percent from Integer.MAX_VALUE that is min value of elements in queue. Should be from 0.0 to 1.0
     */
    public IPv4BlockingQueue(int maxCapacity, double upperThreshold, double lowerThreshold) {
        this.maxCapacity = maxCapacity;
        this.upperThreshold = upperThreshold;
        this.lowerThreshold = lowerThreshold;
        this.queue = new long[maxCapacity];
    }


    public void put(long ip) throws InterruptedException {
        writeLock.lock();
        try {
            while (size.get() == maxCapacity) {
                notFull.await();
            }
            if (nextIndex > maxCapacity * upperThreshold) {
                nextIndex = 0;
            }
            if (size.get() >= maxCapacity * upperThreshold) {
                readLock.lock();
                try {
                    notEmpty.signalAll();
                } finally {
                    readLock.unlock();
                }
            }
            size.addAndGet(1);
            queue[nextIndex++] = ip;
//            System.out.println(LocalTime.now() + " put : " + ip);
        } finally {
            writeLock.unlock();
        }
    }

    public long take() throws InterruptedException {
        readLock.lock();
        try {
            while (size.get() <= 0) {
                notEmpty.await();
            }
            if (offset > maxCapacity * upperThreshold) {
                offset = 0;
            }
            if (size.get() <= maxCapacity * lowerThreshold) {
                writeLock.lock();
                try {
                    notFull.signalAll();
                } finally {
                    writeLock.unlock();
                }
            }
            size.addAndGet(-1);
//            System.out.println(LocalTime.now() + " take : " + queue[offset]);
            return queue[offset++];
        } finally {
            readLock.unlock();
        }
    }

    public boolean isEmpty() {
        return size.get() == 0;
    }

    public void finish() {
        finish = true;
        notEmpty.signalAll();
    }
}
