package by.epam.dao.parser;

import by.epam.entity.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class PaymentSaxParser extends DefaultHandler {
    enum PaymentTagName {
        NUMBER, PAYMENT, PAYMENTS
    }

    private static final Logger logger = LogManager.getLogger();

    private List<Payment> paymentList = null;
    private Payment payment;
    private StringBuilder text;

    public List<Payment> getPayments() {
        return paymentList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        text = new StringBuilder();
        PaymentTagName paymentTagName;
        try {
            paymentTagName = PaymentTagName.valueOf(qName.toUpperCase().replace("-", "_"));
            if (paymentTagName.equals(PaymentTagName.PAYMENTS)) {
                paymentList = new ArrayList<>();
                logger.info("Payment list created.");
            }
            if (paymentList != null)
                switch (paymentTagName) {
                    case PAYMENT:
                        payment = new Payment();
                        logger.info("New payment created.");
                        payment.setId(Integer.parseInt(attributes.getValue("id")));
                        break;
                }
        } catch (Exception e) {
            logger.warn("Tag " + qName + " was ignored.");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        text.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        PaymentTagName paymentTagName = null;
        try {
            paymentTagName = PaymentTagName.valueOf(qName.toUpperCase().replace("-", "_"));
            logger.info("Tag " + paymentTagName + " was found.");
        } catch (Exception e) {
            logger.warn("Tag " + paymentTagName + " was ignored.");
        }
        if (paymentTagName != null) {
            if (paymentTagName.equals(PaymentTagName.PAYMENTS))
                logger.info("All payments were read by parser (" + paymentList.size() + ").");
            if (payment != null)
                switch (paymentTagName) {
                    case NUMBER:
                        payment.setPaymentNumber(Integer.parseInt(text.toString()));
                        break;
                    case PAYMENT:
                        paymentList.add(payment);
                        logger.info("Payment was added to the list.");
                        payment = null;
                        break;
                }
        }
    }
}
