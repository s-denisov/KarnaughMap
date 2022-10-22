package org.kmap.ui.inputcircuitsymbols;

public interface WithOutput extends GateOrIO {
    void handleOutputGate(GateOrIO outputGate);

    void resetOutputLines();
}
