package com.github.oskar117.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Part2 {
    private final static String FILE_PATH = "input/day19/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        String[] input = Files.readString(path).split("\n\n");
        Map<String, Workflow> workflowMap = Arrays.stream(input[0].split("\n"))
                .map(Workflow::new)
                .collect(Collectors.toMap(Workflow::name, Function.identity()));

        Map<Rating, Range> ratings = new HashMap<>();
        Arrays.stream(Rating.values()).forEach(r -> ratings.put(r, new Range(1, 4000)));
        String workflowName = "in";
        List<Map<Rating, Range>> explored = exploreRating(workflowMap, ratings, workflowName);

        long result = 0;
        for (var map : explored) {
            long temp = 1;
            for (Range range : map.values()) {
                temp *= range.max - range.min + 1;
            }
            result += temp;
        }

        System.out.println(result + " distinct values can be accepted by the elves");
    }

    private static List<Map<Rating, Range>> exploreRating(Map<String, Workflow> workflowMap, Map<Rating, Range> ratings, String workflowName) {
        if (workflowName.equals("A")) return List.of(ratings);
        if (workflowName.equals("R")) return List.of();
        Workflow workflow = workflowMap.get(workflowName);
        List<Map<Rating, Range>> validRanges = new ArrayList<>();
        for (Condition condition : workflow.conditions) {
            if (condition.sign == null) {
                List<Map<Rating, Range>> explored = exploreRating(workflowMap, new HashMap<>(Map.copyOf(ratings)), condition.redirectName);
                validRanges.addAll(explored);
                continue;
            }
            Range range = ratings.get(condition.rating);
            Range trueRange, falseRange;

            if (condition.sign.equals("<")) {
                trueRange = new Range(range.min, condition.value - 1);
                falseRange = new Range(condition.value, range.max);
            } else {
                trueRange = new Range(condition.value + 1, range.max);
                falseRange = new Range(range.min, condition.value);
            }
            if (trueRange.min <= trueRange.max) {
                Map<Rating, Range> newMap = new HashMap<>(Map.copyOf(ratings));
                newMap.put(condition.rating, trueRange);
                List<Map<Rating, Range>> explored = exploreRating(workflowMap, newMap, condition.redirectName);
                validRanges.addAll(explored);
            }
            if (falseRange.min <= falseRange.max) {
                ratings = new HashMap<>(Map.copyOf(ratings));
                ratings.put(condition.rating, falseRange);
            }
        }
        return validRanges;
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
                this.sign = matcher.group(2);
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

    record Range(int min, int max) {
        Range merge(Range range) {
            int newMin = Math.max(range.min, this.min);
            int newMax = Math.min(range.max, this.max);
            return new Range(newMin, newMax);
        }
    }
}


