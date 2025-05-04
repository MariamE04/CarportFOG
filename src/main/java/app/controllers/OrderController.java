package app.controllers;

import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.util.CarportSvg;
import app.util.Svg;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderController {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void showOrder(Context ctx){
        Locale.setDefault(new Locale("US"));

        int width = Integer.parseInt(ctx.queryParam("width"));   // fx fra ?width=600
        int length = Integer.parseInt(ctx.queryParam("length")); // fx fra ?length=780

        CarportSvg svg = new CarportSvg(width, length);

        ctx.attribute("svg", svg.toString());
        ctx.render("showOrder.html");
    }

    public static void getAllOrders(Context ctx) throws DatabaseException {
        List<Order> orders = OrderMapper.getAllOrders();

        ctx.attribute("orders", orders);
        ctx.render("admin.html");

    }

}

