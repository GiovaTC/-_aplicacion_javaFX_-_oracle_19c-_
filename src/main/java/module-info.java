module com.example.oracleregistros {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.oracleregistros to javafx.fxml;
    exports com.example.oracleregistros;
}