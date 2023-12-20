package com.github.oskar117.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Part1 {

    private final static String FILE_PATH = "input/day20/";
    private final static String FILE_NAME = "data3.txt";

    public static void main(String[] args) throws IOException {
        Path path = Path.of(FILE_PATH, FILE_NAME).toAbsolutePath();
        Map<String, Module> modules = Files.readAllLines(path)
                .stream()
                .map(Module::fromString)
                .collect(Collectors.toMap(Module::getName, Function.identity()));

        List<ConjuctionModule> conjunctionModules = modules.values().stream().filter(module -> module instanceof ConjuctionModule).map(m -> (ConjuctionModule)m).toList();
        for (ConjuctionModule module : conjunctionModules) {
            List<String> inputs = modules.values()
                    .stream()
                    .filter(m -> m.getOutputs().contains(module.name))
                    .map(Module::getName)
                    .toList();
            module.setInputs(inputs);
        }

        int lowPulses = 0, highPulses = 0;
        for (int i = 0; i < 1000; i++) {
            Queue<Pulse> queue = new ArrayDeque<>();
            queue.add(new Pulse("button", "broadcaster", SignalStrength.LOW));
            while (!queue.isEmpty()) {
                Pulse currentPulse = queue.remove();
                if (currentPulse.signalStrength() == SignalStrength.LOW)
                    lowPulses++;
                else
                    highPulses++;
                if (!modules.containsKey(currentPulse.to()))
                    continue;
                Module destination = modules.get(currentPulse.to());
                List<Pulse> newPulses = destination.processPulse(currentPulse);
                queue.addAll(newPulses);
            }
        }

        int result = lowPulses * highPulses;
        System.out.println("Multiplication of total number of pulses is " + result);
    }

}
