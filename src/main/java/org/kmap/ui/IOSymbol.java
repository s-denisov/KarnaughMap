package org.kmap.ui;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class IOSymbol implements LogicGateSymbol {

    private final GraphicsContext gc;
    private final double centerX;
    private final double centerY;

    public IOSymbol(GraphicsContext gc, double centerX, double centerY, String name) {
        this.gc = gc; this.centerX = centerX; this.centerY = centerY;
        gc.strokeText(name, centerX - 10, centerY + 5);
    }

    @Override
    public void setInputs(ArrayList<LogicGateSymbol> inputs, int lineOffset) {
        for (LogicGateSymbol input : inputs) {
            gc.strokeLine(input.getOutputX(), input.getOutputY(), input.getOutputX() + lineOffset, input.getOutputY());
            gc.strokeLine(input.getOutputX() + lineOffset, input.getOutputY(), input.getOutputX() + lineOffset, centerY);
            gc.strokeLine(input.getOutputX() + lineOffset, centerY, centerX - 13, centerY);
            lineOffset += 3;
        }
    }

    @Override
    public double getOutputX() {
        return centerX;
    }

    @Override
    public double getOutputY() {
        return centerY;
    }
}
