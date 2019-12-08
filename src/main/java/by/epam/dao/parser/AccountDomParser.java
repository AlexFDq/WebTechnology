package by.epam.dao.parser;

import by.epam.dao.DomParser;
import by.epam.entity.Account;
import by.epam.exception.DataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountDomParser {

    private static final Logger logger = LogManager.getLogger();
    private List<Account> accounts;

    enum AccountTagName {
        NUMBER, SUM, BLOCKED
    }

    public AccountDomParser(String filepath) throws DataSourceException {
        File xmlFile = new File(filepath);
        accounts = new ArrayList<>();
        logger.info("A list of accounts was created.");
        try {
            Document document = DomParser.parseXmlFile(xmlFile);
            NodeList accountNodes = document.getDocumentElement().getElementsByTagName("Account");
            for (int i = 0; i < accountNodes.getLength(); i++) {
                if (accountNodes.item(i).getNodeType() != Node.TEXT_NODE) {
                    accounts.add(getAccountFromNode(accountNodes.item(i)));
                    logger.info("New account was added to the list.");
                }
            }
            logger.info("All accounts were got by parser (" + accounts.size() + ")");
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new DataSourceException("File " + xmlFile.getName() + " not found or is incorrect.");
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    private Account getAccountFromNode(Node orderNode) {
        Account account = new Account();
        logger.info("New account created.");
        NodeList accountProps = orderNode.getChildNodes();
        AccountTagName accountTagName;
        String str = null;
        for (int j = 0; j < accountProps.getLength(); j++) {
            if (accountProps.item(j).getNodeType() != Node.TEXT_NODE) {
                try {
                    str = accountProps.item(j).getNodeName();
                    accountTagName = AccountTagName.valueOf(str.toUpperCase().replace("-", "_"));
                    switch (accountTagName) {
                        case NUMBER:
                            account.setAccountNumber(accountProps.item(j).getTextContent());
                            break;
                        case SUM:
                            account.setSum(Integer.parseInt(accountProps.item(j).getTextContent()));
                            break;
                        case BLOCKED:
                            account.setBlocked(Boolean.parseBoolean(accountProps.item(j).getTextContent()));
                            break;
                    }
                } catch (Exception e) {
                    logger.warn("Tag " + str + " was ignored.");
                }
            }
        }
        return account;
    }

}
