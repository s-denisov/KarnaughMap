package org.kmap.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class CircuitDrawing {
    @FXML private Canvas canvas;
    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.strokeLine(110,100, 210, 100);
//        gc.strokeLine(110, 120, 210, 120);
        AndSymbol andA = new AndSymbol(gc, 230, 110);
        AndSymbol andB = new AndSymbol(gc, 350, 150);
        ArrayList<LogicGateSymbol> andSymbols = new ArrayList<>();
        andSymbols.add(andA);         andSymbols.add(andA);

        andSymbols.add(andA);
        andSymbols.add(andA); andSymbols.add(new OrSymbol(gc, 230, 220));
        //andB.setInputs(andSymbols, 5);
        andSymbols.add(new NotSymbol(gc, 100, 300));
        andSymbols.add(new IOSymbol(gc, 20, 10, "A"));
        andSymbols.add(new IOSymbol(gc, 20, 30, "B"));
        new OrSymbol(gc, 400, 200).setInputs(andSymbols, 5);
        new NotSymbol(gc,  400, 310).setInputs(andSymbols, 15);
        new IOSymbol(gc,  580, 200, "Q").setInputs(new ArrayList<>(andSymbols.subList(0, 1)), 15);
    }
}
