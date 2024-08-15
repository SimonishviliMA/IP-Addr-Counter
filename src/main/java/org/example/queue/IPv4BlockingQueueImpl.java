package org.example.queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IPv4BlockingQueueImpl implements IPv4BlockingQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition queueFullCondition = lock.newCondition();
    private final Condition queueEmptyCondition = lock.newCondition();

    private final int maxCapacity;
    private final int delta;

    private final long[] queue;

    private final AtomicInteger size = new AtomicInteger();
    private int offset;
    private int nextIndex;
    private boolean finish;


    /**
     * Creating queue which can work with 1 thread for each producer and consumer
     * @param maxCapacity - queue max capacity. Can't be more than {@link Integer#MAX_VALUE}
     * @param delta - help with thread conditions. Consumer will wait until producer puts enough data (delta) to queue.
     *              Producer will wait until consumer takes enough data (maxCapacity - delta) from queue.
     */
    public IPv4BlockingQueueImpl(int maxCapacity, int delta) {
        this.maxCapacity = maxCapacity;
        this.queue = new long[maxCapacity];
        this.delta = delta;
    }


    /**
     * Put ip to the queue
     * @param ip - ip which converted to decimal system
     */
    @Override
    public void put(long ip) throws InterruptedException {
        while (size.get() > maxCapacity - 1) {
            lock.lock();
            try {
                queueFullCondition.await();
            } finally {
                lock.unlock();
            }
        }

        if (nextIndex > maxCapacity - 1) {
            nextIndex = 0;
        }

        queue[nextIndex++] = ip;
        size.addAndGet(1);
        if (size.get() >= delta) {
            lock.lock();
            try {
                queueEmptyCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Taking ip from queue
     * @return ip which was converted to decimal system
     */
    @Override
    public long take() throws InterruptedException {
        while (size.get() < 0 && !finish) {
            lock.lock();
            try {
                queueEmptyCondition.await();
            } finally {
                lock.unlock();
            }
        }

        if (offset > maxCapacity - 1) {
            offset = 0;
        }

        long ip = queue[offset++];
        size.addAndGet(-1);
        if (size.get() <= maxCapacity - delta) {
            lock.lock();
            try {
                queueFullCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
        return ip;
    }

    /**
     * @return true if empty else false
     */
    @Override
    public boolean isEmpty() {
        return size.get() == 0;
    }

    /**
     * Finishing process and giving signal to emptyCondition doesn't wait anymore.
     */
    @Override
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
