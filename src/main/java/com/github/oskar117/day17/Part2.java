package com.github.oskar117.day17;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

class Part2 {
    private final static String FILE_PATH = "input/day17/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        int[][] map = Arrays.stream(Files.readString(path).split("\n"))
                .map(s -> s.chars().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);

        int result = minHeatLoss(map);
        System.out.println("The least heat loss that can occur is " + result);
    }

    private static int minHeatLoss(int[][] map) {
        Map<CacheData, Integer> cache = new HashMap<>();
        Queue<Data> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.heatLoss));
        queue.add(new Data(0, 0, Direction.RIGHT, 0, 0, List.of()));

        while (!queue.isEmpty()) {
            Data current = queue.remove();
            for (Direction neighbour : Direction.values()) {
                int x = neighbour.nextX + current.x;
                int y = neighbour.nextY + current.y;
                int newStreak = current.direction == neighbour ? current.streak + 1 : 1;
                ArrayList<Point> points = new ArrayList<>(current.path);
                Point neighbourPoint = new Point(x, y);
                points.add(neighbourPoint);

                if (x < 0 || y < 0 || x >= map[0].length || y >= map.length)
                    continue;

                if (newStreak > 10)
                    continue;
                if (current.streak + 1 <= 4 && neighbour != current.direction) {
                    continue;
                }
                if (current.path.contains(neighbourPoint))
                    continue;

                int heatLoss = current.heatLoss + map[y][x];
                CacheData cacheData = new CacheData(x, y, neighbour, newStreak);

                if (x == map[0].length - 1 && y == map.length - 1 && newStreak >= 4) {
                    return heatLoss;
                }

                if (cache.containsKey(cacheData)) {
                    int minHeat = cache.get(cacheData);
                    if (minHeat <= heatLoss)
                        continue;
                }
                cache.put(cacheData, heatLoss);

                Data newData = new Data(x, y, neighbour, newStreak, heatLoss, points);
                queue.add(newData);
            }
        }
        return -1;
    }

    enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int nextX;
        final int nextY;

        Direction(int nextX, int nextY) {
            this.nextX = nextX;
            this.nextY = nextY;
        }
    }

    record Data(int x, int y, Direction direction, int streak, int heatLoss, List<Point> path) { }

    record CacheData(int x, int y, Direction direction, int streak) {}
}
