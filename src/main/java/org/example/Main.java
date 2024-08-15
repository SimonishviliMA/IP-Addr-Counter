package org.example;


import org.example.property.impl.PropertyName;
import org.example.service.IPv4FileReaderService;
import org.example.service.IPv4QueueReaderService;
import org.example.service.impl.IPv4FileReaderServiceImpl;
import org.example.service.impl.IPv4QueueReaderServiceImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        long startTime = new Date().getTime();
        System.out.println(startTime);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            IPv4FileReaderService frs = new IPv4FileReaderServiceImpl(
                    new FileInputStream(SingletonObjectFactory.getInstanceOfAppProperty().getProperty(
                            PropertyName.FILE_NAME
                    ))
            );
            Future<Boolean> frsExecutor = executor.submit(frs);
            IPv4QueueReaderService qrService = new IPv4QueueReaderServiceImpl();
            Future<Long> qrExecutor = executor.submit(qrService);

            if (frsExecutor.get()) {
                qrService.finishProcess();
            }

            System.out.println("unique ips: " + qrExecutor.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(new Date().getTime() - startTime);
    }

}