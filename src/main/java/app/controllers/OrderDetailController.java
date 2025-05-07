package app.controllers;

import app.entities.OrderDetails;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderDetailMapper;
import io.javalin.http.Context;

import java.util.List;

public class OrderDetailController {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public static void getOrderDetailsByOrderNumber(Context ctx) throws DatabaseException {
        int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));
        List<OrderDetails> orderDetails =  OrderDetailMapper.getOrderDetailsByOrder(orderNumber);
        ctx.sessionAttribute("orderDetails", orderDetails);
        ctx.render("orderdetails");
        for (OrderDetails orderd: orderDetails){
            System.out.println(orderd);
        }
    }

}




