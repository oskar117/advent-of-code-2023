package com.github.oskar117.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Part1 {
    private final static String FILE_PATH = "input/day12/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        int result = Files.readAllLines(path).stream().mapToInt(line -> {
            String[] split = line.split(" ");
            String[] springMap = split[0].split("");
            Integer[] groupNumbers = Arrays.stream(split[1].split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            List<Integer> indicies = new LinkedList<>();
            for (int i = 0; i < springMap.length; i++) {
                if (springMap[i].equals("?")) indicies.add(i);
            }
            int combinations = 0;
            for (int i = 0; i < Math.pow(2, indicies.size()); i++) {
                String[] placeholders = Integer.toBinaryString(i | (1 << indicies.size())).substring(1).replaceAll("0", ".")
                        .replaceAll("1", "#")
                        .split("");
                String[] testCase = Arrays.copyOf(springMap, springMap.length);
                int offset = 0;
                for (int testIndex = 0; testIndex < testCase.length; testIndex++) {
                    if (indicies.contains(testIndex)) {
                        testCase[testIndex] = placeholders[testIndex - offset];
                    } else {
                        offset++;
                    }
                }

                Integer[] groups = Arrays.stream(String.join("", testCase)
                                .split("\\.+"))
                        .map(String::length)
                        .filter(n -> n != 0)
                        .toArray(Integer[]::new);
                if (Arrays.equals(groups, groupNumbers)) combinations++;
            }
            return combinations;
        }).sum();
        System.out.println("Sum of those counts is " + result);
    }
}
