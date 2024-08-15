package org.example.service.impl;

import org.example.SingletonObjectFactory;
import org.example.queue.TransportBlockingQueue;
import org.example.service.IPv4FileReaderService;

import java.io.*;

public class IPv4FileReaderServiceImpl implements IPv4FileReaderService {

    private static final int MAX_8_BIT_VALUE = 256;
    private static final int MIN_NUMERIC_CHAR_VALUE = 48;
    private static final int MAX_NUMERIC_CHAR_VALUE = 57;
    private static final int DOT_CHAR_VALUE = 46;

    private final TransportBlockingQueue queue = SingletonObjectFactory.getInstanceOfTransportBlockingQueue();

    private final FileInputStream src;

    public IPv4FileReaderServiceImpl(FileInputStream src) {
        this.src = src;
    }


    private void startRead() {

        try (BufferedInputStream bis = new BufferedInputStream(src)) {
            int ch;
            long ipDecimalNumber = 0;
            int octet = 0;
            int octetLeft = 3;
            do {
                ch = bis.read();
                if ((ch >= MIN_NUMERIC_CHAR_VALUE && ch <= MAX_NUMERIC_CHAR_VALUE) || ch == DOT_CHAR_VALUE) {
                    if (ch != DOT_CHAR_VALUE) {
                        octet = (octet * 10) + Character.getNumericValue((char) ch);
                    } else {
                        ipDecimalNumber += getDecimalNumberOfOctet(octet, octetLeft--);
                        octet = 0;
                    }
                } else {
                    ipDecimalNumber += getDecimalNumberOfOctet(octet, octetLeft);
                    octet = 0;
                    queue.put(ipDecimalNumber);
                    ipDecimalNumber = 0;
                    octetLeft = 3;
                }
            } while (ch != -1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private long getDecimalNumberOfOctet(int octet, int octetLeft) {
        return (long) octet * (int) (Math.pow(MAX_8_BIT_VALUE, octetLeft));
    }

    @Override
    public Boolean call() {
        startRead();
        return true;
    }
}
