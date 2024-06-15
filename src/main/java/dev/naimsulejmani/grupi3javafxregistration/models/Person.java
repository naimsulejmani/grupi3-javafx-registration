package dev.naimsulejmani.grupi3javafxregistration.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private int id;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private boolean passive;


    @Override
    public String toString() {
        return String.format("%d %s %s %s [%s]", id, name, surname, birthdate, passive ? "X" : "Y");
    }
}
