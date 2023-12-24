package com.github.oskar117.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

class Part2 {

    private final static String FILE_PATH = "input/day24";
    private final static String FILE_NAME = "data2.txt";
    private final static Long MIN = 200000000000000L;
    private final static Long MAX = 400000000000000L;

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        List<Hailstone> hailstones = Files.readAllLines(path)
                .stream()
                .map(line -> line.replaceAll("[, @]", " "))
                .map(Hailstone::fromLine)
                .sorted(Comparator.comparingDouble(h -> h.position.x))
                .toList();

        TreeSet<Integer> potentialX = new TreeSet<>();
        TreeSet<Integer> potentialY = new TreeSet<>();
        TreeSet<Integer> potentialZ = new TreeSet<>();
        for (int a = 0; a < hailstones.size() - 1; a++) {
            for (int b = a + 1; b < hailstones.size(); b++) {
                Hailstone hailstone = hailstones.get(a);
                Hailstone otherHailstone = hailstones.get(b);

                if (hailstone.velocity.x == otherHailstone.velocity.x && Math.abs(hailstone.velocity.x) > 100) {
                    Set<Integer> newXSet = new HashSet<>();
                    double diff = otherHailstone.position.x - hailstone.position.x;
                    for (int i = -2000; i < 2000; i++) {
                        if (i == hailstone.velocity.x)
                            continue;
                        if (diff % (i - hailstone.velocity.x) == 0)
                            newXSet.add(i);
                    }
                    if (potentialX.isEmpty())
                        potentialX.addAll(newXSet);
                    else
                        potentialX.retainAll(newXSet);
                }

                if (hailstone.velocity.y == otherHailstone.velocity.y && Math.abs(hailstone.velocity.y) > 100) {
                    Set<Integer> newYSet = new HashSet<>();
                    double diff = otherHailstone.position.y - hailstone.position.y;
                    for (int i = -1000; i < 1000; i++) {
                        if (i == hailstone.velocity.y)
                            continue;
                        if (diff % (i - hailstone.velocity.y) == 0)
                            newYSet.add(i);
                    }
                    if (potentialY.isEmpty())
                        potentialY.addAll(newYSet);
                    else
                        potentialY.retainAll(newYSet);
                }

                if (hailstone.velocity.z == otherHailstone.velocity.z && Math.abs(hailstone.velocity.z) > 100) {
                    Set<Integer> newZSet = new HashSet<>();
                    double diff = otherHailstone.position.z - hailstone.position.z;
                    for (int i = -1000; i < 1000; i++) {
                        if (i == hailstone.velocity.z)
                            continue;
                        if (diff % (i - hailstone.velocity.z) == 0)
                            newZSet.add(i);
                    }
                    if (potentialZ.isEmpty())
                        potentialZ.addAll(newZSet);
                    else
                        potentialZ.retainAll(newZSet);
                }
            }
        }
        int potX = potentialX.first();
        int potY = potentialY.first();
        int potZ = potentialZ.first();
        Hailstone first = hailstones.get(0);
        Hailstone second = hailstones.get(1);
        double mf = (first.velocity.y - potY) / (first.velocity.x - potX);
        double ms = (second.velocity.y- potY) / (second.velocity.x - potX);
        double cf = first.position.y - (mf * first.position.x);
        double cs = second.position.y - (ms * second.position.x);
        long resultX = Double.valueOf((cs - cf) / (mf - ms)).longValue();
        long resultY = Double.valueOf(mf * resultX + cf).longValue();
        double time = (resultX - first.position.x) / (first.velocity.x - potX);
        long resultZ = Double.valueOf(first.position.z + (first.velocity.z - potZ) * time).longValue();
        double result = resultX + resultY + resultZ;
        System.out.println(STR."Sum of coordinates gives \{Double.valueOf(result).longValue()}");

    }

    record Hailstone(Point position, Point velocity) {

        static Hailstone fromLine(String line) {
            Scanner scanner = new Scanner(line);
            Point position = new Point(scanner.nextLong(), scanner.nextLong(), scanner.nextLong());
            Point velocity = new Point(scanner.nextLong(), scanner.nextLong(), scanner.nextLong());
            return new Hailstone(position, velocity);
        }

        Point intersectionPoint(Hailstone hailstone) {
            Point p1 = this.position;
            Point p2 = new Point(p1.x + velocity.x, p1.y + velocity.y, 0);
            Point p3 = hailstone.position;
            Point p4 = new Point(p3.x + hailstone.velocity.x, p3.y + hailstone.velocity.y, 0);

            double denominator = (p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x);
            if (denominator == 0) return null;
            double px = ((p1.x * p2.y - p1.y * p2.x) * (p3.x - p4.x) - (p1.x - p2.x) * (p3.x * p4.y - p3.y * p4.x)) / denominator ;
            double py = ((p1.x * p2.y - p1.y * p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x * p4.y - p3.y * p4.x)) / denominator ;
            return new Point(px, py, 0);
        }

        boolean isBehind(Point point) {
            if (velocity.x > 0 && point.x < position.x) return true;
            return velocity.x <= 0 && point.x >= position.x;
        }
    }

    record Point(double x, double y, double z) {
        boolean inArea(double min, double max) {
            return x >= min && x <= max && y >= min && y <= max;
        }
    }
}
