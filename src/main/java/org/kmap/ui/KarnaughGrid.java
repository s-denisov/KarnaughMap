package org.kmap.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kmap.logic.GridState;
import org.kmap.logic.TestCell;
import org.kmap.logic.TrueCell;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class KarnaughGrid {

    @FXML private GridPane grid;
    @FXML private Spinner<Integer> variablesSpinner;
    @FXML private Label resultLabel;
    private GridState gridState;
    private KarnaughCell[] cells;

    public void initialize() {
        variablesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> generateGrid(newValue));
        variablesSpinner.getValueFactory().setValue(4);
        generateGrid(4);
    }

    public void generateGrid(int variables) {
        grid.getChildren().clear();
        gridState = new GridState(variables);
        double half = (double) variables / 2;
        int rowPower = (int) Math.floor(half);
        int columnPower = (int) Math.ceil(half);
        int totalRows = (int) Math.pow(2, rowPower);
        int totalColumns = (int) Math.pow(2, columnPower);
        StringBuilder columnVariables = new StringBuilder("");
        for (int column = rowPower; column < rowPower + columnPower; column++) {
            columnVariables.append((char) (65 + column));
        }
        grid.add(new Label(columnVariables.toString()), 1, 0);
        StringBuilder rowVariables = new StringBuilder("");
        for (int row = 0; row < rowPower; row++) {
            rowVariables.append((char) (65 + row));
        }
        grid.add(new Label(rowVariables.toString()), 0, 1);
        System.out.println(rowVariables);
        System.out.println(columnVariables);
        cells = new KarnaughCell[totalColumns * totalRows];
        for (int row = 0; row < totalRows; row++) {
            for (int column = 0; column < totalColumns; column++) {
                final int offset = 2;
                StackPane stack = new StackPane();
                KarnaughCell cell = new KarnaughCell(row, rowPower, column, columnPower, stack);
                cells[row * totalColumns + column] = cell;
                if (row == 0) grid.add(new Label(cell.getGrayColumn()), column + offset, offset - 1);
                if (column == 0) grid.add(new Label(cell.getGrayRow()), offset - 1, row + offset);
                grid.add(stack, column + offset, row + offset);
                Button cellButton = new Button();
                cellButton.setPrefHeight(50);
                cellButton.setPrefWidth(50);
                cellButton.setOnAction(e -> flipCell(e, cell.findCorrespondingTrueCell()));
                stack.getChildren().add(cellButton);
            }
        }
    }

    public void flipCell(ActionEvent e, TrueCell correspondingCell) {
        Button booleanButton = (Button) e.getSource();
        booleanButton.setText(booleanButton.getText().equals("") ? "1" : "");
        gridState.flip(correspondingCell);
    }

    public void submit() {
        for (KarnaughCell kcell : cells) {
            kcell.getStackPane().getChildren().removeIf(x -> x.getClass() != Button.class);
            kcell.resetPositions();
        }
        int i = 0;
        Set<Set<TrueCell>> loops = gridState.findLoops().keySet();
        Collection<TestCell> andStatements = gridState.findLoops().values();
        for (Set<TrueCell> loop : loops) {
            int hue = i * 256 / loops.size();
            i++;
            KarnaughCell[] kcells = new KarnaughCell[loop.size()];
            int j = 0;
            for (TrueCell cell : loop) {
                KarnaughCell kcell = Arrays.stream(cells).filter(c -> c.matchesBooleanCoOrds(cell.getValues())).findAny().orElseThrow();
                kcells[j] = kcell;
                j++;
            }
            Pos resultingPosition = null;
            positionsLoop: for (Pos pos : kcells[0].getAvailablePositions()) {
                for (KarnaughCell kcell : kcells) {
                    if (!kcell.getAvailablePositions().contains(pos)) continue positionsLoop;
                }
                resultingPosition = pos;
                break;
            }
            for (KarnaughCell kcell : kcells) {
                Label coloredSquare = new Label(Integer.toString(i));
                coloredSquare.setStyle("-fx-background-color: hsb(" + hue + ", 50%, 50%);");
                coloredSquare.setTextFill(Color.WHITE);
                kcell.getStackPane().getChildren().add(coloredSquare);
                StackPane.setAlignment(coloredSquare, resultingPosition);
                kcell.getAvailablePositions().remove(resultingPosition);
            }
            StringBuilder expression = new StringBuilder();
            for (TestCell cell : andStatements) {
                expression.append(cell).append(" + ");
            }
            expression.delete(expression.length() - 2, expression.length() - 1);
            resultLabel.setText(expression.toString());
        }
    }
}
