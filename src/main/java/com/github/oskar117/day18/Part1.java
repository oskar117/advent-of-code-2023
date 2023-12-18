package com.github.oskar117.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Part1 {
    private final static String FILE_PATH = "input/day18/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        Map<Tile, String> lagoon = new HashMap<>();
        Tile lastTile = new Tile(0, 0);
        lagoon.put(lastTile, null);
        for (String line : Files.readAllLines(path)) {
            Pattern pattern = Pattern.compile("(\\w) (\\d+) \\((.+)\\)");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String direction = matcher.group(1);
            int meters = Integer.parseInt(matcher.group(2));
            String color = matcher.group(3);
            lastTile = dig(lagoon, lastTile, direction, color, meters);
        }
        int minX = lagoon.keySet().stream().map(Tile::x).min(Integer::compareTo).orElseThrow();
        int minY = lagoon.keySet().stream().map(Tile::y).min(Integer::compareTo).orElseThrow();
        int maxX = lagoon.keySet().stream().map(Tile::x).max(Integer::compareTo).orElseThrow();
        int maxY = lagoon.keySet().stream().map(Tile::y).max(Integer::compareTo).orElseThrow();

        Stream.of(
                new Tile(1, 1),
                new Tile(-1, -1),
                new Tile(1, -1),
                new Tile(-1,1)
        ).flatMap(tile -> flood(lagoon.keySet(), tile, maxX, minX, maxY, minY).stream()).forEach(p -> lagoon.put(p, null));

        int result = lagoon.keySet().size();
        System.out.printf("Lagoon can hold " + result + " cubic meters of lava");
    }

    private static Tile dig(Map<Tile, String> lagoon, Tile lastTile, String direction, String color, int meters) {
        if (meters == 0) return lastTile;
        Tile currentTile = switch (direction) {
            case "R" -> new Tile(lastTile.x + 1, lastTile.y);
            case "L" -> new Tile(lastTile.x - 1, lastTile.y);
            case "U" -> new Tile(lastTile.x, lastTile.y - 1);
            case "D" -> new Tile(lastTile.x, lastTile.y + 1);
            default -> throw new RuntimeException("Invalid dir");
        };
        lagoon.put(currentTile, color);
        return dig(lagoon, currentTile, direction, color, meters - 1);
    }

    private static List<Tile> flood(Set<Tile> tiles, Tile currentTile, int maxX, int minX, int maxY, int minY) {
        List<Tile> visited = new ArrayList<>();
        Deque<Tile> queue = new ArrayDeque<>();
        queue.add(currentTile);

        while (!queue.isEmpty()) {
            Tile currentData = queue.removeFirst();
            if (!visited.contains(currentData) && !tiles.contains(currentData)) {
                visited.add(currentData);
                for (Tile neighbour : getNeighbours(currentData.x, currentData.y)) {
                    if (neighbour.x < minX || neighbour.y < minY || neighbour.y > maxY || neighbour.x > maxX) {
                        return List.of();
                    }
                    queue.addFirst(neighbour);
                }
            }
        }

        return visited;
    }

    private static List<Tile> getNeighbours(int x, int y) {
        return List.of(
                new Tile(x, y + 1),
                new Tile(x, y - 1),
                new Tile(x + 1, y),
                new Tile(x - 1, y)
        );
    }

    record Tile(int x, int y) {
    }
}
