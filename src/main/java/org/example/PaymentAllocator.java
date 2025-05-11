package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PaymentAllocator {
    public static Map<String, BigDecimal> allocatePayments(List<Order> orders, List<PaymentMethod> paymentMethods) {
        Map<String, BigDecimal> spending = new HashMap<>();
        Map<String, PaymentMethod> methodMap = new HashMap<>();

        for (PaymentMethod m : paymentMethods) {
            methodMap.put(m.id, new PaymentMethod(m.id, m.discount, m.limit));
            spending.put(m.id, BigDecimal.ZERO);
        }

        orders.sort(Comparator.comparing(o -> o.value, Comparator.reverseOrder()));

        for (Order order : orders) {
            BigDecimal orderValue = order.value;
            BigDecimal bestDiscount = BigDecimal.ZERO;
            String bestMethodId = null;
            BigDecimal bestPayAmount = orderValue;

            if (methodMap.containsKey("PUNKTY")) {
                PaymentMethod points = methodMap.get("PUNKTY");
                if (points.limit.compareTo(orderValue) >= 0) {
                    BigDecimal discount = percent(orderValue, points.discount);
                    BigDecimal pay = orderValue.subtract(discount);

                    if (discount.compareTo(bestDiscount) > 0) {
                        bestDiscount = discount;
                        bestMethodId = "PUNKTY";
                        bestPayAmount = pay;
                    }
                }
            }

            List<String> promos = order.promotions != null ? order.promotions : Collections.emptyList();
            for (String methodId : promos) {
                PaymentMethod method = methodMap.get(methodId);

                if (method != null && method.limit.compareTo(bestPayAmount) >= 0) {
                    BigDecimal discount = percent(orderValue, method.discount);
                    BigDecimal pay = orderValue.subtract(discount);

                    if (discount.compareTo(bestDiscount) > 0) {
                        bestDiscount = discount;
                        bestMethodId = methodId;
                        bestPayAmount = pay;
                    }
                }
            }

            if (bestMethodId != null) {
                PaymentMethod method = methodMap.get(bestMethodId);
                method.limit = method.limit.subtract(bestPayAmount);
                spending.put(bestMethodId, spending.get(bestMethodId).add(bestPayAmount));
            } else {
                if (methodMap.containsKey("PUNKTY")) {
                    PaymentMethod points = methodMap.get("PUNKTY");
                    if (points.limit.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal minPoints = orderValue.multiply(new BigDecimal("0.10"));
                        BigDecimal usedPoints = points.limit.min(orderValue);

                        if (usedPoints.compareTo(minPoints) >= 0) {
                            BigDecimal discount = percent(orderValue, 10);
                            BigDecimal remaining = orderValue.subtract(discount).subtract(usedPoints);

                            points.limit = points.limit.subtract(usedPoints);
                            spending.put("PUNKTY", spending.get("PUNKTY").add(usedPoints));

                            PaymentMethod fallback = findAnyWithLimit(methodMap, remaining);
                            if (fallback != null) {
                                fallback.limit = fallback.limit.subtract(remaining);
                                spending.put(fallback.id, spending.get(fallback.id).add(remaining));
                                continue;
                            }
                        }
                    }
                }
            }

            if (bestMethodId == null) {
                PaymentMethod fallback = findAnyWithLimit(methodMap, orderValue);
                if (fallback != null) {
                    fallback.limit = fallback.limit.subtract(orderValue);
                    spending.put(fallback.id, spending.get(fallback.id).add(orderValue));
                } else {
                    System.err.println("Brak środków na opłacenie zamówienia: " + order.id);
                }
            }
        }

        return spending;
    }

    private static BigDecimal percent(BigDecimal value, int p) {
        return value.multiply(BigDecimal.valueOf(p)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private static PaymentMethod findAnyWithLimit(Map<String, PaymentMethod> methods, BigDecimal amount) {
        return methods.values().stream().filter(m -> m.limit.compareTo(amount) >= 0 && !m.id.equals("PUNKTY")).findFirst().orElse(null);
    }
}
