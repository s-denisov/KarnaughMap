package org.kmap.logic.logicelements;

import org.kmap.logic.TrueCell;

import java.util.*;
import java.util.stream.Collectors;

public class AndElement implements LogicElement {

    private ArrayList<LogicElement> inputs = new ArrayList<>();

    @Override
    public boolean calculate(TrueCell inputValues) {
        for (LogicElement element : inputs) {
            if (!element.calculate(inputValues)) return false;
        }
        return true;
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
        if (inputs.size() == 0) return "0";
        StringBuilder expression = new StringBuilder();
        for (LogicElement input : inputs) {
            if (input.getClass() == OrElement.class && inputs.size() > 1) {
                expression.append("(").append(input).append(")").append(".");
            } else {
                expression.append(input).append(".");
            }
        }
        expression.deleteCharAt(expression.length() - 1);
        return expression.toString();
    }

    private List<LogicElement> getSubInputs(LogicElement input) {
        if (input.getClass() == InputElement.class) {
            return List.of(input);
        }
        return input.getInputs();
    }

    private void absorb(SimplificationLogger logger) {
        Set<LogicElement> orInputs = inputs.stream()
                .filter(x -> x.getClass() == OrElement.class || x.getClass() == InputElement.class)
                .collect(Collectors.toSet());
        Set<LogicElement> removedInputs = new HashSet<>();
        for (LogicElement input1 : orInputs) {
            for (LogicElement input2 : orInputs) {
                if (input1 != input2 && !removedInputs.contains(input1) && !removedInputs.contains(input2)) {
                    if (getSubInputs(input1).containsAll(getSubInputs(input2))) {
                        String old = toString();
                        inputs.remove(input1);
                        removedInputs.add(input1);
                        logger.addStep("Absorption", old, toString());
                    }
                }
            }
        }
    }

    @Override
    public String simplify(SimplificationLogger logger, LogicElement parent) {
        /*
        Set<LogicElement> andInputs = inputs.stream().filter(x -> x.getClass() == AndElement.class)
                .collect(Collectors.toSet());
        for (LogicElement input : andInputs) {
            inputs.remove(input);
            inputs.addAll(input.getInputs());
            input.simplify(logger);
        }*/
        absorb(logger);
        Optional<LogicElement> orInputOptional = inputs.stream().filter(x -> x.getClass() == OrElement.class).findFirst();
        String old = toString();
        inputs.removeIf(input -> input.getClass() == TrueElement.class);
        if (!old.equals(toString())) logger.addStep("Dominance", old, toString());
        Set<LogicElement> notInputs = inputs.stream()
                .filter(x -> x.getClass() == NotElement.class || x.getClass() == InputElement.class)
                .collect(Collectors.toSet());
        for (LogicElement input1 : notInputs) {
            for (LogicElement input2 : notInputs) {
                if (NotElement.areComplementary(input1, input2)) {
                    parent.removeInput(this);
                    parent.addInput(new FalseElement());
                    logger.addStep("Complementary", old, "0");
                    return logger.toString();
                }
            }
        }
        if (orInputOptional.isPresent()) {
            LogicElement orInput = orInputOptional.orElseThrow();
            inputs.remove(orInput);
            OrElement distributiveOr = new OrElement();
            for (LogicElement subInput : orInput.getInputs()) {
                AndElement distributiveAnd = new AndElement();
                distributiveAnd.inputs = new ArrayList<>(getInputs());
                distributiveAnd.addInput(subInput);
                distributiveOr.addInput(distributiveAnd);
            }
            inputs.clear();
            inputs.add(distributiveOr);
            parent.removeInput(this);
            parent.addInput(distributiveOr);
            logger.addStep("Distribution", old, toString());
            return logger.toString();
        }
        String old2 = toString();
        inputs = new ArrayList<>(new LinkedHashSet<>(inputs));
        if (!old2.equals(toString())) logger.addStep("Idempotence", old2, toString());
        String old3 = toString();
        int i = 0;
        while (true) {
            String old4 = toString();
            inputs.get(i).simplify(logger, this);
            if (old4.equals(toString())) {
                if (i >= inputs.size() - 1) break;
                else i++;
            } else {
                i = 0;
            }
        }
        // if (!old3.equals(toString())) logger.addStep("So", old, toString());
        return logger.toString();
    }
}
