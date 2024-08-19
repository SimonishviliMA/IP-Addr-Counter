package org.example.service.impl;

import org.example.service.IPv4AddrContainer;

import java.util.BitSet;

public class IPv4AddrContainerImpl implements IPv4AddrContainer {

    private static final long MAX_POSSIBLE_IPV4_QUANTITY = 4_294_967_296L;

    private static IPv4AddrContainerImpl instance = null;

    private final BitSet[] bits;

    private final int maxSizeOfBitSet;
    private final int minSizeOfBitSet;

    private long sizeOfUniqueIPs = 0;

    private IPv4AddrContainerImpl() {
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

    public synchronized static IPv4AddrContainerImpl getInstance() {
        if (instance == null) {
            instance = new IPv4AddrContainerImpl();
        }
        return instance;
    }

    @Override
    public void set(long bitInd) {
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

        synchronized (this) {
            if (!bits[indOfArray].get(indOfBitSet)) {
                bits[indOfArray].set(indOfBitSet);
                sizeOfUniqueIPs++;
            }
        }
    }

    @Override
    public long getSizeOfUniqueIPs() {
        return sizeOfUniqueIPs;
    }

}
