package com.github.oskar117.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Part2 {
    private final static String FILE_PATH = "input/day18/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        List<Tile> lagoon = new ArrayList<>();
        Tile lastTile = new Tile(0, 0);
        int numberOfVerticies = 0;
        for (String line : Files.readAllLines(path)) {
            Pattern pattern = Pattern.compile("\\w \\d+ \\(#(\\S+)\\)");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String input = matcher.group(1);
            String direction = input.substring(input.length()-1);
            int meters = HexFormat.fromHexDigits(input.substring(0, input.length()-1));
            numberOfVerticies += meters;
            lastTile = switch (direction) {
                case "1" -> new Tile(lastTile.x - meters, lastTile.y);
                case "3" -> new Tile(lastTile.x + meters, lastTile.y);
                case "2" -> new Tile(lastTile.x, lastTile.y - meters);
                case "0" -> new Tile(lastTile.x, lastTile.y + meters);
                default -> throw new RuntimeException("Invalid dir");
            };
            lagoon.add(lastTile);
        }

        // shoelace formula - area of polygon with just vertices
        long polygonArea = shoelace(lagoon);
        // pick's theory - convert area to number of vertices inside
        long internalPoints = (polygonArea + 1) - (numberOfVerticies / 2);
        long result = internalPoints + numberOfVerticies;
        System.out.printf("Lagoon can hold " + Double.valueOf(result).longValue() + " cubic meters of lava");
    }

    public static long shoelace(List<Tile> vertices) {
        long area = 0;

        for (int i = 0; i < vertices.size(); i++) {
            int j = (i + 1) % vertices.size();
            area += (long) vertices.get(i).x * vertices.get(j).y - (long) vertices.get(j).x * vertices.get(i).y;
        }

        return area / 2;
    }

    record Tile(int x, int y) {
    }
}
