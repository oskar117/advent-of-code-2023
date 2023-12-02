package com.github.oskar117.day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String filePath = "input/day01/";
        System.out.println("PART1");
        String[] filesPart1 = new String[]{"data1.txt", "data2.txt"};
        Arrays.stream(filesPart1).forEach(file -> part1(filePath, file));
        String[] filesPart2 = new String[]{"data3.txt", "data2.txt"};
        System.out.println("PART2");
        Arrays.stream(filesPart2).forEach(file -> part2(filePath, file));
    }

    public static void part1(String path, String fileName) {
        try {
            List<String> input = Files.readAllLines(Path.of(path, fileName).toAbsolutePath());
            var result = input.stream().map((word) -> {
                List<String> chars = Arrays.asList(word.split(""));
                Function<List<String>, String> findFirstInt = (data) -> data.stream().filter(s -> s.matches("\\d+")).findFirst().orElseThrow();
                String first = findFirstInt.apply(chars);
                Collections.reverse(chars);
                String last = findFirstInt.apply(chars);
                return Integer.parseInt(first + last);
            }).collect(Collectors.summarizingInt(Integer::intValue)).getSum();
            System.out.println("Sum of all of the calibration values for " + fileName + " is: " + result);
        } catch (Exception e) {
            System.err.println("Error occurred!");
        }
    }

    public static void part2(String path, String fileName) {
        Map<String, String> numberMap = new HashMap<>();
        numberMap.put("one", "1");
        numberMap.put("two", "2");
        numberMap.put("three", "3");
        numberMap.put("four", "4");
        numberMap.put("five", "5");
        numberMap.put("six", "6");
        numberMap.put("seven", "7");
        numberMap.put("eight", "8");
        numberMap.put("nine", "9");

        try {
            List<String> input = Files.readAllLines(Path.of(path, fileName).toAbsolutePath());
            var result = input.stream().map((word) -> {
                String[] chars = word.split("");
                StringBuilder res = new StringBuilder();
                //Mozna wywalić ten rak dzięki replaceAll -_- + character.isDigit
                for (int x = 0; x < chars.length; x++) {
                    String buffer = "";
                    for (int y = x; y < chars.length; y++) {
                        buffer += chars[y];
                        String finalBuffer = buffer;
                        List<String> possibleNumbers = numberMap.keySet().stream().filter(s -> s.startsWith(finalBuffer)).toList();
                        if (possibleNumbers.size() == 0) {
                            res.append(chars[x]);
                            break;
                        } else if (possibleNumbers.size() == 1 && possibleNumbers.contains(buffer)) {
                            res.append(numberMap.get(buffer));
                            break;
                        }
                    }
                }
                return res.toString();
            }).map((word) -> {
                List<String> chars = Arrays.asList(word.split(""));
                Function<List<String>, String> findFirstInt = (data) -> data.stream().filter(s -> s.matches("\\d+")).findFirst().orElseThrow();
                String first = findFirstInt.apply(chars);
                Collections.reverse(chars);
                String last = findFirstInt.apply(chars);
                return Integer.parseInt(first + last);
            }).collect(Collectors.summarizingInt(Integer::intValue)).getSum();
            System.out.println("Sum of all of the calibration values for " + fileName + " is: " + result);
        } catch (Exception e) {
            System.err.println("Error occurred!");
        }
    }
}