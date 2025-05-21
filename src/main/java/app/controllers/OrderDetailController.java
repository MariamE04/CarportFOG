package app.controllers;


import app.entities.Order;

import app.entities.Carport;

import app.entities.OrderDetails;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderDetailMapper;
import app.persistence.OrderMapper;
import app.persistence.QuoteMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderDetailController {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public static void addRoutes(Javalin app) {
        app.post("orderdetails", ctx -> OrderDetailController.getOrderDetailsByOrderNumber(ctx));
        app.get("orderdetails", ctx -> ctx.render("orderdetails"));
    }

    public static void getOrderDetailsByOrderNumber(Context ctx) throws DatabaseException {
        int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));
        List<OrderDetails> orderDetails =  OrderDetailMapper.getOrderDetailsByOrder(orderNumber);
       // double totalPrice = QuoteMapper.getQuoteById(orderNumber).getPrice();

        //double totalPrice = QuoteMapper.calculateQuotePrice(orderNumber);

        ctx.sessionAttribute("orderDetails", orderDetails);
        //ctx.sessionAttribute("totalPrice", totalPrice);
        ctx.render("orderdetails");
        /*for (OrderDetails orderd: orderDetails){
            System.out.println(orderd);
        }*/
    }
}




