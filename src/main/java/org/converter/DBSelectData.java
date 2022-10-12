package org.converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSelectData {
    public void DBSelectAllData() {
        Connection conn = DBConnection.connect();
        PreparedStatement prepStmt = null;
        ResultSet resSet = null;
        String sqlAll = "SELECT * FROM computers";

        try {

            prepStmt = conn.prepareStatement(sqlAll);
            resSet = prepStmt.executeQuery();

            while (resSet.next()) {
                System.out.println(resSet.getString("nazwa") + "\t" +
                        resSet.getString("data_ksiegowania") + "\t" +
                        resSet.getDouble("koszt_USD") + "\t" +
                        resSet.getDouble("koszt_PLN"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                prepStmt.close();
                resSet.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
