package org.example;

import java.util.*;
import java.math.BigDecimal;

public class Order {
    public String id;
    public BigDecimal value;
    public List<String> promotions = new ArrayList<>();

    public Order() {
    }

    public Order(String id, BigDecimal value, List<String> promotions) {
        this.id = id;
        this.value = value;
        this.promotions = promotions;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public List<String> getPromotions() {
        return promotions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPromotions(List<String> promotions) {
        this.promotions = promotions;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
