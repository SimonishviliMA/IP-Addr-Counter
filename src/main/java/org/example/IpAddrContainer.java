package org.example;

import java.util.BitSet;

public class IpAddrContainer {

    private final BitSet[] bits;

    private final int maxSizeOfBitSet;
    private final int minSizeOfBitSet;

    private long sizeOfUniqueIPs = 0;

    public IpAddrContainer(long maxPossibleIPCount) {
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

    public void set(long bitInd) {
        int indOfArray = (int) Math.ceil((float) bitInd / maxSizeOfBitSet) - 1;
        if (indOfArray > bits.length) {
            //TODO выход за пределы массива битов
            throw new IndexOutOfBoundsException();
        }
        int indOfBitSet = (int) (bitInd - (maxSizeOfBitSet * indOfArray));
        if (bits.length == indOfArray) {
            if (indOfBitSet > minSizeOfBitSet) {
                //TODO выход за пределы сета битов
                throw new IndexOutOfBoundsException();
            }
        }
        if (!bits[indOfArray].get(indOfBitSet)) {
            bits[indOfArray].set(indOfBitSet);
            sizeOfUniqueIPs++;
        }
    }

    public long getSizeOfUniqueIPs() {
        return sizeOfUniqueIPs;
    }

}
