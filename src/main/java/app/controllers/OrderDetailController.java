package app.controllers;

import app.entities.Carport;
import app.entities.OrderDetails;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderDetailMapper;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

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

    private static void updatetask(@NotNull Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        try {
            int carportId = Integer.parseInt(ctx.formParam("carportId"));
            int width = Integer.parseInt(ctx.formParam("width"));
            int length = Integer.parseInt(ctx.formParam("length"));
            CarportMapper.updateCarport(width, length, carportId);

            //Nået her til
            List<Carport> carport = TaskMapper.getAllTasksPerUser(user.getUserId(), connectionPool);
            ctx.attribute("carport", carport);
            ctx.attribute("price", price);
            ctx.render("task.html");

        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    private static void edittask(@NotNull Context ctx) {
        User user = ctx.sessionAttribute("currentUser");
        try {


            int price = 0;
            int carportId = Integer.parseInt(ctx.formParam("carportId"));
            Carport carport = CarportMapper.getCarportById(carportId);

            //Gjort attributes klar til HTML siden
            ctx.attribute("carport", carport);
            ctx.attribute("price", price);
            ctx.render("edit.html");

        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

}




