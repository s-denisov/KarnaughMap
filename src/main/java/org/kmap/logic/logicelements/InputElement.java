package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.List;
import java.util.Objects;

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
    public void addInput(LogicElement input) {
    }

    @Override
    public void removeInput(LogicElement input) {
    }

    @Override
    public String toString() {
        return Character.toString(index + 65);
    }

    @Override
    public String simplify(SimplificationLogger logger, LogicElement parent) {
        return logger.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputElement that = (InputElement) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
