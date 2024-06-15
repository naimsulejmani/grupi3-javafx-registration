package dev.naimsulejmani.grupi3javafxregistration.repositories;

import dev.naimsulejmani.grupi3javafxregistration.infrastructure.DbConnection;
import dev.naimsulejmani.grupi3javafxregistration.infrastructure.Repository;
import dev.naimsulejmani.grupi3javafxregistration.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository implements Repository<Person, Integer> {
    @Override
    public boolean add(Person model) {
        PreparedStatement statement = null;
        String query = """
                INSERT INTO Persons(Name, Surname, Birthdate, Passive)
                VALUES (?,?,?,?)
""";
        try {
            Connection connection = DbConnection.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, model.getName());
            statement.setString(2, model.getSurname());
            statement.setDate(3, Date.valueOf(model.getBirthdate()));
            statement.setBoolean(4, model.isPassive());

            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modify(Integer id, Person model) {
        PreparedStatement statement = null;
        String query = """
                UPDATE Persons
                SET Name = ?, Surname = ?, Birthdate = ?, Passive = ?
                WHERE Id = ?
""";
        try {
            Connection connection = DbConnection.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, model.getName());
            statement.setString(2, model.getSurname());
            statement.setDate(3, Date.valueOf(model.getBirthdate()));
            statement.setBoolean(4, model.isPassive());
            statement.setInt(5, id);

            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeById(Integer id) {
        PreparedStatement statement = null;
        String query = "DELETE FROM Persons WHERE Id = ?";
        try {
            Connection connection = DbConnection.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Person findById(Integer id) {
        PreparedStatement statement = null;
        String query = "SELECT * FROM Persons WHERE Id = ?";
        Person person = null;
        try {
            Connection connection = DbConnection.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                person = toObject(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return person;
    }

    @Override
    public List<Person> findAll() {

        PreparedStatement statement = null;
        String query = "SELECT * FROM Persons";
        List<Person> persons = null;
        try {
            Connection connection = DbConnection.getConnection();
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            persons = new ArrayList<>();
            while (resultSet.next()) {
                Person person = toObject(resultSet);
                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


        return persons;
    }

    private Person toObject(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("Id"));
        person.setName(resultSet.getString("Name"));
        person.setSurname(resultSet.getString("Surname"));

        //validimi i kolonave nullable
        if (resultSet.getObject("Birthdate") != null)
            person.setBirthdate(resultSet.getDate("Birthdate").toLocalDate());

        person.setPassive(resultSet.getBoolean("Passive"));
        return person;
    }
}
