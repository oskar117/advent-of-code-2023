package com.github.oskar117.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;

class Part1 {

    public static void main(String[] args) throws IOException {
        String filePath = "input/day05/";
        String[] filesPart1 = new String[]{"data2.txt"};
        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        String[] input = Files.readString(path).split("\n\n");

        long[] seeds = Arrays.stream(input[0].replace("seeds: ", "").split(" ")).mapToLong(Long::parseLong).toArray();
        for (int i = 1; i < input.length; i++) {
            String almanacData = input[i].replaceAll(".*map:\n", "");
            AlmanacMap almanacMap = new AlmanacMap(almanacData);
            seeds = almanacMap.processMap(seeds);
        }
        long result = Arrays.stream(seeds).min().orElseThrow();
        System.out.println("Lowest location number that corresponds to initial seeds is " + result);
    }

    static class AlmanacMap {
        private final List<Category> categories = new ArrayList<>();

        public AlmanacMap(String categoryStrings) {
            Arrays.stream(categoryStrings.split("\n")).map(s -> {
                long[] nums = Arrays.stream(s.split(" ")).mapToLong(Long::parseLong).toArray();
                return new Category(nums[0], nums[1], nums[2]);
            }).forEach(this.categories::add);
        }

        long[] processMap(long[] input) {
            return Arrays.stream(input)
                    .sorted()
                    .map(this::mapToCategory)
                    .toArray();
        }

        long mapToCategory(long seed) {
            return categories.stream()
                    .filter((c) -> c.source <= seed && c.source + c.length > seed)
                    .findFirst()
                    .map(c -> seed - c.source + c.destination)
                    .orElse(seed);
        }

        record Category(long destination, long source, long length) {
        }
    }
}
