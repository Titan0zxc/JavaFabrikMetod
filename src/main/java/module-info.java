module com.example.fabrichmetod {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    opens com.example.fabrichmetod to javafx.fxml;
    exports com.example.fabrichmetod;
}