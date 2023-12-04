package com.github.oskar117.day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class Part2 {

    public static void main(String[] args) throws IOException {
        String filePath = "input/day04/";
        String[] filesPart1 = new String[]{"data2.txt"};
        Path path = Path.of(filePath, filesPart1[0]).toAbsolutePath();
        List<String> linesList = Files.readAllLines(path);
        int[] instances = new int[linesList.size()];
        Arrays.fill(instances, 1);

        for (int cardIndex = 0; cardIndex < linesList.size(); cardIndex++) {
            String cardLine = linesList.get(cardIndex);
            List<List<Integer>> numbers = Arrays.stream(cardLine
                            .substring(cardLine.indexOf(":") + 1)
                            .split("\\|"))
                    .map(n -> Arrays.stream(n
                                    .trim()
                                    .split("\\s+"))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .toList())
                    .toList();

            List<Integer> winningNumbers = numbers.get(0);
            List<Integer> possessedNumbers = numbers.get(1);
            long matches = possessedNumbers.stream().filter(winningNumbers::contains).count();
            for (int cardCopyIndex = cardIndex + 1; cardCopyIndex < cardIndex + matches + 1; cardCopyIndex++) {
                for (int instanceNumber = 0; instanceNumber < instances[cardIndex]; instanceNumber++) {
                    instances[cardCopyIndex]++;
                }
            }
        }
        System.out.println("You end up with " + Arrays.stream(instances).sum() + " scratchcards.");
    }
}
