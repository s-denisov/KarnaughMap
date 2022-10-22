package org.kmap.ui.inputcircuitsymbols;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class GateOutput {

    public static class ActiveChecker {
        private boolean anyActive = false;
        public boolean noneActive() {
            return !anyActive;
        }
    }

    private boolean active = false;
    private boolean horizontal = true;
    private final List<Node> parentRepresentation;
    private final List<Line> lines = new ArrayList<>();
    private final Circle outputDot;
    private Pane root;
    private final ActiveChecker activeChecker;
    private final EventHandler<MouseEvent> drawLineRef = this::drawLine;
    private final EventHandler<MouseEvent> splitLineRef = this::splitLine;

    public static Circle createIOCircle(double x, double y) {
        Circle io = new Circle(x, y, 3, Color.BLACK);
        io.setOnMouseEntered(e -> io.setFill(Color.RED));
        io.setOnMouseExited(e -> io.setFill(Color.BLACK));
        return io;
    }

    public GateOutput(Pane _root, List<Node> _parentRepresentation, double x, double y, ActiveChecker _activeChecker) {
        root = _root;
        parentRepresentation = _parentRepresentation;
        activeChecker = _activeChecker;
        outputDot = new Circle(x, y, 3, Color.BLACK);
        parentRepresentation.add(outputDot);
        root.getChildren().add(outputDot);
        outputDot.setOnMouseEntered(e -> outputDot.setFill(Color.RED));
        outputDot.setOnMouseExited(e -> outputDot.setFill(Color.BLACK));
        outputDot.setOnMousePressed(e -> {
            active = true;
            activeChecker.anyActive = true;
            Line line = new Line(x, y, x, y);
            lines.add(line);
            parentRepresentation.add(line);
            root.getChildren().add(line);
            root.addEventHandler(MouseEvent.MOUSE_MOVED, drawLineRef);
            root.addEventHandler(MouseEvent.MOUSE_CLICKED, splitLineRef);
        });
    }

    private Line getLast() {
        return lines.get(lines.size() - 1);
    }

    private void drawLine(MouseEvent e) {
        double xWithOffset = e.getX() - 10;
        if (lines.size() > 1) {
            Line beforePrevious = lines.get(lines.size() - 2);
            if (horizontal) {
                beforePrevious.setEndY(e.getY());
                getLast().setStartY(e.getY());
                getLast().setEndY(e.getY());
            } else {
                beforePrevious.setEndX(xWithOffset);
                getLast().setStartX(xWithOffset);
                getLast().setEndX(xWithOffset);
            }
        }
        if (lines.size() > 0) {
            if (horizontal) getLast().setEndX(xWithOffset);
            else getLast().setEndY(e.getY());
        }
    }

    private void splitLine(MouseEvent e) {
        if (lines.size() > 0) {
            horizontal = !horizontal;
            double lineX = getLast().getEndX();
            double lineY = getLast().getEndY();
            Line line = horizontal ? new Line(lineX, lineY, lineX + 20, lineY)
                    : new Line(lineX, lineY, lineX, lineY + 20);
            lines.add(line);
            parentRepresentation.add(line);
            root.getChildren().add(line);
        }
    }

    public boolean finishLine() {
        if (lines.size() > 0 && active) {
            active = false;
            activeChecker.anyActive = false;
            root.removeEventHandler(MouseEvent.MOUSE_MOVED, drawLineRef);
            root.removeEventHandler(MouseEvent.MOUSE_CLICKED, splitLineRef);
            return true;
        }
        return false;
    }

    public void update(Pane root) {
        this.root = root;
    }

    public void reset() {
        root.getChildren().removeAll(lines);
        parentRepresentation.removeAll(lines);
        lines.clear();
    }
}
