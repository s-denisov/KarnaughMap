package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.*;
import java.util.stream.Collectors;

public class OrElement implements LogicElement {

    private List<LogicElement> inputs = new ArrayList<>();

    @Override
    public boolean calculate(TrueCell inputValues) {
        for (LogicElement element : inputs) {
            if (element.calculate(inputValues)) return true;
        }
        return false;
    }

    @Override
    public List<LogicElement> getInputs() {
        return new ArrayList<>(inputs);
    }

    @Override
    public void addInput(LogicElement input) {
        if (input.getClass() == getClass()) {
            inputs.addAll(input.getInputs());
        } else {
            inputs.add(input);
        }
    }

    @Override
    public void removeInput(LogicElement input) {
        inputs.remove(input);
    }

    @Override
    public String toString() {
        StringBuilder expression = new StringBuilder();
        for (LogicElement input : inputs) {
            expression.append(input).append(" + ");
        }
        expression.delete(expression.length() - 3, expression.length());
        return expression.toString();
    }

    private List<LogicElement> getSubInputs(LogicElement input) {
        if (input.getClass() == InputElement.class) {
            return List.of(input);
        }
        return input.getInputs();
    }

    private void absorb(SimplificationLogger logger) {
        Set<LogicElement> andInputs = inputs.stream()
                .filter(x -> x.getClass() == AndElement.class || x.getClass() == InputElement.class || x.getClass() == NotElement.class)
                .collect(Collectors.toSet());
        Set<LogicElement> removedInputs = new HashSet<>();
        for (LogicElement input1 : andInputs) {
            for (LogicElement input2 : andInputs) {
                if (input1 != input2 && !removedInputs.contains(input1) && !removedInputs.contains(input2)) {
                    String old = toString();
                    if (getSubInputs(input1).containsAll(getSubInputs(input2))) {
                        inputs.remove(input1);
                        removedInputs.add(input1);
                        logger.addStep("Absorption", old, toString());
                    } else {
                        List<LogicElement> intersection1 = getSubInputs(input1).stream()
                                .filter(x -> !getSubInputs(input2).contains(x)).collect(Collectors.toList());
                        List<LogicElement> intersection2 = getSubInputs(input2).stream()
                                .filter(x -> !getSubInputs(input1).contains(x)).collect(Collectors.toList());
                        if (intersection1.size() == 1 && intersection2.size() == 1) {
                            if (NotElement.areComplementary(intersection1.get(0), intersection2.get(0))) {
                                input1.removeInput(intersection1.get(0));
                                input2.removeInput(intersection2.get(0));
                                logger.addStep("Complementary (using distribution)", old, toString());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String simplify(SimplificationLogger logger, LogicElement parent) {
        String old = toString();
        inputs.removeIf(input -> input.getClass() == FalseElement.class);
        if (!old.equals(toString())) logger.addStep("Identity", old, toString());
        Set<LogicElement> notInputs = inputs.stream()
                .filter(x -> x.getClass() == NotElement.class || x.getClass() == InputElement.class)
                .collect(Collectors.toSet());
        ArrayList<LogicElement> removedInputs = new ArrayList<>();
        for (LogicElement input1 : notInputs) {
            for (LogicElement input2 : notInputs) {
                if (input1 != input2 && !removedInputs.contains(input1) && !removedInputs.contains(input2)
                        && NotElement.areComplementary(input1, input2)) {
                    removedInputs.add(input1);
                    removedInputs.add(input2);
                }
            }
        }
        if (removedInputs.size() > 0) {
            inputs.clear();
            inputs.add(new TrueElement());
            parent.removeInput(this);
            parent.addInput(new TrueElement());
            logger.addStep("Complementary", old, toString());
            return logger.toString();
        }

        old = toString();
        inputs = new ArrayList<>(new LinkedHashSet<>(inputs));
        if (!old.equals(toString())) logger.addStep("Idempotence", old, toString());
        absorb(logger);
        int i = 0;
        while (true) {
            old = toString();
            inputs.get(i).simplify(logger, this);
            if (old.equals(toString())) {
                if (i >= inputs.size() - 1) break;
                else i++;
            } else {
                i = 0;
            }
        }
        return logger.toString();
    }
}
