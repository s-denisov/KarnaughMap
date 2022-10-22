package org.kmap.logic;

import java.util.*;
import java.util.stream.Collectors;

public class GridState {

    private final Set<TrueCell> trueCells = new HashSet<>();
    private final int size;

    public GridState(int size) {
        this.size = size;
    }

    public void flip(TrueCell cell) {
        if (trueCells.contains(cell)) {
            trueCells.remove(cell);
        } else {
            trueCells.add(cell);
        }
    }

    private TestCell[] findTests() {
        int totalTests = (int) Math.pow(3, size);
        TestCell[] tests = new TestCell[totalTests];
        for (int i = 0; i < tests.length; i++) {
            tests[i] = new TestCell(size);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < totalTests; j++) {
                int split = (int) Math.pow(3, i);
                int rem = (j / split) % 3;
                Boolean value = rem == 2 ? null : rem == 1;
                tests[j].setValue(i, value);
            }
        }
        Arrays.sort(tests, Collections.reverseOrder(Comparator.comparingInt(TestCell::gapCount)));
        return tests;
    }

    public Map<Set<TrueCell>, TestCell> findLoops() {
        Map<Set<TrueCell>, TestCell> loops = new LinkedHashMap<>();
        ArrayList<TrueCell> checkCells = new ArrayList<>(trueCells);
        while (!checkCells.isEmpty()) {
            for (TestCell test : findTests()) {
                TrueCell currentCell = checkCells.get(0);
                if (test.matches(currentCell)) {
                    Set<TrueCell> loop = trueCells.stream().filter(test::matches).collect(Collectors.toSet());
                    int neededSize = (int) (16 * Math.pow(0.5, 4 - test.gapCount()));
                    if (loop.size() == neededSize) {
                        loops.put(loop, test);
                        for (TrueCell cell : loop) {
                            checkCells.remove(cell);
                        }
                        break;
                    }
                }
            }
        }
        loops.keySet().removeIf(loop -> {
            boolean allInOtherLoops = true;
            for (TrueCell cell : loop) {
                boolean inOtherLoops = false;
                for (Set<TrueCell> loop2 : loops.keySet()) {
                    if (!loop.equals(loop2) && loop2.contains(cell)) {
                        inOtherLoops = true;
                        break;
                    }
                }
                if (!inOtherLoops) {
                    allInOtherLoops = false;
                    break;
                }
            }
            return allInOtherLoops;
        });
        return loops;
    }

    public Set<TrueCell> getTrueCells() {
        return trueCells;
    }
}
