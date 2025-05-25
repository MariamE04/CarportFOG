package app.controllers;

// Håndterer visning af detaljerede materialer tilknyttet en specifik ordre.

import app.entities.Order;

import app.entities.Carport;

import app.entities.OrderDetails;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.OrderDetailMapper;
import app.persistence.OrderMapper;
import app.persistence.QuoteMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderDetailController {

    // Registrerer ruter til at hente og vise ordredetaljer.
    public static void addRoutes(Javalin app) {
        app.post("orderdetails", ctx -> OrderDetailController.getOrderDetailsByOrderNumber(ctx));
        app.get("orderdetails", ctx -> ctx.render("orderdetails"));
    }


    // Henter ordredetaljer (materialer og antal) fra databasen baseret på et ordre-nummer,
    // som modtages via form-parameter.
    //Lægger detaljerne i session-attribute og viser dem på siden orderdetails.

    public static void getOrderDetailsByOrderNumber(Context ctx) throws DatabaseException {
        int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));
        List<OrderDetails> orderDetails =  OrderDetailMapper.getOrderDetailsByOrder(orderNumber);

        ctx.sessionAttribute("orderDetails", orderDetails);
        ctx.render("orderdetails");

        /*for (OrderDetails orderd: orderDetails){
            System.out.println(orderd);
        }*/
    }
}




