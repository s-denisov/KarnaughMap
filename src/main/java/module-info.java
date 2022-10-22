module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.kmap.ui to javafx.fxml;
    exports org.kmap.ui;
    exports org.kmap.ui.outputcircuitsymbols;
    opens org.kmap.ui.outputcircuitsymbols to javafx.fxml;
}