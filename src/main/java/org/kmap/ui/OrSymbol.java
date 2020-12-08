package org.kmap.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class OrSymbol implements LogicGateSymbol {

    private final GraphicsContext gc;
    private final double centerX;
    private final double centerY;

    public OrSymbol(GraphicsContext gc, double centerX, double centerY) {
//        gc.strokeArc(190, 200, 30, 40, 300, 120, ArcType.OPEN);
//        gc.strokeArc(72, 200, 200, 40, -67, 134, ArcType.OPEN);
        this.gc = gc; this.centerX = centerX; this.centerY = centerY;
        gc.strokeArc(centerX - 40, centerY - 20, 30, 40, 300, 120, ArcType.OPEN);
        gc.strokeArc(centerX - 158, centerY - 20, 200, 40, -67, 134, ArcType.OPEN);
    }

    @Override
    public void setInputs(ArrayList<LogicGateSymbol> inputs, int lineOffset) {
        for (int i = 0; i < inputs.size(); i++) {
            LogicGateSymbol input = inputs.get(i);
            gc.strokeLine(input.getOutputX(), input.getOutputY(), input.getOutputX() + lineOffset, input.getOutputY());
            // Using equation of an ellipse's co-ordinates
            double correction = inputs.size() == 1 ? 20 - 5 : (double) (2 * 20 - 10) * i / (inputs.size() - 1);
            double inputY = centerY - 20 + 5 + correction;
            double inputX = 30 * Math.sqrt(1 - Math.pow(inputY - centerY /*+20*/, 2) / 1600) + centerX - 42;
            gc.strokeLine(input.getOutputX() + lineOffset, input.getOutputY(), input.getOutputX() + lineOffset, inputY);
            gc.strokeLine(input.getOutputX() + lineOffset, inputY, inputX, inputY);
            lineOffset += 3;
        }
    }

    @Override
    public double getOutputX() {
        return centerX + 43;
    }

    @Override
    public double getOutputY() {
        return centerY;
    }
}
