package com.elbicon.coderscampus;


import javax.crypto.spec.PSource;
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
    TaskDto taskDto = new TaskDto();
    List<List<Integer>> numbers = new ArrayList<List<Integer>>();

    public void read() throws ExecutionException, InterruptedException {
        ExecutorService executorServiceCPU = Executors.newFixedThreadPool(10);
        ExecutorService executorServiceIO = Executors.newCachedThreadPool();

        Assignment8 assignment8 = new Assignment8();
        CompletableFuture<List<Integer>> cf = new CompletableFuture<>();
        int numbersMaxCount = 1000;

        for (int i = 0; i < numbersMaxCount; i++) {
            cf.supplyAsync(() -> assignment8, executorServiceCPU)
                    .thenApplyAsync(Assignment8::getNumbers)
                    .thenAcceptAsync(dto -> {
                        this.addNumbersToList(dto.stream()
                                .map(m -> m.intValue())
                                .collect(Collectors.toList()));
                    },executorServiceIO).join();
        }
        outputToConsole();

        // Further Reading Required on allOf as behaviour
        // did not match expectations
/*        for (int i = 0; i < numbersMaxCount; i++) {
            CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> assignment8, executorServiceIO)
                        .thenApplyAsync(Assignment8::getNumbers)
                        .thenAcceptAsync(dto ->{
                            this.addNumbersToList(dto.stream().map(m -> m.intValue()).collect(Collectors.toList()));
                        })
                ).join();
        }
        outputToConsole();*/
    }

    private void addNumbersToList(List<Integer> number) {
        number.stream().map(m -> m.intValue()).forEach(f -> {
            synchronized (taskDto) {
                taskDto.setNumber(number.get(f.intValue()));
            }
        });
    }

    private void outputToConsole() {
        List<Integer> dtoNumbers = taskDto.getNumber();
        Map<Integer, Long> counts =
                dtoNumbers.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        //System.out.println(counts);

        for (Map.Entry<Integer, Long> entry : counts.entrySet()) {
            //System.out.println("Key=" + entry.getKey() + " Value=" + entry.getValue());
            System.out.print(entry.getKey() + "=" + entry.getValue() + " ");
        }
    }
}
