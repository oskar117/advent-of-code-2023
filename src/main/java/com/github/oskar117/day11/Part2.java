package com.github.oskar117.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Part2 {
    private final static String FILE_PATH = "input/day11/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] map = Files.readAllLines(path).stream().map(String::toCharArray).toArray(char[][]::new);
        List<Galaxy> galaxies = new ArrayList<>();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == '#')
                    galaxies.add(new Galaxy(x, y));
            }
        }

        int galaxyMultiplier = 999999;
        int resizeYIndex = 0;
        for (int y = 0; y < map.length; y++) {
            if (new String(map[y]).chars().allMatch(c -> c == '.')) {
                int finalY = y;
                int finalResizeYIndex = resizeYIndex;
                galaxies.stream().filter(g -> g.y > finalY+ finalResizeYIndex).forEach(g -> g.y+=galaxyMultiplier);
                resizeYIndex+=galaxyMultiplier;
            }
        }

        int resizeIndex = 0;
        for (int x = 0; x < map[0].length; x++) {
            StringBuilder tmp = new StringBuilder();
            for (char[] chars : map) {
                tmp.append(chars[x]);
            }
            if (tmp.chars().allMatch(c -> c == '.')) {
                int finalX = x;
                int finalResizeIndex = resizeIndex;
                galaxies.stream().filter(g -> g.x - finalResizeIndex > finalX).forEach(g -> g.x+=galaxyMultiplier);
                resizeIndex+=galaxyMultiplier;
            }
        }

        List<GalaxyPair> pairs = new ArrayList<>();
        for (int ii = 0; ii < galaxies.size(); ii++) {
            for (int i = 1+ii; i < galaxies.size(); i++) {
                pairs.add(new GalaxyPair(galaxies.get(ii), galaxies.get(i)));
            }
        }

        long result = pairs.stream()
                .map(g -> g.distance)
                .mapToLong(Long::longValue)
                .sum();
        System.out.println("Sum of lengths is " + result);
    }

    static class Galaxy{
        int x, y;
        Galaxy(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class GalaxyPair {
        Galaxy a, b;
        long distance;
        GalaxyPair(Galaxy a, Galaxy b) {
            this.a = a;
            this.b = b;
            this.distance = Math.abs(b.x - a.x) + Math.abs(b.y - a.y);
        }
    }
}
