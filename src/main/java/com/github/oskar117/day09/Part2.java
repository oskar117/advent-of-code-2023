package com.github.oskar117.day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class Part2 {

    private final static String FILE_PATH = "input/day09/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        int result = Files.readAllLines(path).stream()
                .map(line -> Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray())
                .mapToInt(Part2::getPrevElement)
                .sum();
        System.out.println("Sum of extrapolated values is " + result);
    }

    private static int getPrevElement(int[] sequence) {
        if (Arrays.stream(sequence).allMatch(n -> n == 0)) return 0;
        int[] nextHistory = new int[sequence.length - 1];
        for (int i = 0; i < nextHistory.length; i++) {
            nextHistory[i] = sequence[i + 1] - sequence[i];
        }

        return sequence[0] - getPrevElement(nextHistory);
    }
}
