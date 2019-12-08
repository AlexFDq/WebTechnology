package by.epam.dao.parser;

import by.epam.dao.DomParser;
import by.epam.entity.CreditCard;
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

public class CreditCardDomParser {
    private static final Logger logger = LogManager.getLogger();
    private List<CreditCard> cardsList;

    enum CardTagName {
        NUMBER, VALIDITY, SECRET_NUMBER, ACCOUNT_ID
    }

    public CreditCardDomParser(String filepath) throws DataSourceException {
        File xmlFile = new File(filepath);
        cardsList = new ArrayList<>();
        logger.info("Cards list created.");
        try {
            Document document = DomParser.parseXmlFile(xmlFile);
            NodeList cardsNodes = document.getDocumentElement().getElementsByTagName("Order");
            for (int i = 0; i < cardsNodes.getLength(); i++) {
                if (cardsNodes.item(i).getNodeType() != Node.TEXT_NODE) {
                    cardsList.add(getCardFromNode(cardsNodes.item(i)));
                    logger.info("Card added to the list.");
                }
            }
            logger.info("All cards were got by parser (" + cardsList.size() + ")");
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new DataSourceException("File " + xmlFile.getName() + " not found or is incorrect.");
        }
    }

    public List<CreditCard> getCards() {
        return cardsList;
    }

    private CreditCard getCardFromNode(Node cardNode) {
        logger.info("New card created.");
        CreditCard card = new CreditCard();
        card.setId(Integer.parseInt(cardNode.getAttributes().getNamedItem("id").getNodeValue()));
        NodeList cardProps = cardNode.getChildNodes();
        CardTagName cardTagName;
        String str = null;
        for (int j = 0; j < cardProps.getLength(); j++) {
            if ((cardProps.item(j).getNodeType() != Node.TEXT_NODE)) {
                try {
                    str = cardProps.item(j).getNodeName();
                    cardTagName = CardTagName.valueOf(str.toUpperCase().replace("-", "_"));
                    logger.info("Tag " + str + " was found.");
                    switch (cardTagName) {
                        case NUMBER:
                            card.setCardNumber(cardProps.item(j).getTextContent());
                            break;
                        case VALIDITY:
                            card.setValidity(cardProps.item(j).getTextContent());
                            break;
                        case SECRET_NUMBER:
                            card.setSecretNumber(Integer.parseInt(cardProps.item(j).getTextContent()));
                            break;
                        case ACCOUNT_ID:
                            card.setAccount_id(Integer.parseInt(cardProps.item(j).getTextContent()));
                            break;
                    }
                } catch (Exception e) {
                    logger.warn("Tag " + str + "  was ignored.");
                }
            }
        }
        return card;
    }
}
