package by.epam.service.migration;

import by.epam.entity.CreditCard;
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

public class CreditCardMigrationService {

    private static final Logger logger = LogManager.getLogger();
    private static final CreditCardMigrationService instance = new CreditCardMigrationService();

    private CreditCardMigrationService(){
    }

    public static CreditCardMigrationService getInstance() {
        return instance;
    }

    public void migrate(List<CreditCard> cards)  throws DatabaseException, AlreadyExistsException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DbManager dbManager = DbManager.getInstance();
        Connection connection = dbManager.getConnection();
        for (CreditCard card : cards) {
            try {
                preparedStatement = connection.prepareStatement("SELECT * FROM cards WHERE id = ?;");
                preparedStatement.setInt(1, card.getId());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.first()) {
                    throw new AlreadyExistsException("Such card already exists!");
                }
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO orders (id, number, validity, secret_number, account_id) VALUES (?,?,?,?,?);");
                preparedStatement.setInt(1, card.getId());
                preparedStatement.setString(2, card.getCardNumber());
                preparedStatement.setString(3, card.getValidity());
                preparedStatement.setInt(4, card.getSecretNumber());
                preparedStatement.setInt(5, card.getAccount_id());
                preparedStatement.executeUpdate();
                logger.info("Card was migrated to database.");
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
                    logger.info("SQLException while closing resultSet or preparedStatement.");
                }
            }
        }
    }

}
