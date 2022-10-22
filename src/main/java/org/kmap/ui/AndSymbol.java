package org.kmap.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class AndSymbol extends LogicGateSymbol {

    private final int WIDTH = 20;

    public AndSymbol(GraphicsContext gc, double centerX, double centerY) { // 230,110
//        gc.strokeLine(210, 90, 210, 130);
//        gc.strokeLine(210, 90, 250, 90);
//        gc.strokeLine(210, 130, 250, 130);
//        gc.strokeArc(230, 90, 40, 40, 270, 180, ArcType.OPEN);
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
