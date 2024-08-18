package org.example.service.impl;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicLong;

public class IPv4AddrContainer {

    private static final long MAX_POSSIBLE_IPV4_QUANTITY = 4_294_967_296L;

    private static IPv4AddrContainer instance = null;

    private final BitSet[] bits;

    private final int maxSizeOfBitSet;
    private final int minSizeOfBitSet;

    private final AtomicLong sizeOfUniqueIPs = new AtomicLong();

    private IPv4AddrContainer() {
        long countOfElements = MAX_POSSIBLE_IPV4_QUANTITY;
        this.maxSizeOfBitSet = Integer.MAX_VALUE;

        int sizeOfBitSetArray = (int) (countOfElements / maxSizeOfBitSet) + 1;
        bits = new BitSet[sizeOfBitSetArray];
        int i = 0;
        while (countOfElements > 0) {
            if (countOfElements > maxSizeOfBitSet) {
                countOfElements -= maxSizeOfBitSet;
                bits[i++] = new BitSet(maxSizeOfBitSet);
            } else {
                bits[i] = new BitSet((int)countOfElements);
                countOfElements = 0;
            }
        }
        this.minSizeOfBitSet = bits[i].length();
    }

    public static IPv4AddrContainer getInstance() {
        if (instance == null) {
            instance = new IPv4AddrContainer();
        }
        return instance;
    }

    protected void set(long bitInd) {
        int indOfArray = 0;
        if (bitInd != 0) {
            indOfArray = (int) Math.ceil((double) (bitInd / maxSizeOfBitSet));
        }
        if (indOfArray > bits.length) {
            throw new IndexOutOfBoundsException(
                    "Out of bounds the BitSet array. Length of array = " + bits.length + ", index = " + indOfArray
            );
        }

        int indOfBitSet = (int) (bitInd - (maxSizeOfBitSet * indOfArray));
        if (bits.length == indOfArray) {
            if (indOfBitSet > minSizeOfBitSet) {
                throw new IndexOutOfBoundsException(
                        "Out of bounds the BitSet. Length of BitSet = " + bits.length + ", index = " + indOfBitSet
                );
            }
        }
        if (!bits[indOfArray].get(indOfBitSet)) {
            bits[indOfArray].set(indOfBitSet);
            sizeOfUniqueIPs.incrementAndGet();
        }
    }

    public long getSizeOfUniqueIPs() {
        return sizeOfUniqueIPs.get();
    }

}
