package by.epam.service.migration;

import by.epam.entity.Payment;
import by.epam.exception.AlreadyExistsException;
import by.epam.exception.DatabaseException;
import by.epam.service.DbManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class PaymentMigrationService {

    private static final Logger logger = LogManager.getLogger();
    private static final PaymentMigrationService instance = new PaymentMigrationService();

    private PaymentMigrationService() {
    }

    public static PaymentMigrationService getInstance() {
        return instance;
    }

    public void migrate(List<Payment> payments) throws AlreadyExistsException, DatabaseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DbManager dbManager = DbManager.getInstance();
        Connection connection = dbManager.getConnection();
        for (Payment payment : payments) {
            try {
                preparedStatement = connection.prepareStatement("SELECT * FROM payments WHERE id = ?;");
                preparedStatement.setInt(1, payment.getId());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.first())
                    throw new AlreadyExistsException("Such payment already exists!");
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO payments (id, number ) VALUES (?,?);");
                preparedStatement.setInt(1, payment.getId());
                preparedStatement.setInt(2, payment.getPaymentNumber());
                preparedStatement.executeUpdate();
                logger.info("Payment was migrated to database.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                logger.error("SQLException: ", e);
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
