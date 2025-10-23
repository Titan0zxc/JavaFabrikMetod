module com.example.fabrichmetod {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fabrichmetod to javafx.fxml;
    exports com.example.fabrichmetod;
}