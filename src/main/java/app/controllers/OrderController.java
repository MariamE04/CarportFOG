package app.controllers;

import app.entities.Carport;
import app.entities.Order;
import app.entities.OrderDetails;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.*;
import app.entities.Quote;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.OrderMapper;
import app.persistence.QuoteMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.batik.transcoder.TranscoderException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app){
        app.post("editOrder", ctx -> OrderController.editOrder(ctx));
        app.get("editOrder", ctx -> ctx.render("editOrder"));
        app.post("updateOrder", ctx -> OrderController.updateOrder(ctx));
        app.post("/admin", ctx -> OrderController.updateOrder(ctx));
        app.get("/admin", ctx -> {
            CarportController.adminOrderUpdater(ctx);
            OrderController.getAllOrders(ctx);
        });
       //Er det nødvendigt? app.get("/admin", ctx -> OrderController.updateOrder(ctx));


    }

    public static void getAllOrders(Context ctx) throws DatabaseException {
        List<Order> orders = OrderMapper.getAllOrders();
        ctx.attribute("orders", orders);
        ctx.render("admin.html");

    }


    public static void updateOrder(@NotNull Context ctx) {
        try {
            System.out.println("updateMetode | orderNumber-edit param: " + ctx.formParam("orderNumber-update"));

            //Update Carport
            int carportId = Integer.parseInt(ctx.formParam("edit_carportId"));
            int width = Integer.parseInt(ctx.formParam("carport-width"));
            int length = Integer.parseInt(ctx.formParam("carport-length"));
            CarportMapper.updateCarport(width, length, carportId);


            //Update Price
            int orderNumber = Integer.parseInt(ctx.formParam("orderNumber-update"));
            double totalPrice = Double.parseDouble(ctx.formParam("editPrice"));
            OrderMapper.updateOrderPrice(totalPrice, orderNumber);


            //Update Quantity
            int quantity = Integer.parseInt(ctx.formParam("chosen-quantity"));
            int orderDetailId = Integer.parseInt(ctx.formParam("edit_orderDetailId"));
            System.out.println("OrderDetail Number i UpdateMetode: "+orderDetailId);
            OrderDetailMapper.updateQuantity(quantity ,orderDetailId);

            //Update new materialLength
            String materialLengthAndNamesFromHtml = ctx.formParam("chosenMaterialLengthAndName");
            String[] materialLengthAndName = materialLengthAndNamesFromHtml.split(":");

            int materialLength = Integer.parseInt(materialLengthAndName[0]);
            String materialName = materialLengthAndName[1];

            int materialId = MaterialMapper.getMaterialIdByChosenLengthAndName(materialLength, materialName);
            System.out.println("Material ID: "+materialId);
            MaterialMapper.updateMaterialId(materialId, orderDetailId);

            //Carport
            Carport carport = CarportMapper.getCarportById(carportId);
            ctx.attribute("carport", carport);

            //Price
            //ctx.attribute("price", totalPrice);


            ctx.redirect("/admin");



        } catch (DatabaseException | NumberFormatException e) {
            System.out.println("Fejl i updateOrder catch block");
            ctx.attribute("message", e.getMessage());
            ctx.render("admin.html");
        }
    }

    public static void editOrder(@NotNull Context ctx) throws DatabaseException{
        System.out.println("editMetode | orderNumber-edit param: " + ctx.formParam("orderNumber-edit"));

        //Henter orderNumber fra admin.html
            int orderNumber = Integer.parseInt(ctx.formParam("orderNumber-edit"));
            double price = OrderMapper.getPrice(orderNumber);

            //Henter carportId her fra admin.html
            int carportId = Integer.parseInt(ctx.formParam("carportId"));
            Carport carport = CarportMapper.getCarportById(carportId);



            //OrderDetails
        List<OrderDetails> orderDetailsEdit =  OrderDetailMapper.getOrderDetailsByOrder(orderNumber);
        ctx.sessionAttribute("orderDetailsEdit", orderDetailsEdit);
            if(orderDetailsEdit.isEmpty()){
                System.out.println("OrderDetails liste er tom");
            }

            //Her viser vi alle materialLengths
            ArrayList<String> materialLengthAndNames = MaterialMapper.getAllLengthsAndNames();


            //Gjort attributes klar til HTML siden
            ctx.attribute("materialLengthAndNames", materialLengthAndNames);
            ctx.attribute("carport", carport);
            ctx.attribute("price", price);

            ctx.render("edit.html");
    }
}

