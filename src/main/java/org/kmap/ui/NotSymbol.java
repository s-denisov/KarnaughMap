package org.kmap.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class NotSymbol extends LogicGateSymbol {

    public NotSymbol(GraphicsContext gc, double centerX, double centerY) {
        super(gc, centerX, centerY);
        gc.strokePolygon(new double[]{centerX - 25, centerX - 25, centerX + 25}, new double[]{centerY - 20, centerY + 20, centerY}, 3);
        gc.strokeArc(centerX + 25, centerY - 5, 10, 10, 0, 360, ArcType.OPEN);
    }
/*
    @Override
    public void setInputs(ArrayList<LogicGateSymbol> inputs, int lineOffset) {
        for (int i = 0; i < inputs.size(); i++) {
            LogicGateSymbol input = inputs.get(i);
            gc.strokeLine(input.getOutputX(), input.getOutputY(), input.getOutputX() + lineOffset, input.getOutputY());
            double correction = inputs.size() == 1 ? 20 - 5 : (double) (2 * 20 - 10) * i / (inputs.size() - 1);
            double inputY = centerY - 20 + 5 + correction;
            gc.strokeLine(input.getOutputX() + lineOffset, input.getOutputY(), input.getOutputX() + lineOffset, inputY);
            gc.strokeLine(input.getOutputX() + lineOffset, inputY, centerX - 25, inputY);
            lineOffset += 3;
        }
    }*/

    @Override
    public double getOutputX() {
        return centerX + 35;
    }

    @Override
    protected double getInputX(double inputY) {
        return centerX - 25;
    }
}
