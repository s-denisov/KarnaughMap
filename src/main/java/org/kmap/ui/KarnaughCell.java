package org.kmap.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import org.kmap.logic.TrueCell;

import java.util.ArrayList;
import java.util.Arrays;

public class KarnaughCell {

    private final String grayRow;
    private final String grayColumn;
    private final boolean[] booleanCoOrds;
    private final ArrayList<Pos> availablePositions = new ArrayList<>();
    private final StackPane stackPane;

    private static String toGrayCode(int num, int length) {
        String unformatted = Integer.toBinaryString(num ^ (num >> 1));
        return String.format("%" + length + "s", unformatted).replace(" ", "0");
    }

    public KarnaughCell(int row, int rowPower, int column, int columnPower, StackPane stackPane) {
        this.grayRow = toGrayCode(row, rowPower);
        this.grayColumn = toGrayCode(column, columnPower);
        this.stackPane = stackPane;
        int totalLength = grayRow.length() + grayColumn.length();
        boolean[] booleanCoOrds = new boolean[totalLength];
        for (int i = 0; i < grayRow.length(); i++) {
            booleanCoOrds[i] = grayRow.charAt(i) == '1';
        }
        for (int i = 0; i < grayColumn.length(); i++) {
            booleanCoOrds[i + grayRow.length()] = grayColumn.charAt(i) == '1';
        }
        this.booleanCoOrds = booleanCoOrds;
        resetPositions();
    }

    public void resetPositions() {
        availablePositions.clear();
        availablePositions.add(Pos.TOP_LEFT);
        availablePositions.add(Pos.TOP_RIGHT);
        availablePositions.add(Pos.BOTTOM_LEFT);
        availablePositions.add(Pos.BOTTOM_RIGHT);
        availablePositions.add(Pos.TOP_CENTER);
        availablePositions.add(Pos.BOTTOM_CENTER);
    }

    public ArrayList<Pos> getAvailablePositions() {
        return availablePositions;
    }

    public TrueCell findCorrespondingTrueCell() {
        return new TrueCell(booleanCoOrds);
    }

    public boolean matchesBooleanCoOrds(boolean[] coOrds) {
        return Arrays.equals(coOrds, booleanCoOrds);
    }

    public boolean[] getBooleanCoOrds() {
        return booleanCoOrds;
    }

    public String getGrayRow() {
        return grayRow;
    }

    public String getGrayColumn() {
        return grayColumn;
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
