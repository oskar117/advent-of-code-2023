package com.github.oskar117.day21;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

class Part2 {

    private final static String FILE_PATH = "input/day21/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] map = Arrays.stream(Files.readString(path).split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);
        Position startingPosition = findStart(map);

        assert map.length == map[0].length;
        int size = map.length;
        int steps = 26501365;
        assert startingPosition.x == startingPosition.y && startingPosition.y == size / 2;
        assert steps % size == size / 2;
        int gridWidth = steps / size - 1;
        long odd = pow(gridWidth / 2 * 2 + 1);
        long even = pow((gridWidth + 1) / 2 * 2);

        long oddPoints = walk(map, startingPosition, size * 2 + 1);
        long evenPoints = walk(map, startingPosition, size * 2);

        long cornerT = walk(map, new Position(size - 1, startingPosition.y, 0), size - 1);
        long cornerR = walk(map, new Position(startingPosition.x, 0, 0), size - 1);
        long cornerB = walk(map, new Position(0, startingPosition.y, 0), size - 1);
        long cornerL = walk(map, new Position(startingPosition.x, size - 1, 0), size - 1);
        System.out.println(cornerT + " " + cornerR + " " + cornerB + " " + cornerL);

        long smallTr = walk(map, new Position(size - 1, 0, 0), size / 2 - 1);
        long smallTl = walk(map, new Position(size - 1, size - 1, 0), size / 2 - 1);
        long smallBr = walk(map, new Position(0, 0, 0), size / 2 - 1);
        long smallBl = walk(map, new Position(0, size - 1, 0), size / 2 - 1);
        System.out.println(smallBl + " " + smallBr + " " + smallTr + " " + smallTl);

        long largeTr = walk(map, new Position(size - 1, 0, 0), size * 3 / 2 - 1);
        long largeTl = walk(map, new Position(size - 1, size - 1, 0), size * 3 / 2 - 1);
        long largeBr = walk(map, new Position(0, 0, 0), size * 3 / 2 - 1);
        long largeBl = walk(map, new Position(0, size - 1, 0), size * 3 / 2 - 1);
        System.out.println(largeBl + " " + largeBr + " " + largeTr + " " + largeTl);

        System.out.println(odd + " " + oddPoints + " " +  even  +" "+ evenPoints);
        long result = odd * oddPoints + even * evenPoints +
                cornerB + cornerR + cornerL + cornerT +
                (gridWidth + 1) * (smallTr + smallTl + smallBl + smallBr) +
                gridWidth * (largeBl + largeBr + largeTl + largeTr);
        System.out.println("Elf can walk " + result + " plots");
    }

    private static long pow(long x) {
        return x * x;
    }

    private static int walk(char[][] map, Position startingPosition, final int stepsRequired) {
        Queue<Position> queue = new PriorityQueue<>(Comparator.comparingInt(Position::step));
        HashSet<Position> visited = new HashSet<>();
        queue.add(startingPosition);
        visited.add(startingPosition);

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
        return queue.size();
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
