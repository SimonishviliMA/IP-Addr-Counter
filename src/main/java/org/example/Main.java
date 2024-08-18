package org.example;


import org.example.property.impl.PropertyName;
import org.example.service.IPv4FileReaderService;
import org.example.service.impl.IPv4FileReaderServiceImpl;
import org.example.service.impl.IPv4AddrContainer;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        long startTime = new Date().getTime();

        init();

        int quantityOfThreads = Integer.parseInt(
                SingletonObjectFactory.getInstanceOfAppProperty().getProperty(
                        PropertyName.QUANTITY_OF_THREADS
                )
        );

        try (ExecutorService executor = Executors.newFixedThreadPool(quantityOfThreads)) {

            Object[] frsTasks = new Object[quantityOfThreads];
            File ipv4File = new File(SingletonObjectFactory.getInstanceOfAppProperty().getProperty(
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
                "Quantity of unique ips = " + IPv4AddrContainer.getInstance().getSizeOfUniqueIPs() +
                "\nProgram is finished. Time is " + (new Date().getTime() - startTime) + " ms."
        );
    }

    private static void init() {
        IPv4AddrContainer.getInstance();
    }

}