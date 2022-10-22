package org.kmap.ui.outputcircuitsymbols;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public abstract class LogicGateSymbol {
    protected final GraphicsContext gc;
    protected final double centerX;
    protected final double centerY;
    protected ArrayList<LogicGateSymbol> inputGates = new ArrayList<>();
    protected static double offsetChange = 3;

    public LogicGateSymbol(GraphicsContext gc, double centerX, double centerY) {
        this.gc = gc;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public double setInputs(ArrayList<LogicGateSymbol> inputs, double lineOffset) {
        inputGates =  inputs;
        for (int i = 0; i < inputs.size(); i++) {
            LogicGateSymbol input = inputs.get(i);
            gc.strokeLine(input.getOutputX(), input.getOutputY(), input.getOutputX() + lineOffset, input.getOutputY());
            gc.fillArc(input.getOutputX() + lineOffset - 3, input.getOutputY() - 3, 5, 5, 360, 360, ArcType.OPEN);
            double correction = inputs.size() == 1 ? 20 - 5 : (double) (2 * 20 - 10) * i / (inputs.size() - 1);
            double inputY = centerY - 20 + 5 + correction;
            gc.strokeLine(input.getOutputX() + lineOffset, input.getOutputY(), input.getOutputX() + lineOffset, inputY);
            gc.fillArc(input.getOutputX() + lineOffset - 3, inputY - 3, 5, 5, 360, 360, ArcType.OPEN);
            gc.strokeLine(input.getOutputX() + lineOffset, inputY, getInputX(inputY), inputY);
            lineOffset += offsetChange;
        }
        return lineOffset;
    }

    public abstract double getOutputX();
    protected abstract double getInputX(double inputY);
    public double getOutputY() {
        return centerY;
    };

    public static void setOffsetChange(double offset) {
        offsetChange = offset;
    }
}
