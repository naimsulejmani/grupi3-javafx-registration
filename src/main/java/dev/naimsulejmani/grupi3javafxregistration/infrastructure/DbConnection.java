package dev.naimsulejmani.grupi3javafxregistration.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static Connection connection;

    @Getter
    @Setter
    private static String connectionString = "jdbc:sqlserver://127.0.0.1:1433;encrypt=false;databaseName=Shitjet;username=sa;password=C@cttusEducation";

    private DbConnection() {
        // smundet kush me kriju objekt prej saj nga jasht!
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(connectionString);
        }
        return connection;
    }



}
