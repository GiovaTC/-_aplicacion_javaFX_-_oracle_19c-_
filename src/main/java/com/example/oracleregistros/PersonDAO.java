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
