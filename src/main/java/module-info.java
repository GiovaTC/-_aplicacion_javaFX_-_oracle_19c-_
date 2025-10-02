module com.example.oracleregistros {
    requires java.sql;        // Necesario para Connection, DriverManager, ResultSet, etc.
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.oracleregistros to javafx.fxml;
    exports com.example.oracleregistros;
}