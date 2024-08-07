package org.example;

import java.io.*;
import java.util.Arrays;

public class IPv4FileReaderService implements Runnable {

    private static final int MAX_8_BIT_VALUE = 256;
    private static final int MIN_NUMERIC_CHAR_VALUE = 48;
    private static final int MAX_NUMERIC_CHAR_VALUE = 57;
    private static final int DOT_CHAR_VALUE = 46;
    private static long countOfElements = 0;

    private final TransportBlockingQueue queue = TransportBlockingQueue.getInstance();

    private final FileInputStream src;

    public IPv4FileReaderService(FileInputStream src) {
        this.src = src;
    }


    private void startRead() {

        try (BufferedInputStream bis = new BufferedInputStream(src)) {
            int ch;
            long ipDecimalNumber = 0;
            int[] octetArr = getDefaultOctetArr();
            int octetArrInd = 0;
            int octetLeft = 3;
            do {
                ch = bis.read();
                if ((ch >= MIN_NUMERIC_CHAR_VALUE && ch <= MAX_NUMERIC_CHAR_VALUE) || ch == DOT_CHAR_VALUE) {
                    if (ch != DOT_CHAR_VALUE) {
                        octetArr[octetArrInd++] = ch;
                    } else {
                        ipDecimalNumber += getDecimalNumberOfOctet(getOctet(octetArr), octetLeft--);
                        octetArr = getDefaultOctetArr();
                        octetArrInd = 0;
                    }
                } else if (!Arrays.equals(octetArr, getDefaultOctetArr())) {
                    ipDecimalNumber += getDecimalNumberOfOctet(getOctet(octetArr), octetLeft);
                    octetArr = getDefaultOctetArr();
                    octetArrInd = 0;
                    queue.put(ipDecimalNumber);
                    ipDecimalNumber = 0;
                    octetLeft = 3;
                    if (countOfElements++ % (double) (Integer.MAX_VALUE / 2) == 0) {
                        System.out.println(countOfElements);
                    }
                }
            } while (ch != -1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] getDefaultOctetArr() {
        return new int[] {-1, -1, -1};
    }

    private long getDecimalNumberOfOctet(int octet, int octetLeft) {
        return (long) octet * (int) (Math.pow(MAX_8_BIT_VALUE, octetLeft));
    }

    private int getOctet(int[] octetArr) {

        int octet = 0;
        int actualLength = octetArr.length - 1;
        for (int j = actualLength; j >= 0 ; j--) {
            if (octetArr[j] != -1) {
                octet += Character.getNumericValue((char) octetArr[j]) * (int) Math.pow(10, actualLength - j);
            } else {
                actualLength--;
            }
        }
        return octet;
    }

    @Override
    public void run() {
        startRead();
    }
}
