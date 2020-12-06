module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.kmap.ui to javafx.fxml;
    exports org.kmap.ui;
}