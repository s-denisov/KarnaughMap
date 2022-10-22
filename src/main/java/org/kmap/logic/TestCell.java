package org.kmap.logic;

public class TestCell {

    private final Boolean[] values;

    public TestCell(int size) {
        this.values = new Boolean[size];
    }

    public void setValue(int i, Boolean value) {
        values[i] = value;
    }

    public Boolean[] getValues() {
        return values;
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

    public Integer getSingleInputIndex() {
        Integer result = null;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                if (result != null) return null;
                result = i;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            char equivalentVariable = (char) (65 + i);
            result.append(values[i] == null ? "" : values[i] ? equivalentVariable : "¬" + equivalentVariable);
            if (values[i] != null) result.append(".");
        }
        if (result.toString().equals("")) {
            return "1";
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
