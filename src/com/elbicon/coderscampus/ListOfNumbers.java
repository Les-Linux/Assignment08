package com.elbicon.coderscampus;


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
        ExecutorService executorServiceCPU = Executors.newFixedThreadPool(1);
        ExecutorService executorServiceIO = Executors.newCachedThreadPool();
        Assignment8 assignment8 = new Assignment8();
        CompletableFuture<List<Integer>> cf = new CompletableFuture<>();
        int numbersMaxCount = 2;

        for (int i = 0; i < numbersMaxCount; i++) {
            CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> assignment8, executorServiceIO)
                        .thenApplyAsync(Assignment8::getNumbers)
                        .thenAcceptAsync(dto ->{
                            this.addNumbersToList(dto.stream().map(m -> m.intValue()).collect(Collectors.toList()));
                        })
                ).join();
        } //end for
        outputToConsole();
/*        for (int i = 0; i < numbersMaxCount; i++) {
          cf.supplyAsync(() -> assignment8, executorServiceIO)
                    .thenApplyAsync(Assignment8::getNumbers)
                    .thenAcceptAsync(dto ->{
                        this.addNumbersToList(dto.stream().map(m -> m.intValue()).collect(Collectors.toList()));
                  });
        }*/


    }

    private void addNumbersToList(List<Integer> number){
        number.stream().map(m -> m.intValue()).forEach(f -> {
            synchronized (taskDto){
                taskDto.setNumber(number.get(f.intValue()));
            }
        });
    }
    private void outputToConsole(){
        List<Integer> dtoNumbers = taskDto.getNumber();
        Map<Integer, Long> counts =
                dtoNumbers.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        System.out.println("Counts= " + counts);
    }
}
