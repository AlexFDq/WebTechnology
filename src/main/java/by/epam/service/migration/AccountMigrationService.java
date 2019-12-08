package by.epam.service.migration;

import by.epam.entity.Account;
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

public class AccountMigrationService {

    private static final Logger logger = LogManager.getLogger();
    private static final AccountMigrationService instance = new AccountMigrationService();

    private AccountMigrationService() {
    }

    public static AccountMigrationService getInstance() {
        return instance;
    }

    public void migrate(List<Account> accounts) throws DatabaseException, AlreadyExistsException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DbManager dbManager = DbManager.getInstance();
        Connection connection = dbManager.getConnection();
        for (Account account : accounts) {
            try {
                preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE id = ?;");
                preparedStatement.setInt(1, account.getId());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.first())
                    throw new AlreadyExistsException("Such account already exists!");
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO accounts (id, number, sum, blocked) VALUES (?,?,?,?);");
                preparedStatement.setInt(1, account.getId());
                preparedStatement.setString(2, account.getAccountNumber());
                preparedStatement.setInt(3, account.getSum());
                preparedStatement.setBoolean(4, account.isBlocked());
                preparedStatement.executeUpdate();
                logger.info("Account was migrated to database.");
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
                    logger.error("SQLException during closing resultSet or preparedStatement.");
                }
            }
        }
    }
}
