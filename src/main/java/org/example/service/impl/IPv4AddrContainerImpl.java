package org.example.service.impl;

import org.example.service.IPv4AddrContainer;

import java.util.BitSet;

/**
 * Container for collecting IPv4 IPs and summing uniques IPs
 */
public class IPv4AddrContainerImpl implements IPv4AddrContainer {

    private static final long MAX_POSSIBLE_IPV4_QUANTITY = 4_294_967_296L;

    private static IPv4AddrContainerImpl instance = null;

    private final BitSet[] bits;

    private final int maxSizeOfBitSet;
    private final int minSizeOfBitSet;

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

    /**
     * @return Instance of {@link IPv4AddrContainer}
     */
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
                    "Out of bounds of the BitSet array. Length of array = " + bits.length + ", index = " + indOfArray
            );
        }

        int indOfBitSet = (int) (bitInd - (maxSizeOfBitSet * indOfArray));
        if (bits.length == indOfArray) {
            if (indOfBitSet > minSizeOfBitSet) {
                throw new IndexOutOfBoundsException(
                        "Out of bounds of the BitSet. Length of BitSet = " + bits.length + ", index = " + indOfBitSet
                );
            }
        }

        synchronized (this) {
            bits[indOfArray].set(indOfBitSet);
        }
    }

    @Override
    public long getQuantityOfUniqueIPs() {
        long sizeOfUniqueIPs = 0L;
        for (BitSet bitSet : bits) {
            sizeOfUniqueIPs += bitSet.cardinality();
        }
        return sizeOfUniqueIPs;
    }

}
