package org.converter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:ComputersDataBase.db");
            System.out.println("Polaczono z baza danych");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
