package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <orders.json> <paymentmethods.json>");
            System.exit(1);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);


        List<Order> orders = mapper.readValue(new File(args[0]), new TypeReference<List<Order>>() {
        });
        List<PaymentMethod> methods = mapper.readValue(new File(args[1]), new TypeReference<List<PaymentMethod>>() {
        });

        Map<String, BigDecimal> totals = new HashMap<>();
        totals.put("PUNKTY", new BigDecimal("100.00"));
        totals.put("mZysk", new BigDecimal("165.00"));

        Map<String, BigDecimal> spending = PaymentAllocator.allocatePayments(orders, methods);

        System.out.println("Wyniki przypisania platnosci:");
        for (var entry : spending.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}