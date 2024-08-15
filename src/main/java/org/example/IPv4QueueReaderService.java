package org.example;

import org.example.queue.TransportBlockingQueue;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class IPv4QueueReaderService implements Callable<Long> {

    private static final long MAX_POSSIBLE_IPV4_COUNT = 4_294_967_296L;
    private static final IpAddrContainer ipAddrContainer = new IpAddrContainer(MAX_POSSIBLE_IPV4_COUNT);
    private final TransportBlockingQueue queue = ObjectFactory.createTransportBlockingQueue();

    private boolean finish;

    private void startWrite() {
        try {
            while (!finish || queue.isNotEmpty()) {
                long ip = queue.take();
                synchronized (this) {
                    ipAddrContainer.set(ip);
                }
            }
            System.out.println("finish = " + finish + " : is queue empty = " + !queue.isNotEmpty() + ". Time : " + LocalDateTime.now());
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
