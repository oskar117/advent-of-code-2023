package com.github.oskar117.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class Part2 {
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
                    int verticalCount = countRecordsAboveHorizontalLine(reversed, - 1, false);
                    int horizontalCount = countRecordsAboveHorizontalLine(block, -1, false);
                    verticalCount = countRecordsAboveHorizontalLine(reversed, verticalCount, true);
                    if (verticalCount >= 0) return verticalCount;
                    horizontalCount = countRecordsAboveHorizontalLine(block, horizontalCount, true);
                    return horizontalCount < 0 ? 0 : horizontalCount * 100;
                })
                .sum();
        System.out.println("Sum of all notes is " + result);
    }

    private static int countRecordsAboveHorizontalLine(char[][] block, int lastResult, boolean withSmudgeFix) {
        for (int y = 0; y < block.length - 1; y++) {
            if (isMirrored(block, y, y + 1, withSmudgeFix) && y + 1 != lastResult) return y + 1;
        }
        return -1;
    }

    private static boolean isMirrored(char[][] block, int a, int b, boolean smudgeAvailable) {
        if (a < 0 || b > block.length - 1) return true;
        if (Arrays.equals(block[a], block[b])) {
            return isMirrored(block, a - 1, b + 1, smudgeAvailable);
        }
        if (smudgeAvailable && eligibleForSmudgeFix(block[a], block[b])) {
            return isMirrored(block, a - 1, b + 1, false);
        }
        return false;
    }

    private static boolean eligibleForSmudgeFix(char[] a, char[] b) {
        int differences = 0;
        for (int y = 0; y < a.length; y++) {
            differences += a[y] != b[y] ? 1 : 0;
        }
        return differences == 1;
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