package com.github.oskar117.day03;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

class Main {

    private final static int[][] neighbourFieldMap = new int[][]{
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 0},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
    };

    public static void main(String[] args) throws IOException {
        String filePath = "input/day03/";
        String[] filesPart1 = new String[]{"data2.txt"};

        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        System.out.println("PART1");
        part1(path);
        System.out.println("PART1");
        part2(path);
    }

    private static void part1(Path path) throws IOException {
        List<String> linesList = Files.readAllLines(path);
        int lineLength = linesList.get(0).length();
        int height = linesList.size();
        int result = 0;

        for (int y = 0; y < height; y++) {
            StringBuilder currentNumber = new StringBuilder();
            boolean isPart = false;
            for (int x = 0; x < lineLength; x++) {
                char currentChar = linesList.get(y).charAt(x);
                if (Character.isDigit(currentChar)) {
                    currentNumber.append(currentChar);
                    isPart |= hasAdjacentSymbol(linesList, x, y);
                    if (x != lineLength - 1) continue;
                }
                if (!currentNumber.isEmpty()) {
                    if (isPart) {
                        result += Integer.parseInt(currentNumber.toString());
                        isPart = false;
                    }
                    currentNumber = new StringBuilder();
                }
            }
        }
        System.out.println("Sum of all engine parts is: " + result);
    }

    private static void part2(Path path) throws IOException {
        List<String> linesList = Files.readAllLines(path);
        int lineLength = linesList.get(0).length();
        int height = linesList.size();
        Map<Point, List<Integer>> gearMap = new HashMap<>();

        for (int y = 0; y < height; y++) {
            StringBuilder currentNumber = new StringBuilder();
            Point gearPos = null;

            for (int x = 0; x < lineLength; x++) {
                char currentChar = linesList.get(y).charAt(x);
                if (Character.isDigit(currentChar)) {
                    currentNumber.append(currentChar);
                    if (gearPos == null) {
                        gearPos = getGearPos(linesList, x, y);
                    }
                    if (x != lineLength - 1) continue;
                }
                if (!currentNumber.isEmpty()) {
                    if (gearPos != null) {
                        int number = Integer.parseInt(currentNumber.toString());
                        gearMap.computeIfAbsent(gearPos, (k) -> new ArrayList<>());
                        gearMap.get(gearPos).add(number);
                        gearPos = null;
                    }
                    currentNumber = new StringBuilder();
                }
            }
        }
        int result = gearMap.values()
                .stream()
                .filter(l -> l.size() == 2)
                .flatMapToInt(l -> IntStream.of(l.stream().reduce(1, (a, b) -> a * b)))
                .sum();
        System.out.println("Sum of all engine parts is: " + result);
    }

    static boolean hasAdjacentSymbol(List<String> data, int x, int y) {
        for (int[] neighbour : neighbourFieldMap) {
            if (y + neighbour[1] > data.size() - 1) continue;
            if (x + neighbour[0] > data.get(0).length() - 1) continue;
            if (y + neighbour[1] < 0) continue;
            if (x + neighbour[0] < 0) continue;
            char c = data.get(y + neighbour[1]).charAt(x + neighbour[0]);
            if (!Character.isDigit(c) && c != '.') return true;
        }
        return false;
    }

    static Point getGearPos(List<String> data, int x, int y) {
        for (int[] neighbour : neighbourFieldMap) {
            if (y + neighbour[1] > data.size() - 1) continue;
            if (x + neighbour[0] > data.get(0).length() - 1) continue;
            if (y + neighbour[1] < 0) continue;
            if (x + neighbour[0] < 0) continue;
            char c = data.get(y + neighbour[1]).charAt(x + neighbour[0]);
            if (c == '*') return new Point(x+neighbour[0], y+neighbour[1]);
        }
        return null;
    }
}
