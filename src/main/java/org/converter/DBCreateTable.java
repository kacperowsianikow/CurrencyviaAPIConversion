package org.converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBCreateTable {
    public static void createTableIfNotExists() {
        Connection conn = DBConnection.connect();
        PreparedStatement prepStmt = null;
        String sql = "CREATE TABLE IF NOT EXISTS \"computers\" (\n" +
                "\t\"nazwa\"\tTEXT,\n" +
                "\t\"data_ksiegowania\"\tTEXT,\n" +
                "\t\"koszt_USD\"\tREAL,\n" +
                "\t\"koszt_PLN\"\tREAL\n" +
                ");";

        try {
            prepStmt = conn.prepareStatement(sql);
            prepStmt.execute();
            System.out.println("Utworzono nowa tabele w bazie danych");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
