package com.github.oskar117.day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "input/day02/";
        System.out.println("PART1");
        String[] filesPart1 = new String[]{"data2.txt"};

        System.out.println("PART1");
        CubeSet expectedCubeSet = CubeSet.ofValues(12, 13, 14);
        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        long resultPart1 = Files.readAllLines(path)
                .stream()
                .map(Game::new)
                .filter(g -> g.canContain(expectedCubeSet))
                .map(g -> g.number)
                .collect(Collectors.summarizingInt(Integer::intValue))
                .getSum();
        System.out.println("Sum of game IDs is " + resultPart1);

        System.out.println("PART2");
        long resultPart2 = Files.readAllLines(path)
                .stream()
                .map(Game::new)
                .map(Game::minSet)
                .map(g -> g.blue * g.green * g.red)
                .collect(Collectors.summarizingInt(Integer::intValue)).getSum();
        System.out.println("Sum of the power of sets is " + resultPart2);
    }

    static class Game {
        int number;
        List<CubeSet> cubeSets;

        Game(String line) {
            String[] split = line.split(":");
            this.number = Integer.parseInt(split[0].substring(5));
            this.cubeSets = Arrays.stream(split[1].split(";")).map((setString) -> {
                CubeSet cubeSet = new CubeSet();
                Arrays.stream(setString.split(",")).forEach((setSubLine) -> {
                    String[] setSubLineSplit = setSubLine.trim().split(" ");
                    int number = Integer.parseInt(setSubLineSplit[0]);
                    switch (setSubLineSplit[1]) {
                        case "red":
                            cubeSet.red = number;
                            break;
                        case "green":
                            cubeSet.green = number;
                            break;
                        case "blue":
                            cubeSet.blue = number;
                            break;
                    }
                });
                return cubeSet;
            }).toList();
        }

        boolean canContain(CubeSet expectedSet) {
            int maxRed = cubeSets.stream().map(set -> set.red).max(Integer::compare).orElseThrow();
            int maxBlue = cubeSets.stream().map(set -> set.blue).max(Integer::compare).orElseThrow();
            int maxGreen = cubeSets.stream().map(set -> set.green).max(Integer::compare).orElseThrow();
            return maxRed <= expectedSet.red && maxGreen <= expectedSet.green && maxBlue <= expectedSet.blue;
        }

        CubeSet minSet() {
            int maxRed = cubeSets.stream().map(set -> set.red).max(Integer::compare).orElseThrow();
            int maxBlue = cubeSets.stream().map(set -> set.blue).max(Integer::compare).orElseThrow();
            int maxGreen = cubeSets.stream().map(set -> set.green).max(Integer::compare).orElseThrow();
            return CubeSet.ofValues(maxRed, maxBlue, maxGreen);
        }
    }

    static class CubeSet {
        public int red, green, blue;

        static CubeSet ofValues(int red, int green, int blue) {
            var x = new CubeSet();
            x.red = red;
            x.green = green;
            x.blue = blue;
            return x;
        }

    }
}