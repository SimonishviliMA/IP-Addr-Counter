package org.example.service.impl;

import org.example.SingletonObjectFactory;
import org.example.queue.TransportBlockingQueue;
import org.example.service.IPv4QueueReaderService;

import java.time.LocalDateTime;

public class IPv4QueueReaderServiceImpl implements IPv4QueueReaderService {

    private static final long MAX_POSSIBLE_IPV4_COUNT = 4_294_967_296L;
    private static final IpAddrContainer ipAddrContainer = new IpAddrContainer(MAX_POSSIBLE_IPV4_COUNT);
    private final TransportBlockingQueue queue = SingletonObjectFactory.getInstanceOfTransportBlockingQueue();

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

    /**
     * Finishing reader process
     */
    @Override
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
