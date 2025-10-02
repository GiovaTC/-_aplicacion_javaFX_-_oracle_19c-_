package com.example.oracleregistros;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class PersonDAO {

    public static void insertPerson(String name, LocalDate birthDate) {
        String sql = "{ call insert_person(?, ?) }";
        try (Connection conn =  DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, name);
            stmt.setDate(2, Date.valueOf(birthDate));
            stmt.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
