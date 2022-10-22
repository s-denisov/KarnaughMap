package org.kmap.ui.inputcircuitsymbols;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.logic.logicelements.NotElement;
import org.kmap.ui.InputCircuit;

public class NotGate extends GateSymbol {

    public NotGate(ImageView imageView, double centerX, double centerY, InputCircuit parent) {
        super(imageView, centerX, centerY, parent);
    }

    @Override
    public double getInputsX() {
        return centerX - 30;
    }

    @Override
    public double[] getInputsY() {
        return new double[]{centerY};
    }

    @Override
    public double getOutputX() {
        return centerX + 42;
    }

    @Override
    protected LogicElement getCorrespondingLogicElement() {
        return new NotElement();
    }
}
