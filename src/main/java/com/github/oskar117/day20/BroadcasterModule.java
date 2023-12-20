package com.github.oskar117.day20;

import java.util.List;

class BroadcasterModule extends Module {

    BroadcasterModule(String name, List<String> outputs) {
        super(name, outputs);
    }

    @Override
    List<Pulse> processPulse(Pulse pulse) {
        return this.outputs.stream()
                .map(output -> new Pulse(this.name, output, pulse.signalStrength()))
                .toList();
    }
}
