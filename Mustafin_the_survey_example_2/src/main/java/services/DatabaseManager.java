package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Принцип единственной ответственности и паттерн одиночка

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        //try {
            // Здесь можно использовать различные драйверы для подключения
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdb", "user", "password");
        //} catch (SQLException e) {
            //e.printStackTrace();
        //}
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
