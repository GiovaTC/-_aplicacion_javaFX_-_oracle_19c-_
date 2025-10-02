# -_aplicacion_javaFX_-_oracle_19c-_

<img width="1024" height="1024" alt="ChatGPT Image 2 oct 2025, 14_23_25" src="https://github.com/user-attachments/assets/5ac42c86-e998-45ed-8918-e8f0cb63d5f1" />         

📌 Aplicación JavaFX + Oracle 19c :

Proyecto en IntelliJ con Maven para gestionar registros en una base de datos Oracle mediante procedimientos almacenados .

🚀 Características
Interfaz gráfica con JavaFX.
Botón para insertar registros (usando un procedimiento almacenado).
Botón para mostrar registros en una tabla.
Conexión a Oracle 19c con JDBC.
Logo de ejemplo incluido .

## 📂 Estructura del Proyecto

- src/com/example/oracleregistros/MainApp.java → Clase principal JavaFX  
- src/com/example/oracleregistros/DBConnection.java → Conexión a Oracle  
- src/com/example/oracleregistros/Person.java → Modelo de datos  
- src/com/example/oracleregistros/PersonDAO.java → Acceso a BD (DAO)  
- resources/logo.png → Logo de la aplicación  

⚙️ Dependencias Maven (pom.xml) :
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>oracle-registros</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <javafx.version>20</javafx.version>
    </properties>

    <dependencies>
        <!-- JavaFX -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <!-- Oracle JDBC Driver -->
        <dependency>
            <groupId>com.oracle.database.jdbc</groupId>
            <artifactId>ojdbc8</artifactId>
            <version>21.5.0.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- JavaFX Plugin -->
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>${javafx.version}</version>
                <configuration>
                    <mainClass>com.example.oracleregistros.MainApp</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

## 🗄️ Script SQL para Oracle 19c (`setup_oracle.sql`)

```sql
-- Crear tabla
DROP TABLE persons CASCADE CONSTRAINTS;

CREATE TABLE persons (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    birth_date DATE NOT NULL
);

-- Secuencia
DROP SEQUENCE seq_persons;

CREATE SEQUENCE seq_persons
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- Trigger de autoincremento
CREATE OR REPLACE TRIGGER trg_persons_id
BEFORE INSERT ON persons
FOR EACH ROW
WHEN (new.id IS NULL)
BEGIN
    :new.id := seq_persons.NEXTVAL;
END;
/

-- Procedimiento almacenado para insertar
CREATE OR REPLACE PROCEDURE insert_person (
    p_name       IN VARCHAR2,
    p_birth_date IN DATE
) AS
BEGIN
    INSERT INTO persons (id, name, birth_date)
    VALUES (seq_persons.NEXTVAL, p_name, p_birth_date);
    COMMIT;
END;
/

-- Procedimiento almacenado para listar (opcional)
CREATE OR REPLACE PROCEDURE list_persons (
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
        SELECT id, name, birth_date FROM persons ORDER BY id;
END;
/

-- Insertar datos de ejemplo
BEGIN
    insert_person('Carlos Pérez', TO_DATE('1990-05-21','YYYY-MM-DD'));
    insert_person('María Gómez', TO_DATE('1985-10-15','YYYY-MM-DD'));
    insert_person('Andrés Torres', TO_DATE('2000-03-02','YYYY-MM-DD'));
END;
/

package com.example.oracleregistros;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Ajustar
    private static final String USER = "system"; // Cambiar
    private static final String PASS = "tu_password"; // Cambiar

    public static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

🔹 Person.java :
package com.example.oracleregistros;

import java.time.LocalDate;

public class Person {
    private int id;
    private String name;
    private LocalDate birthDate;

    public Person(int id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
}

🔹 PersonDAO.java :
package com.example.oracleregistros;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    public static void insertPerson(String name, LocalDate birthDate) {
        String sql = "{ call insert_person(?, ?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, name);
            stmt.setDate(2, Date.valueOf(birthDate));
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Person> getAllPersons() {
        List<Person> list = new ArrayList<>();
        String sql = "SELECT id, name, birth_date FROM persons";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birth_date").toLocalDate()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

🔹 MainApp.java :
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
        // Logo con nombre
        Label logo = new Label("Registros Base DB Oracle");
        logo.setStyle("-fx-font-size: 20px; -fx-text-fill: darkblue; -fx-font-weight: bold;");

        ImageView imgLogo = new ImageView(new Image("https://img.icons8.com/color/96/database.png"));
        imgLogo.setFitWidth(60);
        imgLogo.setFitHeight(60);

        HBox logoBox = new HBox(10, imgLogo, logo);
        logoBox.setAlignment(Pos.CENTER);

        // Campos de entrada
        TextField txtName = new TextField();
        txtName.setPromptText("Nombre");

        DatePicker dpBirth = new DatePicker();
        dpBirth.setPromptText("Fecha Nacimiento");

        Button btnInsert = new Button("Insertar Registro");
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

🖥️ Ejecucion :
Crear BD con setup_oracle.sql .
Ajustar usuario/contraseña en DBConnection.java .
Ejecutar en IntelliJ :
mvn javafx:run

📸 Interfaz :
Logo + título: "Registros Base DB Oracle" .
Formulario: Campo de nombre + fecha de nacimiento .
Botones: Insertar registro, Mostrar registros .
Tabla: Lista de registros en la BD .
