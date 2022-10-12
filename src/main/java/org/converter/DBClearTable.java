package org.converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBClearTable {
    public static void DBClearData() {
        Connection conn = DBConnection.connect();
        PreparedStatement prepStmt = null;
        String sql = "DELETE FROM computers";

        try {
            prepStmt = conn.prepareStatement(sql);
            prepStmt.execute();

            System.out.println("Dane zostaly usuniete");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
