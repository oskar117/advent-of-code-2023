package com.github.oskar117.day10;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class Part1 {
    private final static String FILE_PATH = "input/day10/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] map = Files.readAllLines(path).stream().map(String::toCharArray).toArray(char[][]::new);
        Queue<Data> queue = new ArrayDeque<>();

        outer:
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (map[y][x] == 'S') {
                    queue.add(new Data(x, y, 0, x, y));
                    break outer;
                }
            }
        }

        while (!queue.isEmpty()) {
            Data currentData = queue.remove();
            List<Point> neighbours = getNeighbours(currentData.x, currentData.y)
                    .stream()
                    .filter(p -> !p.equals(new Point(currentData.prevX, currentData.prevY)))
                    .toList();
            for (Point neighbour : neighbours) {
                if (neighbour.x < 0 || neighbour.y < 0 || neighbour.y >= map.length || neighbour.x >= map[0].length)
                    continue;
                if (neighbour.x > currentData.x && (!List.of('-', 'J', '7', 'S').contains(map[neighbour.y][neighbour.x]) || List.of('|', 'J', '7').contains(map[currentData.y][currentData.x])))
                    continue;
                if (neighbour.x < currentData.x && (!List.of('-', 'F', 'L', 'S').contains(map[neighbour.y][neighbour.x]) || List.of('|', 'L', 'F').contains(map[currentData.y][currentData.x])))
                    continue;
                if (neighbour.y > currentData.y && (!List.of('|', 'J', 'L', 'S').contains(map[neighbour.y][neighbour.x]) || List.of('-', 'L', 'J').contains(map[currentData.y][currentData.x])))
                    continue;
                if (neighbour.y < currentData.y && (!List.of('|', 'F', '7', 'S').contains(map[neighbour.y][neighbour.x]) || List.of('-', '7', 'F').contains(map[currentData.y][currentData.x])))
                    continue;
                if (map[neighbour.y][neighbour.x] == 'S') {
                    System.out.println("Farthest point is " + ((currentData.distance + 1) / 2) + " steps away");
                    System.exit(0);
                }
                queue.add(new Data(neighbour.x, neighbour.y, currentData.distance + 1, currentData.x, currentData.y));
            }
        }
    }

    private static List<Point> getNeighbours(int x, int y) {
        return List.of(
                new Point(x, y + 1),
                new Point(x, y - 1),
                new Point(x + 1, y),
                new Point(x - 1, y)
        );
    }

    record Data(int x, int y, int distance, int prevX, int prevY) {
    }

}
