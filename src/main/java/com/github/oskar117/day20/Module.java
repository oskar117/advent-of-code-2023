package com.github.oskar117.day20;

import java.util.List;

abstract class Module {

    protected final String name;
    protected final List<String> outputs;

    Module(String name, List<String> outputs) {
        this.name = name;
        this.outputs = outputs;
    }

    static Module fromString(String line) {
        String[] in = line.split(" -> ");
        List<String> outputs = List.of(in[1].split(", "));
        return switch (in[0].charAt(0)) {
            case 'b' -> new BroadcasterModule(in[0], outputs);
            case '%' -> new FlipFlopModule(in[0].substring(1), outputs);
            case '&' -> new ConjuctionModule(in[0].substring(1), outputs);
            default -> throw new IllegalStateException("Unexpected value: " + in[0].charAt(0));
        };
    }

    abstract List<Pulse> processPulse(Pulse pulse);

    String getName() {
        return name;
    }

    List<String> getOutputs() {
        return outputs;
    }

    @Override
    public String toString() {
        return String.valueOf(outputs);
    }
}
