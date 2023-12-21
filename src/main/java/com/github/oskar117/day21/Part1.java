package com.github.oskar117.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class Part1 {

    private final static String FILE_PATH = "input/day21/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] map = Arrays.stream(Files.readString(path).split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);
        Queue<Position> queue = new PriorityQueue<>(Comparator.comparingInt(Position::step));
        HashSet<Position> visited = new HashSet<>();
        Position startingPosition = findStart(map);
        queue.add(startingPosition);
        visited.add(startingPosition);

        final int stepsRequired = 64;
        while (!queue.stream().allMatch(p -> p.step == stepsRequired)) {
            Position currentPos = queue.remove();
            for (Position neighbour : getNeighbours(currentPos)) {
                if (neighbour.x() < 0 || neighbour.y() < 0 || neighbour.y() >= map.length || neighbour.x() >= map[0].length)
                    continue;
                if (map[neighbour.y()][neighbour.x()] == '#')
                    continue;
                if (visited.contains(neighbour))
                    continue;
                if (neighbour.step > stepsRequired + 1)
                    continue;
                queue.add(neighbour);
                visited.add(neighbour);
            }
        }
        System.out.println("Elf can reach " + queue.size() + " plots");
    }

    private static Position findStart(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 'S')
                    return new Position(x, y, 0);
            }
        }
        throw new RuntimeException("S not found");
    }

    record Position(int x, int y, int step) {}

    private static List<Position> getNeighbours(Position position) {
        int x = position.x();
        int y = position.y();
        return List.of(
                new Position(x, y + 1, position.step + 1),
                new Position(x, y - 1, position.step + 1),
                new Position(x + 1, y, position.step + 1),
                new Position(x - 1, y, position.step + 1)
        );
    }
}
