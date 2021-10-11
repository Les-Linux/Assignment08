package com.elbicon.coderscampus;


import javax.crypto.spec.PSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class ListOfNumbers {
    static TaskDto taskDto = new TaskDto();
    List<List<Integer>> numbers = new ArrayList<List<Integer>>();

    public void read() throws ExecutionException, InterruptedException, IOException {
        ExecutorService executorServiceCPU = Executors.newFixedThreadPool(12);
        ExecutorService executorServiceIO = Executors.newCachedThreadPool();

        Assignment8 assignment8 = new Assignment8();
        CompletableFuture<List<Integer>> completableFuture = new CompletableFuture<>();
        List<CompletableFuture<List<Integer>>> cfTasks = new ArrayList<>();
        int numbersMaxCount = 1000;

        for (int i = 0; i < numbersMaxCount; i++) {
            CompletableFuture cfTask =
                    completableFuture.supplyAsync(() -> assignment8.getNumbers(), executorServiceCPU)
                            .thenAcceptAsync(dto -> {
                                dto.stream()
                                        .forEach(f -> {
                                            this.addNumbersToList(f.intValue()); //add numbers to list
                                        });
                            }, executorServiceCPU);
            cfTasks.add(cfTask);
        }
        while (cfTasks.stream().filter(CompletableFuture::isDone).count() < numbersMaxCount) {
            // await all asynch to complete
        }
        outputToConsole(); //output results
    }

    private void addNumbersToList(Integer number) {
        synchronized (this) {
            taskDto.setNumber(number); //update static list
        }
    }

    private void outputToConsole() {
        List<Integer> dtoNumbers = taskDto.getNumber();
        Map<Integer, Long> counts =
                dtoNumbers.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        for (Map.Entry<Integer, Long> entry : counts.entrySet()) {
            //System.out.println("Key=" + entry.getKey() + " Value=" + entry.getValue());
            System.out.print(entry.getKey() + "=" + entry.getValue() + " ");
        }
    }
}
