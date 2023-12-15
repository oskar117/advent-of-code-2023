package com.github.oskar117.day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class Part2 {
    private final static String FILE_PATH = "input/day15/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        List<LinkedList<Lens>> hashmap = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            hashmap.add(i, new LinkedList<>());
        }
        Arrays.stream(Files.readString(path).split(","))
                .forEach(seq -> {
                    String[] addInput = seq.split("=");
                    String label = addInput.length == 1 && seq.endsWith("-")
                            ? seq.substring(0, seq.length() - 1)
                            : addInput[0];
                    int hash = hash(label);
                    LinkedList<Lens> lenses = hashmap.get(hash);
                    if (seq.endsWith("-")) {
                        lenses.removeIf(lens -> lens.label.equals(label));
                        return;
                    }
                    Optional<Lens> lens = lenses.stream().filter(f -> f.label.equals(label)).findFirst();
                    int focalLength = Integer.parseInt(addInput[1]);
                    if (lens.isEmpty()) {
                        lenses.add(new Lens(label, focalLength));
                    } else {
                        lens.get().focalLength = focalLength;
                    }
                });

        int result = 0;
        for (int b = 0; b < hashmap.size(); b++) {
            LinkedList<Lens> lenses = hashmap.get(b);
            for (int x = 0; x < lenses.size(); x++) {
                result += lenses.get(x).focalLength * (x + 1) * (b + 1);
            }
        }
        System.out.println("Sum of the results is " + result);
    }

    private static int hash(String seq) {
        return seq.chars().reduce(0, (a, b) -> {
            a += b;
            a *= 17;
            return a % 256;
        });
    }

    static class Lens {
        public String label;
        public int focalLength;

        Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }
    }
}
