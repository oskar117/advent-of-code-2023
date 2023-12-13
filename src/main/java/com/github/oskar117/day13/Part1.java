package com.github.oskar117.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class Part1 {
    private final static String FILE_PATH = "input/day13/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        int result = Arrays.stream(Files.readString(path).split("\n\n"))
                .map(block -> Arrays.stream(block.split("\n"))
                        .map(String::toCharArray)
                        .toArray(char[][]::new))
                .mapToInt(block -> {
                    char[][] reversed = reverseBlock(block);
                    int verticalCount = countRecordsAboveHorizontalLine(reversed);
                    if (verticalCount >= 0) return verticalCount;
                    int horizontalCount = countRecordsAboveHorizontalLine(block);
                    return horizontalCount < 0 ? 0 : horizontalCount * 100;
                })
                .sum();
        System.out.println("Sum of all notes is " + result);
    }

    private static int countRecordsAboveHorizontalLine(char[][] block) {
        for (int y = 0; y < block.length - 1; y++) {
            if (isMirrored(block, y, y + 1)) return y + 1;
        }
        return -1;
    }

    private static boolean isMirrored(char[][] block, int a, int b) {
        if (a < 0 || b > block.length - 1) return true;
        if (Arrays.equals(block[a], block[b])) {
            return isMirrored(block, a - 1, b + 1);
        }
        return false;
    }

    private static char[][] reverseBlock(char[][] block) {
        char[][] result = new char[block[0].length][block.length];
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                result[j][block.length - 1 - i] = block[i][j];
            }
        }
        return result;
    }
}