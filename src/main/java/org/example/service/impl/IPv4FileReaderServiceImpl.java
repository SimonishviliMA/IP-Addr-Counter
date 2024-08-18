package org.example.service.impl;

import org.example.service.IPv4FileReaderService;

import java.io.*;

public class IPv4FileReaderServiceImpl implements IPv4FileReaderService {

    private static final int MAX_8_BIT_VALUE = 256;
    private static final int MIN_NUMERIC_CHAR_VALUE = 48;
    private static final int MAX_NUMERIC_CHAR_VALUE = 57;
    private static final int DOT_CHAR_VALUE = 46;
    private static final int NEXT_LINE_VALUE = 10;

    private final IPv4AddrContainer ipv4AddrContainer = IPv4AddrContainer.getInstance();

    private final File src;

    private final long start;
    private final long end;

    public IPv4FileReaderServiceImpl(File file, int numberOfSegment, int delta) {
        this.src = file;
        long fileLength = file.length();
        long exactEnd = numberOfSegment * (fileLength / delta);
        long exactStart = exactEnd - (fileLength / delta);
        this.start = exactStart == 0 ? 0 : nextIp(exactStart);
        this.end = exactEnd == fileLength ? exactEnd : nextIp(exactEnd);
    }

    private long nextIp(long byteBeacon) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src))) {
            long result = byteBeacon;
            bis.skipNBytes(result - 1);
            int ch = bis.read();
            while (ch != -1 && ch != NEXT_LINE_VALUE) {
                ch = bis.read();
                result++;
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void startRead() {

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src))) {
            bis.skipNBytes(start);
            long countOfBytes = start;
            int ch;
            long ipDecimalNumber = 0;
            int octet = 0;
            int octetLeft = 3;
            do {
                ch = bis.read();
                countOfBytes++;
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
                    ipv4AddrContainer.set(ipDecimalNumber);
                    ipDecimalNumber = 0;
                    octetLeft = 3;
                }
            } while (countOfBytes != end - 1);
        } catch (IOException e) {
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
