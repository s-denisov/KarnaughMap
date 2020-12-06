package org.kmap.logic;

import java.util.Set;

public class Main {

    private static void printLoops(GridState g) {
        for (Set<TrueCell> loop : g.findLoops().keySet()) {
            System.out.println(loop);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        GridState g = new GridState(4);
        g.flip(new TrueCell(false, false, false, false));
        g.flip(new TrueCell(true, false, false, false));
        g.flip(new TrueCell(false, false, true, false));
        g.flip(new TrueCell(true, false, true, false));
        g.flip(new TrueCell(false, false, false, true));
        printLoops(g);

        GridState g2 = new GridState(3);
        g2.flip(new TrueCell(false, false, false));
        g2.flip(new TrueCell(false, true, false));
        g2.flip(new TrueCell(false, false, true));
        printLoops(g2);

        GridState g3 = new GridState(4);
        g3.flip(new TrueCell(false, false, false, false));
        g3.flip(new TrueCell(true, false, false, false));
        g3.flip(new TrueCell(true, true, false, false));
        g3.flip(new TrueCell(false, false, false, true));
        printLoops(g3);

    }
}
