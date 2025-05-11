import org.example.Order;
import org.example.PaymentAllocator;
import org.example.PaymentMethod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentAllocatorTest {

    private Order createOrder(String id, double value, List<String> promotions) {
        Order order = new Order();
        order.setId(id);
        order.setValue(BigDecimal.valueOf(value));
        order.setPromotions(promotions);
        return order;
    }

    private PaymentMethod createPaymentMethod(String id, int discount, double limit) {
        PaymentMethod method = new PaymentMethod();
        method.setId(id);
        method.setDiscount(discount);
        method.setLimit(BigDecimal.valueOf(limit));
        return method;
    }

    @Test
    void testAllocatePayments_NoPromotions() {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder("ORDER1", 100.00, null));
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(createPaymentMethod("PUNKTY", 10, 150.00));

        Map<String, BigDecimal> spending = PaymentAllocator.allocatePayments(orders, methods);

        assertEquals(1, spending.size());
        assertEquals(new BigDecimal("90.00"), spending.get("PUNKTY"));
    }

    @Test
    void testAllocatePayments_WithPromotions() {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder("ORDER1", 100.00, List.of("mZysk")));
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(createPaymentMethod("PUNKTY", 10, 150.00));
        methods.add(createPaymentMethod("mZysk", 20, 100.00));

        Map<String, BigDecimal> spending = PaymentAllocator.allocatePayments(orders, methods);

        assertEquals(2, spending.size());
        assertEquals(new BigDecimal("80.00"), spending.get("mZysk"));
    }

    @Test
    void testAllocatePayments_InsufficientFunds() {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder("ORDER1", 200.00, List.of("mZysk")));
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(createPaymentMethod("mZysk", 20, 100.00));

        Map<String, BigDecimal> spending = PaymentAllocator.allocatePayments(orders, methods);

        assertEquals(1, spending.size());
        assertEquals(new BigDecimal("0"), spending.get("mZysk"));
    }

    @Test
    void testAllocatePayments_MultipleOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder("ORDER1", 100.00, List.of("mZysk")));
        orders.add(createOrder("ORDER2", 50.00, List.of("PUNKTY")));
        List<PaymentMethod> methods = new ArrayList<>();
        methods.add(createPaymentMethod("PUNKTY", 10, 100.00));
        methods.add(createPaymentMethod("mZysk", 20, 100.00));

        Map<String, BigDecimal> spending = PaymentAllocator.allocatePayments(orders, methods);

        assertEquals(2, spending.size());
        assertEquals(new BigDecimal("80.00"), spending.get("mZysk"));
        assertEquals(new BigDecimal("45.00"), spending.get("PUNKTY"));
    }
}
