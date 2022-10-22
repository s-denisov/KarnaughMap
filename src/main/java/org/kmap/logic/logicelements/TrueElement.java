package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.ArrayList;
import java.util.List;

public class TrueElement implements LogicElement {

    @Override
    public boolean calculate(TrueCell inputValues) {
        return true;
    }

    @Override
    public List<LogicElement> getInputs() {
        return new ArrayList<>();
    }

    @Override
    public void addInput(LogicElement input) {
    }

    @Override
    public void removeInput(LogicElement input) {
    }

    @Override
    public String simplify(SimplificationLogger logger, LogicElement parent) {
        return logger.toString();
    }

    @Override
    public String toString() {
        return "1";
    }
}
