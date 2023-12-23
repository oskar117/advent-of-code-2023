package com.github.oskar117.day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Part2 {

    private final static String FILE_PATH = "input/day23/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] floor = Arrays.stream(Files.readString(path).split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        List<Point> junctions = new ArrayList<>();
        junctions.add(new Point(1, 0));
        junctions.add(new Point(floor[0].length - 2, floor.length - 1));

        for (int y = 0; y < floor.length; y++) {
            for (int x = 0; x < floor[0].length; x++) {
                if (floor[y][x] == '#')
                    continue;
                int neighbours = 0;
                for (Direction neighbour : Direction.values()) {
                    int nextX = x + neighbour.nextX;
                    int nextY = y + neighbour.nextY;
                    if (nextX < 0 || nextY < 0 || nextY >= floor.length || nextX >= floor[0].length)
                        continue;
                    if (floor[nextY][nextX] == '#')
                        continue;
                    neighbours++;
                }
                if (neighbours >= 3)
                    junctions.add(new Point(x, y));
            }
        }

        Map<Point, HashMap<Point, Integer>> graph = junctions.stream().collect(Collectors.toMap(Function.identity(), (_) -> new HashMap<>()));

        record GraphData(int x, int y, int length) {}
        for (Point currentPoint : junctions) {
            Deque<GraphData> deque = new ArrayDeque<>();
            Set<Point> visited = new HashSet<>();
            visited.add(currentPoint);

            deque.add(new GraphData(currentPoint.x, currentPoint.y, 0));
            while (!deque.isEmpty()) {
                GraphData currentData = deque.removeFirst();
                if (currentData.length > 0 && junctions.contains(new Point(currentData.x, currentData.y))) {
                    graph.get(currentPoint).put(new Point(currentData.x, currentData.y), currentData.length);
                    continue;
                }
                for (Direction neighbour : Direction.values()) {
                    int nextX = currentData.x + neighbour.nextX;
                    int nextY = currentData.y + neighbour.nextY;
                    if (nextX < 0 || nextY < 0 || nextY >= floor.length || nextX >= floor[0].length)
                        continue;
                    if (floor[nextY][nextX] == '#')
                        continue;
                    if (visited.contains(new Point(nextX, nextY)))
                        continue;
                    deque.addFirst(new GraphData(nextX, nextY, currentData.length + 1));
                    visited.add(new Point(nextX, nextY));
                }
            }
        }

        Deque<Data> queue = new ArrayDeque<>();
        queue.add(new Data(new Point(1, 0), 0, new ArrayList<>()));

        int result = 0;
        while(!queue.isEmpty()) {
            Data current = queue.remove();
            for (Map.Entry<Point, Integer> neighbour: graph.get(current.point).entrySet()) {
                if (current.personalVisited.contains(neighbour.getKey()))
                    continue;
                if (neighbour.getKey().equals(new Point(floor[0].length - 2, floor.length - 1))) {
                    result = Math.max(result, current.length + neighbour.getValue());
                    continue;
                }
                ArrayList<Point> visitedCopy = new ArrayList<>(current.personalVisited);
                visitedCopy.add(neighbour.getKey());
                queue.addFirst(new Data(neighbour.getKey(), current.length + neighbour.getValue(), visitedCopy));
            }
        }
        System.out.println("PART2: Longest possible path without slopes have " + result + " steps");
    }

    record Data(Point point, int length, List<Point> personalVisited) {}

    enum Direction {
        UP(0, -1, 'v'),
        DOWN(0, 1, '^'),
        LEFT(-1, 0, '>'),
        RIGHT(1, 0, '<');

        final int nextX;
        final int nextY;
        final char opposite;

        Direction(int nextX, int nextY, char opposite) {
            this.nextX = nextX;
            this.nextY = nextY;
            this.opposite = opposite;
        }
    }

    record Point(int x, int y) {}
}
