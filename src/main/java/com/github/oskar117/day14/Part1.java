package com.github.oskar117.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Part1 {
    private final static String FILE_PATH = "input/day14/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] platform = Arrays.stream(Files.readString(path).split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        for (int x = 0; x < platform[0].length; x++) {
            for (int y = 1; y < platform.length; y++) {
                for (int i = 1; i < platform.length; i++) {
                    if (platform[i][x] == 'O') {
                        if (platform[i - 1][x] == '.') {
                            platform[i][x] = '.';
                            platform[i - 1][x] = 'O';
                        }
                    }
                }
            }
        }

        int result = 0;
        for (int y = 0; y < platform.length; y++) {
            for (int x = 0; x < platform[0].length; x++) {
                if (platform[y][x] == 'O')
                    result += platform.length - y;
            }
        }
        System.out.println("Load to the north beams is " + result);
    }
}
