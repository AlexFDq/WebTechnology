package by.epam.entity;

import java.util.Objects;

public class Payment {

    private int paymentNumber;
    private int id;

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return paymentNumber == payment.paymentNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentNumber);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentNumber=" + paymentNumber +
                '}';
    }
}