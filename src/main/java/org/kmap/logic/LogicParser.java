package org.kmap.logic;

import org.kmap.logic.logicelements.*;

import java.util.ArrayList;

public class LogicParser {

    private static class CharOrLogic {
        public final Character charValue;
        public final LogicElement logicValue;
        CharOrLogic(char value) {
            charValue = value; logicValue = null;
        }
        CharOrLogic(LogicElement value) {
            logicValue = value; charValue = null;
        }
        boolean charEquals(char char2) {
            return charValue != null && charValue == char2;
        }
    }


    private static int findIndex(ArrayList<CharOrLogic> chars, char toFind) {
        for (int i = 0; i < chars.size(); i++) {
            if (chars.get(i).charEquals(toFind)) return i;
        }
        return -1;
    }

    private static LogicElement parse(ArrayList<CharOrLogic> input) {
        for (int i = 0; i < input.size(); i++) {
            Character inChar = input.get(i).charValue;
            if (inChar != null && 'A' <= inChar && inChar <= 'D') {
                input.set(i, new CharOrLogic(new InputElement(inChar)));
            }
        }
        int openBracket;
        while ((openBracket = findIndex(input, '(')) > -1) {
            int depth = 1;
            int index = openBracket + 1;
            while (depth > 0) {
                if (input.get(index).charEquals('(')) {
                    depth++;
                } else if (input.get(index).charEquals(')')) {
                    depth--;
                }
                index++;
            }
            LogicElement result = parse(new ArrayList<>(input.subList(openBracket + 1, index - 1)));
            input.subList(openBracket, index).clear();
            input.add(openBracket, new CharOrLogic(result));
        }
        int notIndex;
        while ((notIndex = findIndex(input, '¬')) > -1) {
            NotElement notValue = new NotElement();
            notValue.addInput(input.get(notIndex + 1).logicValue);
            input.remove(notIndex + 1);
            input.set(notIndex, new CharOrLogic(notValue));
        }
        int andIndex;
        while ((andIndex = findIndex(input, '.')) > -1) {
            AndElement andValue = new AndElement();
            andValue.addInput(input.get(andIndex - 1).logicValue);
            andValue.addInput(input.get(andIndex + 1).logicValue);
            input.subList(andIndex - 1, andIndex + 2).clear();
            input.add(andIndex - 1, new CharOrLogic(andValue));
        }
        int orIndex;
        while ((orIndex = findIndex(input, '+')) > -1) {
            OrElement orValue = new OrElement();
            CharOrLogic e = input.get(orIndex - 1);
            orValue.addInput(input.get(orIndex - 1).logicValue);
            orValue.addInput(input.get(orIndex + 1).logicValue);
            input.subList(orIndex - 1, orIndex + 2).clear();
            input.add(orIndex - 1, new CharOrLogic(orValue));
        }
        return input.get(0).logicValue;
    }

    public static LogicElement parse(String input) {
        ArrayList<CharOrLogic> chars = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            chars.add(new CharOrLogic(input.charAt(i)));
        }
        return parse(chars);
    }
}
