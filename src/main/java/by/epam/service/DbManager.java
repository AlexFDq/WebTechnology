package by.epam.service;

import by.epam.exception.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {

    private static final Logger logger = LogManager.getLogger();
    private static final DbManager dbManager = new DbManager();
    private Connection connection;
    private boolean isReady = false;

    private DbManager() { }

    public static DbManager getInstance() {
        return dbManager;
    }

    public Connection getConnection() throws DatabaseException {
        if (isReady)
            return connection;
        else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/payment_system?serverTimezone=UTC";
                String user = "root";
                String password = "some_password";
                connection = DriverManager.getConnection(url, user, password);
                isReady = true;
                logger.info("Connection to database was established successfully.");
                return connection;
            } catch (ClassNotFoundException | SQLException e) {
                isReady = false;
                throw new DatabaseException(e.getMessage());
            }
        }
    }

}
