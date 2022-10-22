package org.kmap.logic;

import java.util.Arrays;

public class TrueCell {

    private final boolean[] values;

    public TrueCell(boolean... values) {
        this.values = values;
    }

    public boolean[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrueCell trueCell = (TrueCell) o;
        return Arrays.equals(values, trueCell.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
