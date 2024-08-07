package org.example;

import java.util.concurrent.Callable;

public class IPv4QueueReaderService implements Callable<Long> {

    private static final long MAX_POSSIBLE_IPV4_COUNT = 4_294_967_296L;
    private static final IpAddrContainer ipAddrContainer = new IpAddrContainer(MAX_POSSIBLE_IPV4_COUNT);
    private final TransportBlockingQueue queue = TransportBlockingQueue.getInstance();

    private boolean finish;

    private void startWrite() {
        try {
            while (!finish || queue.isNotEmpty()) {
                long ip = queue.take();
                synchronized (this) {
                    ipAddrContainer.set(ip);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void finishProcess() {
        finish = true;
        queue.finish();
    }

    @Override
    public Long call() {
        startWrite();
        return ipAddrContainer.getSizeOfUniqueIPs();
    }


}
