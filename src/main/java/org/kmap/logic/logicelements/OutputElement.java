package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.List;

public class OutputElement implements LogicElement {

    private LogicElement input;

    public OutputElement(LogicElement input) {
        this.input = input;
    }

    @Override
    public boolean calculate(TrueCell inputValues) {
        return input.calculate(inputValues);
    }

    @Override
    public List<LogicElement> getInputs() {
        return List.of(input);
    }

    @Override
    public void addInput(LogicElement input) {
        this.input = input;
    }

    @Override
    public void removeInput(LogicElement input) {
        if (this.input == input) this.input = null;
    }

    @Override
    public String simplify(SimplificationLogger logger, LogicElement parent) {
        String old = toString();
        while (true) {
            String old2 = toString();
            input.simplify(logger, this);
            if (old2.equals(toString())) {
                break;
            } else {
                logger.addStep("So", old, toString());
            }
        }
        return logger.toString();
    }

    public String simplify() {
        return simplify(new SimplificationLogger(), null);
    }

    @Override
    public String toString() {
        return input.toString();
    }
}
