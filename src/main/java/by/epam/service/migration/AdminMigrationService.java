package by.epam.service.migration;

import by.epam.entity.Admin;
import by.epam.exception.AlreadyExistsException;
import by.epam.exception.DatabaseException;
import by.epam.service.DbManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdminMigrationService {

    private static final Logger logger = LogManager.getLogger();
    private static final AdminMigrationService instance = new AdminMigrationService();
    private AdminMigrationService(){}

    public static AdminMigrationService getInstance() {
        return instance;
    }

    public void migrate(List<Admin> admins) throws DatabaseException, AlreadyExistsException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DbManager dbManager = DbManager.getInstance();
        Connection connection = dbManager.getConnection();
        for (Admin admin : admins) {
            try {
                preparedStatement = connection.prepareStatement("SELECT * FROM admins WHERE name = ?;");
                preparedStatement.setString(1, admin.getName());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.first())
                    throw new AlreadyExistsException("Such admin already exists!");
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO admins (name, password_hash, authority_level) VALUES (?,?,?);");
                preparedStatement.setString(1, admin.getName());
                preparedStatement.setString(2, admin.getPasswordHash());
                preparedStatement.setInt(3, admin.getAuthorityLevel());
                preparedStatement.executeUpdate();
                logger.info("Admin was migrated to database.");
            } catch (SQLException e) {
                logger.error("SQLException: ", e);
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (resultSet != null)
                        resultSet.close();
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("SQLException while closing resultSet or preparedStatement.");
                }
            }
        }
    }

}
