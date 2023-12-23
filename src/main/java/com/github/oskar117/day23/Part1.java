package com.github.oskar117.day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

class Part1 {

    private final static String FILE_PATH = "input/day23/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] floor = Arrays.stream(Files.readString(path).split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        System.out.println("PART1: Longest possible path have " + getLongestPathLength(floor, true) + " steps");
        //not working, data too big
//        System.out.println("PART2: Longest possible path without slopes have " + getLongestPathLength(floor, false) + " steps");
    }

    private static int getLongestPathLength(char[][] floor, boolean withSlopes) {
        Deque<Data> queue = new ArrayDeque<>();
        queue.add(new Data(1, -1, 0, Direction.DOWN, new boolean[floor.length][floor[0].length]));

        int result = 0;
        while(!queue.isEmpty()) {
            Data current = queue.remove();
            for (Direction neighbour : Direction.values()) {
                int nextX = current.x + neighbour.nextX;
                int nextY = current.y + neighbour.nextY;
                if (nextX < 0 || nextY < 0 || nextY >= floor.length || nextX >= floor[0].length)
                    continue;
                if (current.personalVisited[nextY][nextX])
                    continue;
                if (floor[nextY][nextX] == '#')
                    continue;
                if (withSlopes && floor[nextY][nextX] == neighbour.opposite)
                    continue;
                if (nextY == floor.length - 1 && nextX == floor[0].length - 2) {
                    result = Math.max(result, current.length);
                    continue;
                }
                boolean[][] visitedCopy =Arrays.stream(current.personalVisited).map(boolean[]::clone).toArray(boolean[][]::new);
                visitedCopy[nextY][nextX] = true;
                queue.addFirst(new Data(nextX, nextY, current.length + 1, neighbour, visitedCopy));
            }
        }
        return result;
    }

    record Data(int x, int y, int length, Direction direction, boolean[][] personalVisited) {}

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
}
