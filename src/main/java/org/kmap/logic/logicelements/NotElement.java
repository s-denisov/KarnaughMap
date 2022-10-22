package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.List;
import java.util.Objects;

public class NotElement implements LogicElement {

    private LogicElement input;

    @Override
    public boolean calculate(TrueCell inputValues) {
        return !input.calculate(inputValues);
    }

    public LogicElement getInput() {
        return input;
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
    public String toString() {
        if (input.getClass() == InputElement.class || input.getClass() == NotElement.class) {
            return "¬" + input.toString();
        } else {
            return "¬(" + input.toString() + ")";
        }
    }

    public static boolean areComplementary(LogicElement input1, LogicElement input2) {
        return input1.getClass() == NotElement.class && ((NotElement) input1).getInput().equals(input2) ||
                input2.getClass() == NotElement.class && ((NotElement) input2).getInput().equals(input1);
    }

    private void deMorgans(LogicElement flippedResult, SimplificationLogger logger, LogicElement parent) {
        String old = toString();
        for (LogicElement subInput : input.getInputs()) {
            NotElement not = new NotElement();
            not.input = subInput;
            flippedResult.addInput(not);
        }
        parent.removeInput(this);
        parent.addInput(flippedResult);
        logger.addStep("De Morgan's", old, flippedResult.toString());
    }

    @Override
    public String simplify(SimplificationLogger logger, LogicElement parent) {
        if (input.getClass() == NotElement.class) {
            String old = toString();
            parent.removeInput(this);
            LogicElement subInput = ((NotElement) input).getInput();
            parent.addInput(subInput);
            logger.addStep("Double negation", old, subInput.toString());
            return subInput.simplify(logger, parent);
        } else if (input.getClass() == AndElement.class) {
            deMorgans(new OrElement(), logger, parent);
        } else if (input.getClass() == OrElement.class) {
            deMorgans(new AndElement(), logger, parent);
        }
        return input.simplify(logger, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotElement that = (NotElement) o;
        return Objects.equals(input, that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input);
    }
}
