package org.converter;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.converter.DBCreateTable.createTableIfNotExists;

public class DBCreate {
    public static void createDB(String dbName) {
        File file = new File(dbName);
        if (!file.exists()) {
            String url = "jdbc:sqlite:ComputersDataBase.db";

            try {
                Connection conn = DriverManager.getConnection(url);
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("Stworzono nowa baze danych");
                    System.out.println("Nazwa sterownika: " + meta.getDriverName());

                    createTableIfNotExists();
                }
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
