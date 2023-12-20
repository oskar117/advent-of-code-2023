package com.github.oskar117.day20;

import java.util.List;

class FlipFlopModule extends Module {

    private boolean isOn;

    FlipFlopModule(String name, List<String> outputs) {
        super(name, outputs);
    }

    @Override
    List<Pulse> processPulse(Pulse pulse) {
        if (pulse.signalStrength() == SignalStrength.HIGH)
            return List.of();
        SignalStrength newStrength = isOn ? SignalStrength.LOW : SignalStrength.HIGH;
        isOn = !isOn;
        return this.outputs.stream()
                .map(output -> new Pulse(this.name, output, newStrength))
                .toList();
    }
}
