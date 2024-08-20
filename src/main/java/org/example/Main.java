package org.example;


import org.example.factory.SingletonObjectFactory;
import org.example.property.AppProperty;
import org.example.property.impl.PropertyName;
import org.example.service.IPv4AddrContainer;
import org.example.service.IPv4FileReaderService;
import org.example.service.impl.IPv4FileReaderServiceImpl;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static IPv4AddrContainer iPv4AddrContainer;
    private static AppProperty appProperty;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        long startTime = new Date().getTime();

        init();

        int quantityOfThreads = Integer.parseInt(appProperty.getProperty(
                PropertyName.QUANTITY_OF_THREADS
        ));

        try (ExecutorService executor = Executors.newFixedThreadPool(quantityOfThreads)) {

            Object[] frsTasks = new Object[quantityOfThreads];
            File ipv4File = new File(appProperty.getProperty(
                    PropertyName.FILE_NAME
            ));
            for (int i = 0; i < quantityOfThreads; i++) {
                IPv4FileReaderService frs = new IPv4FileReaderServiceImpl(
                        ipv4File,
                        i + 1,
                        quantityOfThreads
                );
                frsTasks[i] = executor.submit(frs);
            }
            for (Object frsTask : frsTasks) {
                ((Future<Boolean>) frsTask).get();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(
                "Quantity of unique IPs = " + iPv4AddrContainer.getQuantityOfUniqueIPs() +
                "\nProgram is finished. Time is " + (new Date().getTime() - startTime) + " ms."
        );
    }

    private static void init() {
        iPv4AddrContainer = SingletonObjectFactory.getInstanceOfIPv4AddrContainer();
        appProperty = SingletonObjectFactory.getInstanceOfAppProperty();
    }

}