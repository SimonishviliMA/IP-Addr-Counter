package org.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IPv4BlockingQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition queueFullCondition = lock.newCondition();
    private final Condition queueEmptyCondition = lock.newCondition();

    private final int maxCapacity;
    private final int delta;

    private final long[] queue;
    //TODO size should be atomicity
    private final AtomicInteger size = new AtomicInteger();
    private int offset;
    private int nextIndex;
    private boolean finish;


    //TODO добавить валидацию и описание delta
    public IPv4BlockingQueue(int maxCapacity, int delta) {
        this.maxCapacity = maxCapacity;
        this.queue = new long[maxCapacity];
        this.delta = delta;
    }


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
