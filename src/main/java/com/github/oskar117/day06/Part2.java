package com.github.oskar117.day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class Part2 {

    public static void main(String[] args) throws IOException {
        String filePath = "input/day06/";
        String[] filesPart1 = new String[]{"data2.txt"};
        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        long[] lines = Files.readAllLines(path)
                .stream()
                .map(s -> s.replaceAll("\\w+:\\s+", "").replaceAll("\\s+", ""))
                .mapToLong(Long::parseLong)
                .toArray();

        long time = lines[0];
        long distance = lines[1];
        int beatRecordCount = 0;
        for (long holdTime = 0; holdTime <= time; holdTime++) {
            long travelled = holdTime * (time - holdTime);
            if (travelled > distance) {
                beatRecordCount++;
            }
        }
        System.out.println("In a long race you can beat the record " + beatRecordCount + " times");
    }
}
