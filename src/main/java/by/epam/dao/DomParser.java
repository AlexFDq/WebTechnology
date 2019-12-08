package by.epam.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DomParser {

    private static Document document = null;
    private static DomParser instance = new DomParser();
    private static final Logger logger = LogManager.getLogger();

    public static DomParser getInstance() {
        return instance;
    }

    public static Document parseXmlFile(File xmlFile) throws IOException, SAXException, ParserConfigurationException {
        if (document == null) {
            logger.info("Creating document for dom parser from file - " + xmlFile);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(xmlFile);
            document.normalize();
        }
        return document;
    }

}