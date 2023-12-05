package com.github.oskar117.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class Part2 {

    public static void main(String[] args) throws IOException {
        String filePath = "input/day05/";
        String[] filesPart1 = new String[]{"data2.txt"};
        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        String[] input = Files.readString(path).split("\n\n");

        AlmanacMap almanacChain = null;

        for (int i = 1; i < input.length; i++) {
            String almanacData = input[i].replaceAll(".*map:\n", "");
            AlmanacMap almanacMap = new AlmanacMap(almanacData);
            if (almanacChain == null) {
                almanacChain = almanacMap;
            } else {
                almanacChain.addChain(almanacMap);
            }
        }

        long result = Arrays.stream(input[0].replace("seeds: ", "").split("(?<=\\G(\\d{1,11} ){2}+)"))
                .parallel()
                .flatMapToLong(s -> {
                    String[] split = s.trim().split(" ");
                    long start = Long.parseLong(split[0]);
                    long length = Long.parseLong(split[1]);
                    return LongStream.range(start, start + length);
                })
                .map(Objects.requireNonNull(almanacChain)::processChainCategory)
                .min().orElseThrow();

        System.out.println("Lowest location number that corresponds to initial seed ranges is " + result);
    }

    static class AlmanacMap {
        private final List<Category> categories = new ArrayList<>();
        private AlmanacMap next;

        public AlmanacMap(String categoryStrings) {
            Arrays.stream(categoryStrings.split("\n")).map(s -> {
                long[] nums = Arrays.stream(s.split(" ")).mapToLong(Long::parseLong).toArray();
                return new Category(nums[0], nums[1], nums[2]);
            }).forEach(this.categories::add);
        }

        long mapToCategory(long seed) {
            return categories.stream()
                    .filter((c) -> c.source <= seed && c.source + c.length > seed)
                    .findFirst()
                    .map(c -> seed - c.source + c.destination)
                    .orElse(seed);
        }

        long processChainCategory(long seed) {
            long value = mapToCategory(seed);
            if (next == null) return value;
            return next.processChainCategory(value);
        }

        public void addChain(AlmanacMap almanacMap) {
            if (next == null) {
                next = almanacMap;
            } else {
                next.addChain(almanacMap);
            }
        }

        record Category(long destination, long source, long length) {
        }
    }
}
