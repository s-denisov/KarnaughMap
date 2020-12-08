package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndElement implements LogicElement {

    private final ArrayList<LogicElement> inputs = new ArrayList<>();

    @Override
    public boolean calculate(TrueCell inputValues) {
        for (LogicElement element : inputs) {
            if (!element.calculate(inputValues)) return false;
        }
        return true;
    }

    @Override
    public List<LogicElement> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    @Override
    public void addInput(LogicElement input) {
        inputs.add(input);
    }
}
