package com.github.oskar117.day04;

import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Part1 {

    public static void main(String[] args) throws IOException {
        String filePath = "input/day04/";
        String[] filesPart1 = new String[]{"data2.txt"};
        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        List<String> linesList = Files.readAllLines(path);

        long result = 0;
        for (String cardLine : linesList) {
            List<List<Integer>> numbers = Arrays.stream(cardLine
                            .substring(cardLine.indexOf(":") + 1)
                            .split("\\|"))
                    .map(n -> Arrays.stream(n
                                    .trim()
                                    .split("\\s+"))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .toList())
                    .toList();

            List<Integer> winningNumbers = numbers.get(0);
            List<Integer> possessedNumbers = numbers.get(1);
            long matches = possessedNumbers.stream().filter(winningNumbers::contains).count();
            result += matches > 0 ? Double.valueOf(Math.pow(2, matches - 1)).longValue() : 0L;
        }
        System.out.println("Scratchcards are worth " + result + " points.");
    }
}
