package com.github.oskar117.day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part2 {

    private final static String FILE_PATH = "input/day08/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        List<String> allLines = Files.readAllLines(path);
        String[] instructions = allLines.get(0).split("");
        Map<String, Node> nodeMap = new HashMap<>();
        allLines.subList(2, allLines.size()).forEach((line) -> {
            Pattern pattern = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String key = matcher.group(1);
            String left = matcher.group(2);
            String right = matcher.group(3);
            nodeMap.put(key, new Node(left, right));
        });

        Long result = nodeMap.keySet().stream().filter(k -> k.endsWith("A")).map(start -> {
            long step = 0;
            String currNode = start;
            while (!currNode.endsWith("Z")) {
                String instruction = instructions[(int) (step++ % instructions.length)];
                Node node = nodeMap.get(currNode);
                currNode = instruction.equals("R") ? node.right : node.left;
            }
            return step;
        }).reduce(1L, (a, b) -> a * b / gcd(a, b));

        System.out.println("You need " + result + " to reach all nodes ending with Z");
    }

    private static long gcd(long a, long b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    record Node(String left, String right) {
    }
}
