package org.kmap.ui.inputcircuitsymbols;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.kmap.logic.logicelements.AndElement;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.ui.InputCircuit;

public class AndGate extends GateSymbol {

    public AndGate(ImageView imageView, double centerX, double centerY, InputCircuit parent) {
        super(imageView, centerX, centerY, parent);
    }

    @Override
    public double getInputsX() {
        return centerX - 30;
    }

    @Override
    public double[] getInputsY() {
        return new double[]{centerY - 10, centerY + 13};
    }

    @Override
    public double getOutputX() {
        return centerX + 40;
    }

    @Override
    protected LogicElement getCorrespondingLogicElement() {
        return new AndElement();
    }
}
