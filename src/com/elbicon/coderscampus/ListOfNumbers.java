package com.elbicon.coderscampus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ListOfNumbers {
    TaskDto taskDto = new TaskDto();
    List<Integer> numbers = new ArrayList<>();
    public void read() throws ExecutionException, InterruptedException {

        ExecutorService executorServiceCPU = Executors.newFixedThreadPool(10);
        ExecutorService executorServiceIO = Executors.newCachedThreadPool();
        Assignment8 assignment8 = new Assignment8();


        for (int i = 0; i < 10; i++) {
/*        CompletableFuture<List<Integer>> cf = CompletableFuture.supplyAsync(() -> assignment8, executorServiceIO)
                .thenApplyAsync(Assignment8::getNumbers);*/
            CompletableFuture.supplyAsync(() -> assignment8, executorServiceIO)
                    .thenApplyAsync(Assignment8::getNumbers)
                    .get();
        }
        System.out.println("Done");
    }

    private void addNumbersToList(Integer number){
        numbers.add(number);
    }

}
