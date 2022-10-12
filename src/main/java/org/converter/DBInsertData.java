package org.converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBInsertData {
    public static void DBInsertData(String nazwa, String data_ksiegowania, double koszt_USD, double koszt_PLN) {
        Connection conn = DBConnection.connect();
        PreparedStatement prepStmt = null;
        String sql = "INSERT INTO computers(nazwa, data_ksiegowania, koszt_USD, koszt_pLN) VALUES(?, ?, ?, ?)";

        try {
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, nazwa);
            prepStmt.setString(2, data_ksiegowania);
            prepStmt.setDouble(3, koszt_USD);
            prepStmt.setDouble(4, koszt_PLN);
            prepStmt.execute();

            System.out.println("Pomyslnie wprowadzono dane");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

