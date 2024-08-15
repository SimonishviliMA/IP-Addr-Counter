package org.example.service;

import java.util.concurrent.Callable;

public interface IPv4QueueReaderService extends Callable<Long> {
    void finishProcess();
}
