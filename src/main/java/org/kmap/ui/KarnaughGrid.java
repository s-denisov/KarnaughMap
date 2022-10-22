package org.kmap.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kmap.logic.GridState;
import org.kmap.logic.LogicParser;
import org.kmap.logic.TestCell;
import org.kmap.logic.TrueCell;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.logic.logicelements.OutputElement;
import org.kmap.ui.inputcircuitsymbols.GateOrIO;

import java.util.*;

public class KarnaughGrid {

    @FXML
    private GridPane grid;
    @FXML
    private Spinner<Integer> variablesSpinner;
    @FXML
    private Label resultLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField inputExpression;
    @FXML
    private Label algebraicSimplification;
    private GridState gridState;
    private KarnaughCell[] cells;
    private int variables;
    private List<GateOrIO> inputCircuitSymbols;

    public void initialize() {
        variablesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> generateGrid(newValue));
        generateGrid(4);
    }

    private Label createLabelFittingText(String text) {
        Label label = new Label(text);
        label.textOverrunProperty().setValue(OverrunStyle.CLIP);
        label.setMinWidth(20);
        return label;
    }

    public void generateGrid(int variables) {
        this.variables = variables;
        variablesSpinner.getValueFactory().setValue(variables);
        grid.getChildren().clear();
        gridState = new GridState(variables);
        double half = (double) variables / 2;
        int rowPower = (int) Math.floor(half);
        int columnPower = (int) Math.ceil(half);
        int totalRows = (int) Math.pow(2, rowPower);
        int totalColumns = (int) Math.pow(2, columnPower);
        StringBuilder columnVariables = new StringBuilder();
        for (int column = rowPower; column < rowPower + columnPower; column++) {
            columnVariables.append((char) (65 + column));
        }
        grid.add(createLabelFittingText(columnVariables.toString()), 1, 0);
        StringBuilder rowVariables = new StringBuilder();
        for (int row = 0; row < rowPower; row++) {
            rowVariables.append((char) (65 + row));
        }
        grid.add(createLabelFittingText(rowVariables.toString()), 0, 1);
        cells = new KarnaughCell[totalColumns * totalRows];
        for (int row = 0; row < totalRows; row++) {
            for (int column = 0; column < totalColumns; column++) {
                final int offset = 2;
                StackPane stack = new StackPane();
                KarnaughCell cell = new KarnaughCell(row, rowPower, column, columnPower, stack);
                cells[row * totalColumns + column] = cell;
                if (row == 0) {
                    grid.add(createLabelFittingText(cell.getGrayColumn()), column + offset, offset - 1);
                }
                if (column == 0) {
                    grid.add(createLabelFittingText(cell.getGrayRow()), offset - 1, row + offset);
                }
                grid.add(stack, column + offset, row + offset);
                Button cellButton = new Button();
                cellButton.setMinHeight(50);
                cellButton.setMinWidth(50);
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
        if (loops.size() == 0) {
            resultLabel.setText("0");
        }
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
            positionsLoop:
            for (Pos pos : kcells[0].getAvailablePositions()) {
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
            int k = 0;
            for (TestCell cell : andStatements) {
                k++;
                expression.append(cell).append(" + ");
                if (k % 5 == 0) expression.append("\n");
            }
            expression.delete(expression.lastIndexOf("+"), expression.length());
            resultLabel.setText(expression.toString());
        }
    }

    public void drawGridFromExpression() {
        String expression = inputExpression.getText();
        LogicElement result;
        try {
            result = LogicParser.parse(expression);
            errorLabel.setText("");
        } catch (LogicParser.InvalidInputException e) {
            errorLabel.setText(e.getMessage());
            return;
        }
        int variablesNeeded = expression.contains("D") ? 4 : expression.contains("C") ? 3 : 2;
        drawGridFromLogic(result, variablesNeeded);
    }

    public void drawGridFromLogic(LogicElement logic, int variables) {
        generateGrid(variables);
        for (KarnaughCell kcell : cells) {
            TrueCell trueCell = new TrueCell(kcell.getBooleanCoOrds());
            if (logic.calculate(trueCell)) {
                gridState.flip(trueCell);
                for (Node child : kcell.getStackPane().getChildren()) {
                    if (child.getClass() == Button.class) ((Button) child).setText("1");
                }
            }
        }
        submit();
        algebraicSimplification.setText(new OutputElement(logic).simplify());
    }

    public void outputCircuit() {
        CircuitDrawing drawing = (CircuitDrawing) App.setRoot("circuit-drawing");
        drawing.setGridState(gridState);
        drawing.setVariables(variables);
        drawing.setInputCircuitSymbols(inputCircuitSymbols);
        drawing.setInputExpression(inputExpression.getText());
        drawing.drawKarnaugh();
    }

    public void setGridState(GridState gridState) {
        this.gridState = gridState;
        for (KarnaughCell kcell : cells) {
            for (TrueCell trueCell : gridState.getTrueCells()) {
                if (kcell.matchesBooleanCoOrds(trueCell.getValues())) {
                    for (Node child : kcell.getStackPane().getChildren()) {
                        if (child.getClass() == Button.class) ((Button) child).setText("1");
                    }
                }
            }
        }
    }

    public void inputCircuit() {
        InputCircuit inputCircuit = (InputCircuit) App.setRoot("input-circuit");
        if (inputCircuitSymbols != null) inputCircuit.setSymbols(inputCircuitSymbols);
    }

    public void setInputCircuitSymbols(List<GateOrIO> inputCircuitSymbols) {
        this.inputCircuitSymbols = inputCircuitSymbols;
    }

    public void setInputExpressionText(String text) {
        inputExpression.setText(text);
    }
}
