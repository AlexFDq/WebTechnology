package by.epam.dao.parser;

import by.epam.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserStaxParser {

    private static final Logger logger = LogManager.getLogger();
    private XMLStreamReader reader;
    private List<User> users;

    public UserStaxParser(String filepath) throws FileNotFoundException {
        logger.info("Crating parser for users.");
        XMLInputFactory factory = XMLInputFactory.newFactory();
        InputStream inputStream = new FileInputStream(filepath);
        try {
            reader = factory.createXMLStreamReader(inputStream);
            logger.info("Stax parser to parse users from " + filepath + " is ready.");
            parse();
        } catch (XMLStreamException e) {
            logger.error("Error during creating XML stream reader from file = " + filepath, e);
            System.out.println("Error during creating XML stream reader from file = " + filepath);
        }
    }

    enum UserTagName {
        NAME, CARD_IDS, CARD_ID, PASSWORD_HASH, AUTHORITY_LEVEL, USER, PAYMENT_SYSTEM, USERS
    }

    private void parse() throws XMLStreamException {
        logger.info("Stax parser gets list of users.");
        users = new ArrayList<>();
        User user = null;
        UserTagName tagName = null;
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    String str = reader.getLocalName().trim().toUpperCase().replace("-", "_");
                    logger.info("Start tag: " + str);
                    try {
                        tagName = UserTagName.valueOf(str);
                    } catch (Exception e) {
                        logger.warn("Start tag " + str + " was ignored.", e);
                    }
                    if (tagName == UserTagName.USER) {
                        user = new User();
                        logger.info("New user created.");
                    }
                    break;
                case XMLEvent.CHARACTERS:
                    String text = reader.getText().trim();
                    List<Integer> card_ids = new ArrayList<>();
                    if (text.isEmpty())
                        break;
                    if (user != null)
                        switch (tagName) {
                            case NAME:
                                user.setName(text);
                                logger.info("name set.");
                                break;
                            case CARD_ID:
                                card_ids.add(Integer.parseInt(text));
                                user.setCardIds(card_ids);
                                logger.info("card_id add to the list of cards.");
                                break;
                            case PASSWORD_HASH:
                                user.setPasswordHash(text);
                                logger.info("password hash set.");
                                break;
                            case AUTHORITY_LEVEL:
                                user.setAuthorityLevel(Integer.parseInt(text));
                                logger.info("authority level set.");
                                break;
                        }
                    break;
                case XMLEvent.END_ELEMENT:
                    String s = reader.getLocalName().toUpperCase().replace("-", "_");
                    if (s.equals("USERS")) {
                        logger.info("All users were got by parser (" + users.size() + ").");
                        return;
                    }
                    try {
                        tagName = UserTagName.valueOf(s);
                    } catch (Exception e) {
                        logger.warn("Start tag " + tagName + " was ignored.", e);
                    }
                    if (tagName == UserTagName.USER) {
                        logger.info("New user added to the list.");
                        users.add(user);
                        user = null;
                    }
                    break;
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

}
