package org.example;

import java.math.BigDecimal;

public class PaymentMethod {
    public String id;
    public int discount;
    public BigDecimal limit;

    public PaymentMethod() {
    }

    public PaymentMethod(String id, int discount, BigDecimal limit) {
        this.id = id;
        this.discount = discount;
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public int getDiscount() {
        return discount;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }
}
