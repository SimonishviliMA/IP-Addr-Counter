package org.example.service.impl;

import java.util.BitSet;

class IpAddrContainer {

    private final BitSet[] bits;

    private final int maxSizeOfBitSet;
    private final int minSizeOfBitSet;

    private long sizeOfUniqueIPs = 0;

    protected IpAddrContainer(long maxPossibleIPCount) {
        long countOfElements = maxPossibleIPCount;
        this.maxSizeOfBitSet = Integer.MAX_VALUE;
        double floatSizeOfBitSetArray = (double) countOfElements / maxSizeOfBitSet;

        int sizeOfBitSetArray = (
                floatSizeOfBitSetArray % 1 == 0 ?
                        (int) floatSizeOfBitSetArray :
                        (int) floatSizeOfBitSetArray + 1
        );
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
            sizeOfUniqueIPs++;
        }
    }

    protected long getSizeOfUniqueIPs() {
        return sizeOfUniqueIPs;
    }

}
