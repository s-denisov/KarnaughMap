package org.kmap.logic;

import org.kmap.logic.logicelements.LogicElement;

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
        System.out.println(g.findLoops().values());

        GridState g2 = new GridState(3);
        g2.flip(new TrueCell(false, false, false));
        g2.flip(new TrueCell(false, true, false));
        g2.flip(new TrueCell(false, false, true));
        System.out.println(g2.findLoops().values());

        GridState g3 = new GridState(4);
        g3.flip(new TrueCell(false, false, false, false));
        g3.flip(new TrueCell(true, false, false, false));
        g3.flip(new TrueCell(true, true, false, false));
        g3.flip(new TrueCell(false, false, false, true));
        System.out.println(g3.findLoops().values());

        LogicElement l = LogicParser.parse("¬(A.¬B)+(¬A.B)");
        System.out.println(l.calculate(new TrueCell(false, false)));
        System.out.println(l.calculate(new TrueCell(false, true)));
        System.out.println(!l.calculate(new TrueCell(true, false)));
        System.out.println(l.calculate(new TrueCell(true, true)));
    }
}
