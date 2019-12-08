package by.epam.dao.parser;

import by.epam.dao.DomParser;
import by.epam.entity.Admin;
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

public class AdminDomParser {
    private static final Logger logger = LogManager.getLogger();
    private List<Admin> adminList;

    enum AdminTagName {
        NAME, PASSWORD_HASH, AUTHORITY_LEVEL
    }

    public AdminDomParser(String filepath) throws DataSourceException {
        File xmlFile = new File(filepath);
        adminList = new ArrayList<>();
        logger.info("List of admins created.");
        try {
            Document document = DomParser.parseXmlFile(xmlFile);
            NodeList adminNodes = document.getDocumentElement().getElementsByTagName("Admin");
            for (int i = 0; i < adminNodes.getLength(); i++) {
                if (adminNodes.item(i).getNodeType() != Node.TEXT_NODE) {
                    adminList.add(getAdminFromNode(adminNodes.item(i)));
                    logger.info("An admin was added to the list");
                }
                logger.info("All admins were got by parser (" + adminList.size() + ")");
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new DataSourceException("File " + xmlFile.getName() + " not found or is incorrect.");
        }
    }

    public List<Admin> getAdmins() {
        return adminList;
    }

    private Admin getAdminFromNode(Node adminNode) {
        Admin admin = new Admin();
        logger.info("New admin created.");
        NodeList adminProps = adminNode.getChildNodes();
        AdminTagName adminTagName;
        String str = null;
        for (int j = 0; j < adminProps.getLength(); j++) {
            if ((adminProps.item(j).getNodeType() != Node.TEXT_NODE)) {
                try {
                    str = adminProps.item(j).getNodeName();
                    String text = adminProps.item(j).getTextContent();
                    adminTagName = AdminTagName.valueOf(str.toUpperCase().replace("-", "_"));
                    logger.info("Tag " + str + " was found.");
                    switch (adminTagName) {
                        case NAME:
                            admin.setName(text);
                            break;
                        case PASSWORD_HASH:
                            admin.setPasswordHash(text);
                            break;
                        case AUTHORITY_LEVEL:
                            admin.setAuthorityLevel(Integer.parseInt(text));
                            break;
                    }
                } catch (Exception e) {
                    logger.warn("Tag " + str + " was ignored.");
                }
            }
        }
        return admin;
    }
}
