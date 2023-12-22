package com.github.oskar117.day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Part2 {
    private final static String FILE_PATH = "input/day22/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        List<Brick> list = Files.readAllLines(path)
                .stream()
                .map(Brick::new)
                .sorted(Comparator.comparingInt(b -> b.a.z))
                .collect(Collectors.toCollection(ArrayList::new));

        for (int x = 0; x < list.size(); x++) {
            Brick brick = list.get(x);
            brick.i = x;

            int maxZ = 1;
            for (int i = 0; i < x; i++) {
                Brick belowBrick = list.get(i);
                if (belowBrick.intersects(brick)) {
                    maxZ = Math.max(maxZ, belowBrick.b.z + 1);
                }
            }
            brick.drop(maxZ);
        }
        list.sort(Comparator.comparingInt(b -> b.a.z));
        Map<Brick, Set<Brick>> supports = new HashMap<>();
        Map<Brick, Set<Brick>> isSupportedBy = new HashMap<>();
        for (int x = 0; x < list.size(); x++) {
            Brick brick = list.get(x);
            isSupportedBy.putIfAbsent(brick, new HashSet<>());
            supports.putIfAbsent(brick, new HashSet<>());
            for (int i = 0; i < x; i++) {
                Brick belowBrick = list.get(i);
                if (belowBrick.intersects(brick) && brick.a.z == belowBrick.b.z + 1) {
                    supports.get(belowBrick).add(brick);
                    isSupportedBy.get(brick).add(belowBrick);
                }
            }
        }

        int result = 0;
        for (Brick brick : list) {
            Queue<Brick> queue = supports.get(brick).stream().filter(b -> isSupportedBy.get(b).size() == 1).collect(Collectors.toCollection(ArrayDeque::new));
            Set<Brick> falling = new HashSet<>(queue);
            falling.add(brick);
            while (!queue.isEmpty()) {
                Brick current = queue.remove();
                List<Brick> list1 = supports.get(current).stream().filter(f -> !falling.contains(f)).toList();
                for (Brick next : list1) {
                    if (falling.containsAll(isSupportedBy.get(next))) {
                        queue.add(next);
                        falling.add(next);
                    }
                }
            }
            result += falling.size() - 1;

        }
        System.out.println(result + " would fall if only one can be disintegrated");
    }


    static class Brick {

        Point a;
        Point b;
        int i;

        Brick(String line) {
            String[] split = line.split("~");
            this.a = Point.of(split[0]);
            this.b = Point.of(split[1]);
        }

        boolean intersects(Brick other) {
            return Math.max(a.x, other.a.x) <= Math.min(b.x, other.b.x) && Math.max(a.y, other.a.y) <= Math.min(b.y, other.b.y);
        }

        void drop(int to) {
            this.b = b.drop(b.z - (a.z - to));
            this.a = a.drop(to);
        }
    }

    record Point(int x, int y, int z) {
        static Point of(String line) {
            String[] split = line.split(",");
            int nx = Integer.parseInt(split[0]);
            int ny = Integer.parseInt(split[1]);
            int nz = Integer.parseInt(split[2]);
            return new Point(nx, ny, nz);
        }

        Point drop(int newZ) {
            return new Point(x, y, newZ);
        }
    }
}
