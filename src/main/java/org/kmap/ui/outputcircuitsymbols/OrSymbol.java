package org.kmap.ui.outputcircuitsymbols;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

public class OrSymbol extends LogicGateSymbol {

    public OrSymbol(GraphicsContext gc, double centerX, double centerY) {
        super(gc, centerX, centerY);
        gc.strokeArc(centerX - 40, centerY - 20, 30, 40, 300, 120, ArcType.OPEN);
        gc.strokeArc(centerX - 158, centerY - 20, 200, 40, -67, 134, ArcType.OPEN);
    }

    @Override
    public double getOutputX() {
        return centerX + 43;
    }

    @Override
    protected double getInputX(double inputY) {
        return 30 * Math.sqrt(1 - Math.pow(inputY - centerY /*+20*/, 2) / 1600) + centerX - 42;
    }
}
