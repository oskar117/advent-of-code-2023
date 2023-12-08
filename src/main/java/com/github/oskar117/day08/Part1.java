package com.github.oskar117.day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Part1 {

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

        int step = 0;
        String currentKey = "AAA";
        while (!currentKey.equals("ZZZ")) {
            String instruction = instructions[step % instructions.length];
            Node node = nodeMap.get(currentKey);
            currentKey = instruction.equals("R") ? node.right : node.left;
            step++;
        }
        System.out.println("You need " + step + " steps to reach ZZZ");
    }

    record Node(String left, String right) {}
}
