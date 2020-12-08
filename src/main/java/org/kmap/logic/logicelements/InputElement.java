package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.List;

public class InputElement implements LogicElement {

    private final int index;
    public InputElement(char name) {
        this.index = name - 65;
    }

    @Override
    public boolean calculate(TrueCell inputValues) {
        return inputValues.getValues()[index];
    }

    @Override
    public List<LogicElement> getInputs() {
        return List.of();
    }

    @Override
    public void addInput(LogicElement input) {}
}
