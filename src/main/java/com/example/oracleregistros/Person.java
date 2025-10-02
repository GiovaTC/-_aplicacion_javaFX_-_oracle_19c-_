package com.example.oracleregistros;

import java.time.LocalDate;
public class Person {
    private int id;
    private String name;
    private LocalDate birthDate;

    public Person(int id, String name, LocalDate birthDate) {
        this.id=id;
        this.name=name;
        this.birthDate=birthDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
