package app.controllers;

import app.entities.Carport;
import app.entities.Order;
import app.entities.OrderDetails;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderDetailMapper;
import app.persistence.OrderMapper;
import io.javalin.http.Context;
import org.apache.batik.transcoder.TranscoderException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    public static void updateOrder(@NotNull Context ctx) {
        try {

            //Update Carport
            int carportId = Integer.parseInt(ctx.formParam("edit_carportId"));
            int width = Integer.parseInt(ctx.formParam("carport-width"));
            int length = Integer.parseInt(ctx.formParam("carport-length"));
            CarportMapper.updateCarport(width, length, carportId);

            //Update Price
            //TODO Lav en update til price når vi kan beregne prisen
            int price = 1;

            //Update Quantity
            int quantity = Integer.parseInt(ctx.formParam("chosen-quantity"));
            int orderDetailId = Integer.parseInt(ctx.formParam("edit_orderDetailId"));
            OrderDetailMapper.updateQuantity(quantity ,orderDetailId);

            //Carport
            Carport carport = CarportMapper.getCarportById(carportId);
            ctx.attribute("carport", carport);

            //Price
            ctx.attribute("price", price);

            //Update quantity
            ctx.attribute("quantity");

            ctx.redirect("/admin");



        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("admin.html");
        }
    }

    public static void editOrder(@NotNull Context ctx) throws DatabaseException{

        //todo: Laver pris her, men skal ændres til at hente fra et sted
            int price = 0;

            //Henter carportId her fra admin.html
            int carportId = Integer.parseInt(ctx.formParam("carportId"));
            System.out.println(carportId);
            Carport carport = CarportMapper.getCarportById(carportId);
            System.out.println("Carport hentet"+carport);

            //Vi har allerede lavet en attribute til orderDetails.



            //Gjort attributes klar til HTML siden

            ctx.attribute("carport", carport);
            ctx.attribute("price", price);

            ctx.render("edit.html");


    }

}

