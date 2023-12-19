package com.github.oskar117.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part1 {
    private final static String FILE_PATH = "input/day19/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        String[] input = Files.readString(path).split("\n\n");
        Map<String, Workflow> workflowMap = Arrays.stream(input[0].split("\n"))
                .map(Workflow::new)
                .collect(Collectors.toMap(Workflow::name, Function.identity()));
        List<Part> parts = Arrays.stream(input[1].split("\n")).map(Part::new).toList();

        List<Part> acceptedParts = new ArrayList<>();
        for (Part part : parts) {
            String workflowName = "in";
            while (!workflowName.equals("R")) {
                Workflow workflow = workflowMap.get(workflowName);
                String newName = workflow.process(part);
                if (newName.equals("A")) {
                    acceptedParts.add(part);
                    break;
                }
                workflowName = newName;
            }
        }
        int result = acceptedParts.stream().mapToInt(Part::sum).sum();
        System.out.println("Sum of ratings of accepted parts is " + result);
    }

    static class Workflow {
        private static final Pattern PATTERN = Pattern.compile("(\\w+)\\{(.+)}");

        private final String name;
        Queue<Condition> conditions;

        Workflow(String line) {
            Matcher matcher = PATTERN.matcher(line);
            matcher.find();
            this.name = matcher.group(1);
            String[] conditions = matcher.group(2).split(",");
            this.conditions = Arrays.stream(conditions)
                    .map(Condition::new)
                    .collect(Collectors.toCollection(ArrayDeque::new));
        }

        String name() {
            return name;
        }

        String process(Part part) {
            for (Condition condition : conditions) {
                boolean evaluate = condition.evaluate(part);
                if (evaluate) return condition.redirectName;
            }
            throw new RuntimeException("Unable to process");
        }
    }

    static class Condition {
        private static final Pattern PATTERN = Pattern.compile("(\\w)([<>])(\\d+):(\\w+)");

        Rating rating;
        String sign;
        int value;
        String redirectName;

        public Condition(String condiitonString) {
            Matcher matcher = PATTERN.matcher(condiitonString);
            boolean isFound = matcher.find();
            if (isFound) {
                this.rating = Rating.valueOf(matcher.group(1).toUpperCase());
                this.sign =matcher.group(2);
                this.value = Integer.parseInt(matcher.group(3));
                this.redirectName = matcher.group(4);
            } else {
                this.redirectName = condiitonString;
            }
        }

        boolean evaluate(Part part) {
            if (sign == null) return true;
            int partValue = part.getMapping(rating);
            return switch (sign) {
                case ">" -> partValue > value;
                case "<" -> partValue < value;
                default -> throw new RuntimeException("Invalid sign");
            };
        }

    }

    enum Rating {
        X, M, A, S
    }

    static class Part {
        Map<Rating, Integer> ratings = new HashMap<>();

        public Part(String line) {
            Arrays.stream(line.replaceAll("[{}]", "").split(",")).forEach((entry) -> {
                String[] split = entry.split("=");
                Rating rating = Rating.valueOf(split[0].toUpperCase());
                int value = Integer.parseInt(split[1]);
                ratings.put(rating, value);
            });
        }

        int getMapping(Rating rating) {
            return ratings.get(rating);
        }

        int sum() {
            return ratings.values().stream().mapToInt(Integer::intValue).sum();
        }
    }
}


