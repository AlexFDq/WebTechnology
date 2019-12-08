package by.epam.dao;

import by.epam.dao.parser.*;
import by.epam.entity.*;
import by.epam.exception.DataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.IOException;
import java.util.List;

public class XmlReader {

    private static final Logger logger = LogManager.getLogger();
    private UserStaxParser userStaxParser;
    private PaymentSaxParser paymentSaxParser;
    private AccountDomParser accountDomParser;
    private AdminDomParser adminDomParser;
    private CreditCardDomParser creditCardDomParser;

    private List<User> users;
    private List<Payment> payments;
    private List<Account> accounts;
    private List<Admin> admins;
    private List<CreditCard> cards;

    public XmlReader(String file) throws SAXException, IOException, DataSourceException {
        logger.info("Xml reader starts parsing file - " + file + ".");
        userStaxParser = new UserStaxParser(file);
        logger.info("Payment system sax parser created.");
        paymentSaxParser = new PaymentSaxParser();
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(paymentSaxParser);
        xmlReader.parse(new InputSource(file));
        creditCardDomParser = new CreditCardDomParser(file);
        accountDomParser = new AccountDomParser(file);
        adminDomParser = new AdminDomParser(file);
//        paymentSaxParser = new PaymentSaxParser();
//        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
//        xmlReader.setContentHandler(paymentSaxParser);
//        xmlReader.parse(new InputSource(file));
//        accountDomParser = new AccountDomParser(file);
//        adminDomParser = new AdminDomParser(file);
//        creditCardDomParser = new CreditCardDomParser(file);
    }

    public List<User> getUsers() {
        if (users == null) {
            users = userStaxParser.getUsers();
            logger.info("User list is ready.");
        }
        return users;
    }

    public List<Admin> getAdmins() {
        if (admins == null) {
            admins = adminDomParser.getAdmins();
        }
        return admins;
    }

    public List<Payment> getPayments() {
        if (payments == null) {
            payments = paymentSaxParser.getPayments();
        }
        return payments;
    }

    public List<Account> getAccounts() {
        if (accounts == null)
            accounts = accountDomParser.getAccounts();
        return accounts;
    }

    public List<CreditCard> getCards() {
        if (cards == null) {
            cards = creditCardDomParser.getCards();
        }
        return cards;
    }
}