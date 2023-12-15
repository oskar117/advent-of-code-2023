package com.github.oskar117.day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class Part1 {
    private final static String FILE_PATH = "input/day15/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        int result = Arrays.stream(Files.readString(path).split(","))
                .mapToInt(seq -> seq.chars().reduce(0, (a, b) -> {
                    a += b;
                    a *= 17;
                    return a % 256;
                }))
                .sum();

        System.out.println("Sum of the results is " + result);
    }
}
