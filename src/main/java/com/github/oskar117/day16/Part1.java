package com.github.oskar117.day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;

class Part1 {
    private final static String FILE_PATH = "input/day16/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] floor = Arrays.stream(Files.readString(path).split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);
        boolean[][] energized = new boolean[floor.length][floor[0].length];
        Set<Beam> cache = new HashSet<>();

        Queue<Beam> queue = new ArrayDeque<>();
        queue.add(new Beam(-1, 0, Direction.RIGHT));

        while (!queue.isEmpty()) {
            Beam current = queue.remove();
            if (cache.contains(current))
                continue;
            cache.add(current);
            Direction direction = current.direction;
            int nextX = direction.nextX + current.x;
            int nextY = direction.nextY + current.y;
            if (nextX < 0 || nextY < 0 || nextY >= floor.length || nextX >= floor[0].length)
                continue;
            energized[nextY][nextX] = true;
            direction.mapToNext(floor[nextY][nextX])
                    .stream()
                    .map(newDir -> new Beam(nextX, nextY, newDir))
                    .forEach(queue::add);

        }
        long result = Arrays.stream(energized).flatMap(b -> IntStream.range(0, b.length).mapToObj(idx -> b[idx])).filter(b -> b).count();
        System.out.println(result + " tiles end up being energized");
    }

    enum Direction {
        UP(0, -1) {
            @Override
            List<Direction> mapToNext(char symbol) {
                return switch (symbol) {
                    case '/' -> List.of(RIGHT);
                    case '\\' -> List.of(LEFT);
                    case '-' -> List.of(RIGHT, LEFT);
                    default -> List.of(UP);
                };
            }
        },
        LEFT(-1, 0) {
            @Override
            List<Direction> mapToNext(char symbol) {
                return switch (symbol) {
                    case '/' -> List.of(DOWN);
                    case '\\' -> List.of(UP);
                    case '|' -> List.of(DOWN, UP);
                    default -> List.of(LEFT);
                };
            }
        },
        DOWN(0, 1) {
            @Override
            List<Direction> mapToNext(char symbol) {
                return switch (symbol) {
                    case '/' -> List.of(LEFT);
                    case '\\' -> List.of(RIGHT);
                    case '-' -> List.of(RIGHT, LEFT);
                    default -> List.of(DOWN);
                };
            }
        },
        RIGHT(1, 0) {
            @Override
            List<Direction> mapToNext(char symbol) {
                return switch (symbol) {
                    case '/' -> List.of(UP);
                    case '\\' -> List.of(DOWN);
                    case '|' -> List.of(DOWN, UP);
                    default -> List.of(RIGHT);
                };
            }
        };

        final int nextX;
        final int nextY;

        Direction(int nextX, int nextY) {
            this.nextX = nextX;
            this.nextY = nextY;
        }

        abstract List<Direction> mapToNext(char symbol);
    }

    record Beam(int x, int y, Direction direction) {
    }

}
