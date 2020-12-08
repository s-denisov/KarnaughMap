package org.kmap.ui;

import java.util.ArrayList;

public interface LogicGateSymbol {
    void setInputs(ArrayList<LogicGateSymbol> inputs, int lineOffset);
    double getOutputX();
    double getOutputY();
}
