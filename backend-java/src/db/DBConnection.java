package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver Loaded");

            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ai_interview",
                "root",
                "Archit@dbms"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
