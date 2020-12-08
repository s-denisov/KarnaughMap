package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.List;

public interface LogicElement {
    boolean calculate(TrueCell inputValues);
    List<LogicElement> getInputs();
    void addInput(LogicElement input);
}
