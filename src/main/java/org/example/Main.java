package org.example;


import org.example.property.PropertyName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    //TODO можно сделать чтение по байтам, найти значение точки и игнорировать его при сборке,
    // полученное число будет местоположением в массиве
    public static void main(String[] args) throws FileNotFoundException {

        long startTime = new Date().getTime();
        System.out.println(startTime);

        init();

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            IPv4FileReaderService frs = new IPv4FileReaderService(
                    new FileInputStream(ObjectFactory.createAppProperty().getProperty(
                            PropertyName.FILE_NAME
                    ))
            );
            Future<Boolean> frsExecutor = executor.submit(frs);
            IPv4QueueReaderService qrService = new IPv4QueueReaderService();
            Future<Long> qrExecutor = executor.submit(qrService);
            //TODO надо заменить на нормальное завершение
            if (frsExecutor.get()) {
                qrService.finishProcess();
            }
            //TODO надо заменить на нормальное завершение
            System.out.println("unique ips: " + qrExecutor.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(new Date().getTime() - startTime);
    }


    private static void init() {
        ObjectFactory.createAppProperty();
        ObjectFactory.createTransportBlockingQueue();
    }

}