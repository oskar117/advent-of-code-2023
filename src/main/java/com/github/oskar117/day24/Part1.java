package com.github.oskar117.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

class Part1 {

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
                .toList();

        int result = 0;
        for (int a = 0; a < hailstones.size() - 1; a++) {
            for (int b = a + 1; b < hailstones.size(); b++) {
                Hailstone hailstone = hailstones.get(a);
                Hailstone otherHailstone = hailstones.get(b);
                if (hailstone.equals(otherHailstone))
                    continue;
                Point intersectionPoint = hailstone.intersectionPoint(otherHailstone);
                if (intersectionPoint == null)
                    continue;
                if(hailstone.isBehind(intersectionPoint) || otherHailstone.isBehind(intersectionPoint))
                    continue;
                if (!intersectionPoint.inArea(MIN, MAX))
                //if (!intersectionPoint.inArea(7, 27))
                    continue;
                result++;
            }
        }
        System.out.println(STR."\{result} intersections occur within the area");
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
