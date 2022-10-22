package org.kmap.ui.inputcircuitsymbols;

import javafx.scene.Node;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.ui.InputCircuit;

import java.util.List;

public interface GateOrIO {
    List<Node> getRepresentation();

    LogicElement findLogic();

    void update(InputCircuit parent);
}
