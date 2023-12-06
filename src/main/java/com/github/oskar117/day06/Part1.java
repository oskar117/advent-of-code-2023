package com.github.oskar117.day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class Part1 {

    public static void main(String[] args) throws IOException {
        String filePath = "input/day06/";
        String[] filesPart1 = new String[]{"data2.txt"};
        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        int[][] lines = Files.readAllLines(path)
                .stream()
                .map(s -> Arrays.stream(s.replaceAll("\\w+:\\s*", "").split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray())
                .toArray(int[][]::new);

        int result = 1;
        for (int x = 0; x < lines[0].length; x++) {
            int time = lines[0][x];
            int distance = lines[1][x];
            int beatRecordCount = 0;
            for (int holdTime = 0; holdTime <= time; holdTime++) {
                int travelled = holdTime * (time - holdTime);
                if (travelled > distance) {
                    beatRecordCount ++;
                }
            }
            result *= beatRecordCount;
        }
        System.out.println("Product of number of ways you can beat the record is " + result);
    }
}
