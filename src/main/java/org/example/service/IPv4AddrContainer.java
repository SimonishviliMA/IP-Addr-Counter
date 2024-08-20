package org.example.service;

import java.util.BitSet;

public interface IPv4AddrContainer {

    /**
     * Set true value to {@link BitSet} at bitInd
     * @param bitInd IPv4 in decimal system presentation
     */
    void set(long bitInd);

    /**
     * Summing all true values from {@link BitSet} and returning them
     * @return Quantity of unique ips from that container
     */
    long getQuantityOfUniqueIPs();
}
