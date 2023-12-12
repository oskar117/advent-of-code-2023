package com.github.oskar117.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Part2 {
    private final static String FILE_PATH = "input/day12/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        Map<Pair, Long> cache = new HashMap<>();
        long result = Files.readAllLines(path).stream().mapToLong(line -> {
            String[] split = line.split(" ");
            String[] springMap = split[0].concat("?").repeat(5).replaceAll("\\?$", "").split("");
            Long[] groupNumbers = Arrays.stream(split[1].concat(",")
                            .repeat(5)
                            .replaceAll(",$", "")
                            .split(","))
                    .map(Long::parseLong)
                    .toArray(Long[]::new);

            return countCombinations(springMap, groupNumbers, cache);
        }).sum();
        System.out.println("Sum of those counts is " + result);
    }

    private static Long countCombinations(String[] springMap, Long[] numbers, Map<Pair, Long> cache) {
        if (springMap.length == 0) {
            return numbers.length == 0 ? 1L : 0L;
        }
        if (numbers.length == 0) return List.of(springMap).contains("#") ? 0L : 1L;

        Pair key = new Pair(springMap, numbers);
        if (cache.containsKey(key))
            return cache.get(key);

        long result = 0;

        if (List.of(".", "?").contains(springMap[0])) {
            result += countCombinations(Arrays.copyOfRange(springMap, 1, springMap.length), numbers, cache);
        }

        if (List.of("#", "?").contains(springMap[0])) {
            if (numbers[0] <= springMap.length
                    && !Arrays.stream(Arrays.copyOfRange(springMap, 0, Math.toIntExact(numbers[0]))).toList().contains(".")
                    && (numbers[0] == springMap.length || !springMap[Math.toIntExact(numbers[0])].equals("#"))) {
                String[] newArr = numbers[0] + 1 > springMap.length ? new String[0] : Arrays.copyOfRange(springMap, (int) (numbers[0] + 1), springMap.length);
                result += countCombinations(newArr, Arrays.copyOfRange(numbers, 1, numbers.length), cache);
            }
        }
        cache.put(key, result);

        return result;
    }

    record Pair(String[] springs, Long[] numbers) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Arrays.equals(springs, pair.springs) && Arrays.equals(numbers, pair.numbers);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(springs);
            result = 31 * result + Arrays.hashCode(numbers);
            return result;
        }
    }

}
