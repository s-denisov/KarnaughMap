package org.kmap.logic;

public class TestCell {

    private final Boolean[] values;
    public TestCell(int size) {
        this.values = new Boolean[size];
    }

    public void setValue(int i, Boolean value) {
        values[i] = value;
    }

    public boolean matches(TrueCell cell) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && values[i] != cell.getValues()[i]) {
                return false;
            }
        }
        return true;
    }

    public int gapCount() {
        int count = 0;
        for (Boolean x : values) {
            if (x == null) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            char equivalentVariable = (char) (65 + i);
            result.append(values[i] == null ? "" : values[i] ? equivalentVariable : "¬" + equivalentVariable);
        }
        return result.toString();
    }
}
