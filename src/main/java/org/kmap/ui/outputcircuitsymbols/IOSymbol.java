package org.kmap.ui.outputcircuitsymbols;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class IOSymbol extends LogicGateSymbol {

    public IOSymbol(GraphicsContext gc, double centerX, double centerY, char name) {
        super(gc, centerX, centerY);
        gc.strokeText(String.valueOf(name), centerX - 10, centerY + 5);
    }

    @Override
    public double setInputs(List<LogicGateSymbol> inputs, double lineOffset) {
        inputGates = inputs;
        for (LogicGateSymbol input : inputs) {
            gc.strokeLine(input.getOutputX(), input.getOutputY(), input.getOutputX() + lineOffset, input.getOutputY());
            gc.strokeLine(input.getOutputX() + lineOffset, input.getOutputY(), input.getOutputX() + lineOffset, centerY);
            gc.strokeLine(input.getOutputX() + lineOffset, centerY, getInputX(0), centerY);
            lineOffset += offsetChange;
        }
        return lineOffset;
    }

    @Override
    public double getOutputX() {
        return centerX;
    }

    @Override
    protected double getInputX(double inputY) {
        return centerX - 13;
    }
}
