package by.epam;

import by.epam.dao.XmlReader;
import by.epam.exception.AlreadyExistsException;
import by.epam.exception.DataSourceException;
import by.epam.exception.DatabaseException;
import by.epam.exception.ValidationException;
import by.epam.service.XSDValidator;
import by.epam.service.migration.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class Main {
    private final static String xmlFile = "src/main/resources/data.xml";
    private final static String xsdFile = "src/main/resources/data_schema.xsd";
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
             logger.info("Application started.");
            XSDValidator.validate(new File(xmlFile), new File(xsdFile));

            XmlReader xmlReader = new XmlReader(xmlFile);

            AccountMigrationService countryMigrationService = AccountMigrationService.getInstance();
            countryMigrationService.migrate(xmlReader.getAccounts());

            AdminMigrationService adminMigrationService = AdminMigrationService.getInstance();
            adminMigrationService.migrate(xmlReader.getAdmins());

            PaymentMigrationService paymentMigrationService = PaymentMigrationService.getInstance();
            paymentMigrationService.migrate(xmlReader.getPayments());

            UserMigrationService userMigrationService = UserMigrationService.getInstance();
            userMigrationService.migrate(xmlReader.getUsers());

            CreditCardMigrationService creditCardMigrationService = CreditCardMigrationService.getInstance();
            creditCardMigrationService.migrate(xmlReader.getCards());

            System.out.println("Users - " + xmlReader.getUsers().size());
            System.out.println("Payments - " + xmlReader.getPayments().size());
            System.out.println("Accounts - " + xmlReader.getAccounts().size());
            System.out.println("Admins - " + xmlReader.getAdmins().size());
            System.out.println("Credit cards - " + xmlReader.getCards().size());

            logger.info("Success.");
        } catch (SAXException | IOException | DataSourceException e) {
            logger.error("Problems with file " + xmlFile, e);
            System.out.println("Problems with file " + xmlFile);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            logger.error("File " + xmlFile + " is not valid", e);
        } catch (AlreadyExistsException e) {
            logger.error("AlreadyExistsException: ", e);
            System.out.println("AlreadyExistsException: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("DatabaseException " + e.getMessage());
            logger.error("DatabaseException: ", e);
        }
        logger.info("Application finished.");
    }
}
