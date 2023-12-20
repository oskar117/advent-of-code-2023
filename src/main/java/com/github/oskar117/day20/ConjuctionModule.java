package com.github.oskar117.day20;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ConjuctionModule extends Module {

    private final Map<String, SignalStrength> lastSignals = new HashMap<>();

    ConjuctionModule(String name, List<String> outputs) {
        super(name, outputs);
    }

    @Override
    List<Pulse> processPulse(Pulse pulse) {
        this.lastSignals.put(pulse.from(), pulse.signalStrength());
        SignalStrength newStrength = this.lastSignals.values().stream().allMatch(signalStrength -> signalStrength == SignalStrength.HIGH)
                ? SignalStrength.LOW
                : SignalStrength.HIGH;
        return this.outputs.stream()
                .map(output -> new Pulse(this.name, output, newStrength))
                .toList();
    }

    void setInputs(List<String> inputs) {
        inputs.forEach(in -> lastSignals.put(in, SignalStrength.LOW));
    }
}

