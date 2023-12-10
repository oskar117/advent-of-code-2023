package com.github.oskar117.day10;

import java.awt.Point;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class Part2 {
    private final static String FILE_PATH = "input/day10/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] map = Files.readAllLines(path).stream().map(String::toCharArray).toArray(char[][]::new);
        Queue<Data> queue = new ArrayDeque<>();
        Point sLocation = null;

        outer:
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 'S') {
                    queue.add(new Data(x, y, 0, x, y, new ArrayList<>(List.of(new Point(x, y)))));
                    sLocation = new Point(x, y);
                    break outer;
                }
            }
        }
        List<Point> finalPipePoints = null;

        pipeFinder:
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
                    finalPipePoints = new ArrayList<>(currentData.points);
                    break pipeFinder;
                }
                currentData.points.add(new Point(neighbour.x, neighbour.y));
                queue.add(new Data(neighbour.x, neighbour.y, currentData.distance + 1, currentData.x, currentData.y, new ArrayList<>(currentData.points)));
            }
        }

        assert finalPipePoints != null;
        Point first = finalPipePoints.get(1);
        Point last = finalPipePoints.getLast();
        if (first.x > last.x && first.y > last.y) map[sLocation.y][sLocation.x] = '7';
        else if (first.x < last.x && first.y > last.y) map[sLocation.y][sLocation.x] = 'F';
        else if (first.x > last.x && first.y < last.y) map[sLocation.y][sLocation.x] = 'J';
        else if (first.x < last.x && first.y < last.y) map[sLocation.y][sLocation.x] = 'L';
        else if (first.y == last.y) map[sLocation.y][sLocation.x] = '|';
        else map[sLocation.y][sLocation.x] = '-';

        char[][] biggerMap = new char[map.length * 2][map[0].length * 2];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                Point currentPoint = new Point(x, y);
                if (finalPipePoints.stream().noneMatch(currentPoint::equals)) {
                    map[y][x] = '.';
                }
                if (map[y][x] == '-') {
                    biggerMap[(y * 2)][(x * 2)] = '-';
                    biggerMap[(y * 2)][(x * 2) + 1] = '-';
                    biggerMap[(y * 2) + 1][(x * 2)] = '.';
                    biggerMap[(y * 2) + 1][(x * 2) + 1] = '.';
                } else if (map[y][x] == '|') {
                    biggerMap[(y * 2)][(x * 2)] = '|';
                    biggerMap[(y * 2)][(x * 2) + 1] = '.';
                    biggerMap[(y * 2) + 1][(x * 2)] = '|';
                    biggerMap[(y * 2) + 1][(x * 2) + 1] = '.';
                } else if (map[y][x] == 'F') {
                    biggerMap[(y * 2)][(x * 2)] = 'F';
                    biggerMap[(y * 2)][(x * 2) + 1] = '-';
                    biggerMap[(y * 2) + 1][(x * 2)] = '|';
                    biggerMap[(y * 2) + 1][(x * 2) + 1] = '.';
                } else if (map[y][x] == '7') {
                    biggerMap[(y * 2)][(x * 2)] = '7';
                    biggerMap[(y * 2)][(x * 2) + 1] = '.';
                    biggerMap[(y * 2) + 1][(x * 2)] = '|';
                    biggerMap[(y * 2) + 1][(x * 2) + 1] = '.';
                } else if (map[y][x] == 'J') {
                    biggerMap[(y * 2)][(x * 2)] = 'J';
                    biggerMap[(y * 2)][(x * 2) + 1] = '.';
                    biggerMap[(y * 2) + 1][(x * 2)] = '.';
                    biggerMap[(y * 2) + 1][(x * 2) + 1] = '.';
                } else if (map[y][x] == 'L') {
                    biggerMap[(y * 2)][(x * 2)] = 'L';
                    biggerMap[(y * 2)][(x * 2) + 1] = '-';
                    biggerMap[(y * 2) + 1][(x * 2)] = '.';
                    biggerMap[(y * 2) + 1][(x * 2) + 1] = '.';
                } else {
                    biggerMap[(y * 2)][(x * 2)] = '.';
                    biggerMap[(y * 2)][(x * 2) + 1] = '.';
                    biggerMap[(y * 2) + 1][(x * 2)] = '.';
                    biggerMap[(y * 2) + 1][(x * 2) + 1] = '.';
                }
            }
        }

        for (int y = 0; y < biggerMap.length; y++) {
            if (biggerMap[y][0] == '.')
                flood(biggerMap, 0, y);
            if (biggerMap[y][biggerMap[0].length - 1] == '.')
                flood(biggerMap, biggerMap[0].length - 1, y);
        }
        for (int x = 0; x < biggerMap[0].length; x++) {
            if (biggerMap[0][x] == '.')
                flood(biggerMap, x, 0);
            if (biggerMap[biggerMap.length - 1][x] == '.')
                flood(biggerMap, x, biggerMap.length - 1);
        }

        int result = 0;
        for (int y = 0; y < biggerMap.length; y++) {
            for (int x = 0; x < biggerMap[0].length; x++) {
                if (biggerMap[y][x] == '.' && y > 0 && x > 0 && !List.of('-', 'F', '7', 'L', 'J').contains(biggerMap[y - 1][x]) && !List.of('|', 'L', 'J', '7', 'F').contains(biggerMap[y][x - 1]) && !List.of('|', 'L', 'J', '7', 'F').contains(biggerMap[y - 1][x - 1])) {
                    result++;
                }
            }
        }
        System.out.println(result / 4 + " tiles are enclosed by the loop");
    }

    private static void flood(char[][] map, int x, int y) {
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(new Point(x, y));
        boolean[][] visited = new boolean[map.length][map[0].length];
        if (map[y][x] == '.') map[y][x] = '0';

        while (!queue.isEmpty()) {
            Point currentData = queue.remove();
            List<Point> neighbours = get8Neighbours(currentData.x, currentData.y)
                    .stream()
                    .toList();
            for (Point neighbour : neighbours) {
                if (neighbour.x < 0 || neighbour.y < 0 || neighbour.y >= map.length || neighbour.x >= map[0].length)
                    continue;
                if (visited[neighbour.y][neighbour.x]) continue;
                if (map[neighbour.y][neighbour.x] != '.') continue;
                if (map[neighbour.y][neighbour.x] == '.') map[neighbour.y][neighbour.x] = '0';
                visited[neighbour.y][neighbour.x] = true;
                queue.add(neighbour);
            }
        }
    }

    private static List<Point> get8Neighbours(int x, int y) {
        return List.of(
                new Point(x, y + 1),
                new Point(x + 1, y + 1),
                new Point(x - 1, y + 1),
                new Point(x, y - 1),
                new Point(x - 1, y - 1),
                new Point(x + 1, y - 1),
                new Point(x + 1, y),
                new Point(x - 1, y)
        );
    }

    private static List<Point> getNeighbours(int x, int y) {
        return List.of(
                new Point(x, y + 1),
                new Point(x, y - 1),
                new Point(x + 1, y),
                new Point(x - 1, y)
        );
    }

    record Data(int x, int y, int distance, int prevX, int prevY, List<Point> points) {
    }

}
