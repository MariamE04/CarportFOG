package app.controllers;

import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.util.CarportSvg;
import app.util.Svg;
import app.util.SvgToPdfConverter;
import io.javalin.http.Context;
import org.apache.batik.transcoder.TranscoderException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderController {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void getAllOrders(Context ctx) throws DatabaseException {
        List<Order> orders = OrderMapper.getAllOrders();

        ctx.attribute("orders", orders);
        ctx.render("admin.html");

    }

}

