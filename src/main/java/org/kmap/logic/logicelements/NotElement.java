package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.List;

public class NotElement implements LogicElement {

    private LogicElement input;

    @Override
    public boolean calculate(TrueCell inputValues) {
        return !input.calculate(inputValues);
    }

    @Override
    public List<LogicElement> getInputs() {
        return List.of(input);
    }

    @Override
    public void addInput(LogicElement input) {
        this.input = input;
    }
}
