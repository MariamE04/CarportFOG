package app.entities;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void priceSummation() {
        List<OrderDetails> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderDetails(new Material(300, 1, "", "", 40, ""), 6, 0));
        orderDetails.add(new OrderDetails(new Material(600, 15, "", "", 28, ""), 15, 0));
        orderDetails.add(new OrderDetails(new Material(480, 1, "", "", 28, ""), 4, 0));
        Order order = new Order(0, "");
        order.setOrderDetails(orderDetails);
        order.priceSummation();
        DecimalFormat df = new DecimalFormat("0.00");
        assertEquals(df.format(3.0*40.0*6.0+6.0*28.0*15.0+4.8*28.0*4.0), order.twoDecimals());
    }
}