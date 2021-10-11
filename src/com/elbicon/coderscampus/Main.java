package com.elbicon.coderscampus;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ListOfNumbers ListOfNumbers = new ListOfNumbers();
        ListOfNumbers.read();
    }
}
