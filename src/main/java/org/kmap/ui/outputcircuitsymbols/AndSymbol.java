package org.kmap.ui.outputcircuitsymbols;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

public class AndSymbol extends LogicGateSymbol {

    private final int WIDTH = 20;

    public AndSymbol(GraphicsContext gc, double centerX, double centerY) {
        super(gc, centerX, centerY);
        int HEIGHT = 20;
        gc.strokeLine(centerX - WIDTH, centerY - HEIGHT, centerX - WIDTH, centerY + HEIGHT);
        gc.strokeLine(centerX - WIDTH, centerY - HEIGHT, centerX + WIDTH, centerY - HEIGHT);
        gc.strokeLine(centerX - WIDTH, centerY + HEIGHT, centerX + WIDTH, centerY + HEIGHT);
        gc.strokeArc(centerX, centerY - HEIGHT, WIDTH * 2, HEIGHT * 2, 270, 180, ArcType.OPEN);
    }


    @Override
    public double getOutputX() {
        return centerX + WIDTH * 2;
    }

    @Override
    protected double getInputX(double inputY) {
        return centerX - WIDTH;
    }
}
