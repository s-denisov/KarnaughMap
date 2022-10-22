package org.kmap.logic.logicelements;

public class SimplificationLogger {

    private final StringBuilder log = new StringBuilder();
    private String prevOldValue = "";
    private String prevNewValue = "";

    public void addStep(String method, String oldValue, String newValue) {
        if (!prevOldValue.equals(oldValue) || !prevNewValue.equals(newValue)) {
            log.append(method).append(": ").append(oldValue).append(" == ").append(newValue).append("\n");
            prevOldValue = oldValue;
            prevNewValue = newValue;
        }
    }

    @Override
    public String toString() {
        return log.toString();
    }
}
