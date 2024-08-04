package org.example;

import java.util.BitSet;

public class IPv4BlockingQueue {

    private final long[] queue = new long[Integer.MAX_VALUE];
    private final BitSet occupiedSet = new BitSet(Integer.MAX_VALUE);
    private int offset;
    private int nextIndex;
    //TODO size should be atomicity
    private int size;
    private boolean finish;

    public void put(long ip) {
        if (nextIndex < 0) {
            nextIndex = 0;
        }
        if (occupiedSet.get(nextIndex)) {
            occupiedSet.set(nextIndex);
            size++;
            queue[nextIndex++] = ip;
        } else {
            //TODO if that cell is occupied
        }
    }

    public long take() {
        if (offset < 0) {
            offset = 0;
        }
        if (!occupiedSet.get(offset)) {
            occupiedSet.set(offset, false);
            size--;
            return queue[offset++];
        } else {
            //TODO if queue is empty
        }
    }

    public void finish() {
        this.finish = true;
    }
}
