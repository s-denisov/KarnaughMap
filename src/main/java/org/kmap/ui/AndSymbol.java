package org.kmap.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class AndSymbol implements LogicGateSymbol {

    private final GraphicsContext gc;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private final double centerX;
    private final double centerY;

    public AndSymbol(GraphicsContext gc, double centerX, double centerY) { // 230,110
//        gc.strokeLine(210, 90, 210, 130);
//        gc.strokeLine(210, 90, 250, 90);
//        gc.strokeLine(210, 130, 250, 130);
//        gc.strokeArc(230, 90, 40, 40, 270, 180, ArcType.OPEN);
        this.gc = gc; this.centerX = centerX; this.centerY = centerY;
        gc.strokeLine(centerX - WIDTH, centerY - HEIGHT, centerX - WIDTH, centerY + HEIGHT);
        gc.strokeLine(centerX - WIDTH, centerY - HEIGHT, centerX + WIDTH, centerY - HEIGHT);
        gc.strokeLine(centerX - WIDTH, centerY + HEIGHT, centerX + WIDTH, centerY + HEIGHT);
        gc.strokeArc(centerX, centerY - HEIGHT, WIDTH * 2, HEIGHT * 2, 270, 180, ArcType.OPEN);
    }

    @Override
    public void setInputs(ArrayList<LogicGateSymbol> inputs, int lineOffset) {
        for (int i = 0; i < inputs.size(); i++) {
            LogicGateSymbol input = inputs.get(i);
            gc.strokeLine(input.getOutputX(), input.getOutputY(), input.getOutputX() + lineOffset, input.getOutputY());
            double correction = inputs.size() == 1 ? HEIGHT - 5 : (double) (2 * HEIGHT - 10) * i / (inputs.size() - 1);
            double inputY = centerY - HEIGHT + 5 + correction;
            gc.strokeLine(input.getOutputX() + lineOffset, input.getOutputY(), input.getOutputX() + lineOffset, inputY);
            gc.strokeLine(input.getOutputX() + lineOffset, inputY, centerX - WIDTH, inputY);
            lineOffset += 3;
        }
    }

    @Override
    public double getOutputX() {
        return centerX + WIDTH * 2;
    }

    @Override
    public double getOutputY() {
        return centerY;
    }
}
