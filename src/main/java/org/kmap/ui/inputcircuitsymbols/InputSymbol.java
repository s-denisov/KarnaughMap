package org.kmap.ui.inputcircuitsymbols;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import org.kmap.logic.logicelements.InputElement;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.ui.InputCircuit;

import java.util.ArrayList;
import java.util.List;

public class InputSymbol implements WithOutput {

    private final List<Node> representation = new ArrayList<>();
    private final char name;
    private final GateOutput outputCircle;

    public InputSymbol(AnchorPane root, int gateNumber, GateOutput.ActiveChecker activeChecker) { // gateNumber is 0 to 3 inclusive
        name = (char) (gateNumber + 65);
        Label letter = new Label(Character.toString(name));
        root.getChildren().add(letter);
        AnchorPane.setLeftAnchor(letter, 8.0);
        double yCoOrdinates = (gateNumber + 0.5) * root.getPrefHeight() / 4;
        AnchorPane.setTopAnchor(letter, yCoOrdinates);
        outputCircle = new GateOutput(root, representation, 20, yCoOrdinates + 6, activeChecker);
        representation.add(letter);
    }

    @Override
    public List<Node> getRepresentation() {
        return representation;
    }

    @Override
    public void handleOutputGate(GateOrIO outputGate) {
        if (outputGate instanceof WithInput) {
            if (outputCircle.finishLine()) {
                ((WithInput) outputGate).addInput(this);
            }
        }
    }

    @Override
    public LogicElement findLogic() {
        return new InputElement(name);
    }

    @Override
    public void update(InputCircuit parent) {
        outputCircle.update(parent.getDrawing());
    }

    @Override
    public void resetOutputLines() {
        outputCircle.reset();
        representation.removeIf(x -> x.getClass() == Line.class);
    }
}
