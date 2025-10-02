package com.example.oracleregistros;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
public class MainApp extends Application {

    private TableView<Person> tableView;
    private ObservableList<Person> data;

    @Override
    public void start(Stage primaryStage) {
        // logo con nombre
        Label logo = new Label("registros BASE BD oracle");
        logo.setStyle("-fx-font-size: 20px; -fx-text-fill: darkblue; -fx-font-weight: bold;");

        ImageView imgLogo = new ImageView(new Image("https://img.icons8.com/color/96/database.png"));
        imgLogo.setFitWidth(60);
        imgLogo.setFitHeight(60);

        HBox logoBox = new HBox(10, imgLogo, logo);
        logoBox.setAlignment(Pos.CENTER);

        // campos de entrada :.
        TextField txtName = new TextField();
        txtName.setPromptText("nombre .");

        DatePicker dpBirth = new DatePicker();
        dpBirth.setPromptText("fecha nacimiento .");

        Button btnInsert = new Button("insertar registro .");
        btnInsert.setOnAction(e -> {
            String name = txtName.getText();
            LocalDate birth = dpBirth.getValue();
            if (name != null && !name.isBlank() && birth != null) {
                PersonDAO.insertPerson(name, birth);
                refreshTable();
                txtName.clear();
                dpBirth.setValue(null);
            }
        });
        Button btnLoad = new Button("Mostrar Registros");
        btnLoad.setOnAction(e -> refreshTable());

        HBox formBox = new HBox(10, txtName, dpBirth, btnInsert, btnLoad);
        formBox.setAlignment(Pos.CENTER);

        // Tabla
        tableView = new TableView<>();
        TableColumn<Person, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());

        TableColumn<Person, String> colName = new TableColumn<>("Nombre");
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        TableColumn<Person, String> colDate = new TableColumn<>("Fecha Nacimiento");
        colDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBirthDate().toString()));

        tableView.getColumns().addAll(colId, colName, colDate);

        VBox root = new VBox(15, logoBox, formBox, tableView);
        root.setStyle("-fx-padding: 20;");
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Registros Base DB Oracle");
        primaryStage.show();

    }
    private void refreshTable() {
        data = FXCollections.observableArrayList(PersonDAO.getAllPersons());
        tableView.setItems(data);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
