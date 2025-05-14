package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderDetailMapper;
import app.persistence.OrderMapper;
import app.util.Calculator;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarportController {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void showWidthAndLength(Context ctx){
            List<Carport> widthAndLength = new ArrayList<>();
            int i = 240;
            while(i<=780){
                widthAndLength.add(new Carport(i,i));
                i = i+60;
            }
            ctx.attribute("widthAndLength", widthAndLength);
            ctx.render("createCarport.html");
        }

    public static void sendUserData(Context ctx){
        try {
            if (ctx.sessionAttribute("currentUser") != null) {
                //Collects users choice of measures
                int width = Integer.parseInt(ctx.formParam("width"));
                int length = Integer.parseInt(ctx.formParam("length"));

                //Inserts to database
                Carport carport = new Carport(width, length, ctx.sessionAttribute("currentUser"));
                CarportMapper.addWidthAndLength(carport);
            }

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.render("createCarport.html");
    }

    public static void interpretUserData(Carport carport) throws DatabaseException {
        Order order = new Order(LocalDate.now(), 0, "afventer betaling", carport.getUser().getId(), carport, null);
        OrderMapper.addOrder(order);
        List<Material> materials = Calculator.orderCalculator(carport.getWidth(), carport.getLength());
        List<OrderDetails> orderDetails = new ArrayList<>();
        int i = 0;
        for (Material material : materials) {
            orderDetails.add(new OrderDetails(material, material.getAmount(), OrderMapper.getLatestOrderNr()));
            OrderDetailMapper.addOrderDetail(orderDetails.get(i).getOrderId(), orderDetails.get(i).getMaterials(), orderDetails.get(i).getQuantity());
        }
        order.setOrderDetails(orderDetails);
        order.priceSummation();
    }
}





