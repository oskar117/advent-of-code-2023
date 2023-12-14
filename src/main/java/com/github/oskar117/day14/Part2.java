package com.github.oskar117.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Part2 {
    private final static String FILE_PATH = "input/day14/";
    private final static String FILE_NAME = "data2.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        char[][] platform = Arrays.stream(Files.readString(path).split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        Set<String>cache = new HashSet<>();

        long cycles = 1000000000;
        long repeatIndex = -1;
        for (long cycle = 0; cycle < cycles; cycle++) {
            String key = Arrays.stream(platform).map(String::new).collect(Collectors.joining("\n"));
            if (cache.contains(key)) {
                if (repeatIndex < 0) {
                    repeatIndex = cycle;
                    cache.clear();
                } else {
                    long diff = cycle - repeatIndex;
                    long timesToAdd = (cycles - repeatIndex) / diff;
                    cycle = (diff * timesToAdd) + repeatIndex;
                }
            }
            cache.add(key);
            for (int s = 0; s < 4; s++) {
                shake(platform);
                platform = spin(platform);
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

    private static void shake(char[][] platform) {
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
    }

    private static char[][] spin(char[][] platform) {
        char[][] result = new char[platform[0].length][platform.length];
        for (int i = 0; i < platform.length; i++) {
            for (int j = 0; j < platform[0].length; j++) {
                result[j][platform.length - 1 - i] = platform[i][j];
            }
        }
        return result;
    }
}
