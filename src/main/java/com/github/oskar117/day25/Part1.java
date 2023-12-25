package com.github.oskar117.day25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

class Part1 {

    private final static String FILE_PATH = "input/day25";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        HashMap<String, Set<String>> components = new HashMap<>();
        for (String line : Files.readAllLines(path)) {
            LinkedHashSet<String> in = Arrays.stream(line.split(": | ")).collect(Collectors.toCollection(LinkedHashSet::new));
            String name = in.removeFirst();
            if (components.containsKey(name)) {
                components.get(name).addAll(in);
            } else {
                components.put(name, in);
            }
            for (String value : in) {
                if (value.equals(name))
                    continue;
                if (components.containsKey(value)) {
                    components.get(value).add(name);
                } else {
                    components.put(value, new LinkedHashSet<>(Set.of(name)));
                }
            }
        }
        List<Pair> pairs = new ArrayList<>();
        for (var entry : components.entrySet()) {
            for (String right : entry.getValue()) {
                Pair p = new Pair(entry.getKey(), right);
                Pair reversed = new Pair(right, entry.getKey());
                if ((!pairs.contains(reversed) || !pairs.contains(p)) && !entry.getKey().equals(right))
                    pairs.add(p);
            }
        }

        // https://en.wikipedia.org/wiki/Karger%27s_algorithm
        ThreadLocalRandom random = ThreadLocalRandom.current();
        while (true) {
            Set<Set<String>> subgroups = components.keySet()
                    .stream()
                    .map(v -> new HashSet<>(Set.of(v)))
                    .collect(Collectors.toCollection(HashSet::new));
            while (subgroups.size() > 2) {
                Pair pair = pairs.get(random.nextInt(pairs.size()));
                Set<String> s1 = subgroups.stream().filter(p -> p.contains(pair.a)).findFirst().orElseThrow();
                Set<String> s2 = subgroups.stream().filter(p -> p.contains(pair.b)).findFirst().orElseThrow();
                if (s1.equals(s2))
                    continue;
                s1.addAll(s2);
                subgroups.removeIf(e -> e.equals(s2));
            }
            if (pairs.stream().filter(p -> !subgroups.stream().filter(s -> s.contains(p.a)).collect(Collectors.toCollection(HashSet::new))
                    .equals(subgroups.stream().filter(s -> s.contains(p.b)).collect(Collectors.toCollection(HashSet::new)))).count() > 3)
                continue;
            int result = subgroups.stream().map(Set::size).reduce(1, (a, b) -> a * b);
            System.out.println(STR."Multiplication of group sizes gives \{result}");
            break;
        }
    }

    record Pair(String a, String b) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(a, pair.a) && Objects.equals(b, pair.b) || Objects.equals(a, pair.b) && Objects.equals(b, pair.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }
}
