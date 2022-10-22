package org.kmap.ui.inputcircuitsymbols;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.logic.logicelements.OutputElement;
import org.kmap.ui.InputCircuit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OutputSymbol implements WithInput {

    private List<Node> representation = new ArrayList<>();
    private GateOrIO input;

    public OutputSymbol(AnchorPane root, Consumer<GateOrIO> onInputSelected) {
        Label letter = new Label("Q");
        root.getChildren().add(letter);
        AnchorPane.setRightAnchor(letter, 8.0);
        double yCoOrdinates = root.getPrefHeight() / 2;
        AnchorPane.setTopAnchor(letter, yCoOrdinates);
        Circle inputCircle = GateOutput.createIOCircle(root.getPrefWidth() - 20, yCoOrdinates + 6);
        inputCircle.setOnMousePressed(e -> onInputSelected.accept(this));
        root.getChildren().add(inputCircle);
        representation.add(inputCircle);
        representation.add(letter);
    }

    @Override
    public List<Node> getRepresentation() {
        return representation;
    }

    @Override
    public void addInput(GateOrIO input) {
        this.input = input;
    }

    @Override
    public void removeInput(GateOrIO input) {
        if (this.input == input) {
            this.input = null;
        }
    }

    @Override
    public LogicElement findLogic() {
        return new OutputElement(input.findLogic());
    }

    @Override
    public void update(InputCircuit parent) {
    }
}
