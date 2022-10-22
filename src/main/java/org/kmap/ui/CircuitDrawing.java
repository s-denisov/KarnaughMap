package org.kmap.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.kmap.logic.GridState;
import org.kmap.logic.TestCell;
import org.kmap.ui.inputcircuitsymbols.GateOrIO;
import org.kmap.ui.outputcircuitsymbols.*;

import java.util.*;

public class CircuitDrawing {

    @FXML
    private Canvas canvas;
    private GridState gridState;
    private int variables;
    private List<GateOrIO> inputCircuitSymbols;
    private String inputExpression;

    public void initialize() {
    }

    public void setGridState(GridState gridState) {
        this.gridState = gridState;
    }

    public void drawKarnaugh() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<IOSymbol> inputs = new ArrayList<>();
        List<NotSymbol> negatedInputs = new ArrayList<>();
        Collection<TestCell> tests = gridState.findLoops().values();
        for (int i = 0; i < variables; i++) {
            double inputCenterY = canvas.getHeight() / 4 * (i + 0.5);
            IOSymbol input = new IOSymbol(gc, canvas.getWidth() / 4 * 0.5, inputCenterY, (char) (i + 65));
            double multiplier = tests.size() <= 4 ? (i + 0.7) / 4 :
                    (double) (Math.round(tests.size() * inputCenterY / canvas.getHeight())) / tests.size();
            boolean negatedInputNeeded = false;
            for (TestCell test : tests) {
                if (test.getValues()[i] != null && !test.getValues()[i]) {
                    negatedInputNeeded = true;
                    break;
                }
            }
            if (negatedInputNeeded) {
                NotSymbol negatedInput = new NotSymbol(gc, canvas.getWidth() / 4 * 1.5, canvas.getHeight() * multiplier);
                ArrayList<LogicGateSymbol> singleInput = new ArrayList<>();
                singleInput.add(input);
                negatedInput.setInputs(singleInput, 5);
                negatedInputs.add(negatedInput);
            } else {
                negatedInputs.add(null);
            }
            inputs.add(input);
        }
        int i = 0;
        ArrayList<LogicGateSymbol> orGateInputs = new ArrayList<>();
        double offsetChange = 22.0 / tests.size();
        LogicGateSymbol.setOffsetChange(22.0 / tests.size());
        double offset = 5 + offsetChange;
        for (TestCell test : tests) {
            if (test.gapCount() == variables) {
                orGateInputs.add(new NotSymbol(gc, canvas.getWidth() / 4 * 2.5, canvas.getHeight() / 2));
                break;
            }
            Integer singleInputIndex = test.getSingleInputIndex();
            if (singleInputIndex != null) {
                Boolean value = test.getValues()[singleInputIndex];
                if (value != null) {
                    if (value) orGateInputs.add(inputs.get(singleInputIndex));
                    else orGateInputs.add(negatedInputs.get(singleInputIndex));
                }
                continue;
            }
            AndSymbol andGate = new AndSymbol(gc, canvas.getWidth() / 4 * 2.5,
                    canvas.getHeight() / tests.size() * (i + 0.5));
            ArrayList<LogicGateSymbol> andGateInputs = new ArrayList<>();
            i++;
            for (int j = 0; j < test.getValues().length; j++) {
                Boolean value = test.getValues()[j];
                if (value != null) {
                    if (value) andGateInputs.add(inputs.get(j));
                    else andGateInputs.add(negatedInputs.get(j));
                }
            }
            offset = andGate.setInputs(andGateInputs, offset);
            orGateInputs.add(andGate);
        }
        ArrayList<LogicGateSymbol> outputInputs = new ArrayList<>();
        if (orGateInputs.size() == 1) {
            outputInputs.add(orGateInputs.get(0));
        } else {
            OrSymbol orGate = new OrSymbol(gc, canvas.getWidth() / 4 * 3.5, canvas.getHeight() / 2);
            orGate.setInputs(orGateInputs, 5);
            outputInputs.add(orGate);
        }
        new IOSymbol(gc, canvas.getWidth() - 10, canvas.getHeight() / 2, 'Q').setInputs(outputInputs, 0);
    }

    public void openGrid() {
        KarnaughGrid grid = (KarnaughGrid) App.setRoot("karnaugh-grid");
        grid.generateGrid(variables);
        grid.setGridState(gridState);
        grid.setInputCircuitSymbols(inputCircuitSymbols);
        grid.setInputExpressionText(inputExpression);
    }

    public void setVariables(int variables) {
        this.variables = variables;
    }

    public void setInputCircuitSymbols(List<GateOrIO> inputCircuitSymbols) {
        this.inputCircuitSymbols = inputCircuitSymbols;
    }

    public void setInputExpression(String inputExpression) {
        this.inputExpression = inputExpression;
    }
}
