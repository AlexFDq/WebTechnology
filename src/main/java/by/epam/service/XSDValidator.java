package by.epam.service;

import by.epam.exception.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XSDValidator {
    private static final Logger logger = LogManager.getLogger();

    public static void validate(File xmlFile, File xsdFile) throws ValidationException {
        logger.info("Validation started.");
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            logger.info("File " + xmlFile +" validated successfully.");
        } catch (IOException e) {
            throw new ValidationException("Validation failed: " + e.getMessage());
        } catch (org.xml.sax.SAXException e) {
            throw new ValidationException(xmlFile.getName() + " isn't valid: " + e);
        }
    }
}
