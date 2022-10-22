package org.kmap.ui.inputcircuitsymbols;

public interface WithInput extends GateOrIO {
    void addInput(GateOrIO input);

    void removeInput(GateOrIO input);
}
