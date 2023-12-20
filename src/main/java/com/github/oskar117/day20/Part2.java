package com.github.oskar117.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Part2 {

    private final static String FILE_PATH = "input/day20/";
    private final static String FILE_NAME = "data3.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        Map<String, Module> modules = Files.readAllLines(path)
                .stream()
                .map(Module::fromString)
                .collect(Collectors.toMap(Module::getName, Function.identity()));

        List<ConjuctionModule> conjunctionModules = modules.values().stream().filter(module -> module instanceof ConjuctionModule).map(m -> (ConjuctionModule) m).toList();
        for (ConjuctionModule module : conjunctionModules) {
            List<String> inputs = modules.values()
                    .stream()
                    .filter(m -> m.getOutputs().contains(module.name))
                    .map(Module::getName)
                    .toList();
            module.setInputs(inputs);
        }

        long qt = 0, ng = 0, mp = 0, qb = 0;
        for (int numberOfPresses = 0; qt * ng * mp * qb == 0 ; numberOfPresses++) {
            Queue<Pulse> queue = new ArrayDeque<>();
            queue.add(new Pulse("button", "broadcaster", SignalStrength.LOW));
            while (!queue.isEmpty()) {
                Pulse currentPulse = queue.remove();
                if (!modules.containsKey(currentPulse.to()))
                    continue;
                if (currentPulse.from().equals("qt") && currentPulse.signalStrength() == SignalStrength.HIGH) qt = numberOfPresses + 1;
                if (currentPulse.from().equals("ng") && currentPulse.signalStrength() == SignalStrength.HIGH) ng = numberOfPresses + 1;
                if (currentPulse.from().equals("mp") && currentPulse.signalStrength() == SignalStrength.HIGH) mp = numberOfPresses + 1;
                if (currentPulse.from().equals("qb") && currentPulse.signalStrength() == SignalStrength.HIGH) qb = numberOfPresses + 1;
                Module destination = modules.get(currentPulse.to());
                List<Pulse> newPulses = destination.processPulse(currentPulse);
                queue.addAll(newPulses);
            }
        }

        long result = qt * ng * mp * qb;
        System.out.println("Fewest number of presses required is " + result);
    }
}
