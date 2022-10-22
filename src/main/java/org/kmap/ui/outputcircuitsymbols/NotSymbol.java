package org.kmap.ui.outputcircuitsymbols;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

public class NotSymbol extends LogicGateSymbol {

    public NotSymbol(GraphicsContext gc, double centerX, double centerY) {
        super(gc, centerX, centerY);
        gc.strokePolygon(new double[]{centerX - 25, centerX - 25, centerX + 25}, new double[]{centerY - 20, centerY + 20, centerY}, 3);
        gc.strokeArc(centerX + 25, centerY - 5, 10, 10, 0, 360, ArcType.OPEN);
    }

    @Override
    public double getOutputX() {
        return centerX + 35;
    }

    @Override
    protected double getInputX(double inputY) {
        return centerX - 25;
    }
}
