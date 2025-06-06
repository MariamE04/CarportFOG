package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.OrderDetailMapper;
import app.persistence.OrderMapper;
import app.util.Calculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarportController {

    public static void addRoutes(Javalin app){
        app.get("createCarport", ctx -> {
            CarportController.showWidthAndLength(ctx);
        });

        app.post("createCarport", CarportController::sendUserData);
    }

    public static void showWidthAndLength(Context ctx){
        List<Integer> width = new ArrayList<>();
        List<Integer> length = new ArrayList<>();
        int i = 240;
        while(i<=780){
            width.add(i);
            if (i <= 600)
                length.add(i);
            i = i + 60;
        }
        ctx.attribute("widthOptions", width);
        ctx.attribute("lengthOptions", length);
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
                CarportMapper.addCarport(carport);
            }

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.render("createCarport.html");
    }

    public static void interpretUserData(Carport carport) throws DatabaseException {
        Order order = new Order(LocalDate.now(), 0, "Afventer betaling", carport.getUser().getId(), carport, null);
        OrderMapper.addOrder(order);
        order.setOrder_id(OrderMapper.getLatestOrderNr());

        List<Material> materials = Calculator.orderCalculator(carport.getWidth());
        List<OrderDetails> orderDetails = new ArrayList<>();

        int i = 0;
        for (Material material : materials) {
            if (i > 0 && material.getMaterialId() == orderDetails.get(orderDetails.size() - 1).getMaterial().getMaterialId()) {
                orderDetails.get(orderDetails.size() - 1).setQuantity(orderDetails.get(orderDetails.size() - 1).getQuantity() + 1);
            }
            else {
                if (i > 0) {
                    OrderDetailMapper.addOrderDetail(
                            orderDetails.get(orderDetails.size() - 1).getOrderId(),
                            orderDetails.get(orderDetails.size() - 1).getMaterial(),
                            orderDetails.get(orderDetails.size() - 1).getQuantity()
                    );
                }
                orderDetails.add(new OrderDetails(material, material.getAmount(), order.getOrder_id()));
            }
            i++;
        }
        OrderDetailMapper.addOrderDetail(
                orderDetails.get(orderDetails.size() - 1).getOrderId(),
                orderDetails.get(orderDetails.size() - 1).getMaterial(),
                orderDetails.get(orderDetails.size() - 1).getQuantity()
        );

        order.setOrderDetails(orderDetails);
        order.priceSummation();
        OrderMapper.updatePrice(order.getOrder_id(), order.getTotal_price());
    }

    public static void adminOrderUpdater(Context ctx) throws DatabaseException {
        List<Carport> carports = OrderMapper.getCarportsWithoutOrders();
        for (Carport carport : carports) {
            interpretUserData(carport);
        }
        ctx.render("admin.html");
    }
}






