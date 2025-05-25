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

    // Registrerer HTTP-ruter til håndtering af carport-oprettelse
    public static void addRoutes(Javalin app){
        app.get("createCarport", ctx -> {
            CarportController.showWidthAndLength(ctx); // Viser valg af bredde og længde til carport
        });

        app.post("createCarport", CarportController::sendUserData);  // Modtager og behandler brugerens input til carport
    }

    // Viser muligheder for bredde og længde i et interval med faste trin
    public static void showWidthAndLength(Context ctx){
        List<Integer> width = new ArrayList<>();
        List<Integer> length = new ArrayList<>();
        int i = 240;

        // Fylder lister med bredde- og længdemuligheder, hvor længden er op til 600
        while(i<=780){
            width.add(i);
            if (i <= 600)
                length.add(i);
            i = i + 60;
        }

        // Sætter bredde- og længdemuligheder som attributter til brug i HTML-skabelonen
        ctx.attribute("widthOptions", width);
        ctx.attribute("lengthOptions", length);
        ctx.render("createCarport.html"); // Render siden med valgmuligheder
    }

    // Modtager brugerens valg af bredde og længde, opretter en Carport og gemmer den i databasen
    public static void sendUserData(Context ctx){
        try {
            if (ctx.sessionAttribute("currentUser") != null) {
                // Henter værdier fra formularen
                int width = Integer.parseInt(ctx.formParam("width"));
                int length = Integer.parseInt(ctx.formParam("length"));

                // Opretter et Carport-objekt med brugerinfo fra sessionen
                Carport carport = new Carport(width, length, ctx.sessionAttribute("currentUser"));
                CarportMapper.addCarport(carport);
            }

        } catch (DatabaseException e) {
            throw new RuntimeException(e); // Fejl ved databaseoperationer kastes videre
        }
        ctx.render("createCarport.html"); // Viser oprettelses-siden igen efter indsættelse
    }

    // Fortolker carport-data for at oprette en ordre og tilknyttede materialer i databasen
    public static void interpretUserData(Carport carport) throws DatabaseException {

        // Opretter en ny ordre med status "Afventer betaling"
        Order order = new Order(LocalDate.now(), 0, "Afventer betaling", carport.getUser().getId(), carport, null);
        OrderMapper.addOrder(order);  // Tilføjer ordre til databasen
        order.setOrder_id(OrderMapper.getLatestOrderNr()); // Sætter ordre-id til den senest oprettede

        // Beregner nødvendige materialer baseret på carportens bredde
        List<Material> materials = Calculator.orderCalculator(carport.getWidth());
        List<OrderDetails> orderDetails = new ArrayList<>();

        int i = 0;
        for (Material material : materials) {
            // Hvis materialet er det samme som det forrige, opdateres mængden
            if (i > 0 && material.getMaterialId() == orderDetails.get(orderDetails.size() - 1).getMaterial().getMaterialId()) {
                orderDetails.get(orderDetails.size() - 1).setQuantity(orderDetails.get(orderDetails.size() - 1).getQuantity() + 1);
            }
            else {
                // Tilføjer det forrige ordrelinje til databasen, hvis det ikke er første iteration
                if (i > 0) {
                    OrderDetailMapper.addOrderDetail(
                            orderDetails.get(orderDetails.size() - 1).getOrderId(),
                            orderDetails.get(orderDetails.size() - 1).getMaterial(),
                            orderDetails.get(orderDetails.size() - 1).getQuantity()
                    );
                }
                // Tilføjer nyt ordrelinje til listen
                orderDetails.add(new OrderDetails(material, material.getAmount(), order.getOrder_id()));
            }
            i++;
        }
        // Tilføjer sidste ordrelinje til databasen
        OrderDetailMapper.addOrderDetail(
                orderDetails.get(orderDetails.size() - 1).getOrderId(),
                orderDetails.get(orderDetails.size() - 1).getMaterial(),
                orderDetails.get(orderDetails.size() - 1).getQuantity()
        );

        order.setOrderDetails(orderDetails);  // Sætter ordrelinjer på ordren
        order.priceSummation(); // Beregner samlet pris for ordren
        OrderMapper.updatePrice(order.getOrder_id(), order.getTotal_price()); // Opdaterer pris i databasen
    }

    // Admin-funktion til at opdatere ordrer for carporte uden tilknyttede ordrer
    public static void adminOrderUpdater(Context ctx) throws DatabaseException {
        List<Carport> carports = OrderMapper.getCarportsWithoutOrders(); // Henter carporte uden ordrer
        for (Carport carport : carports) {
            interpretUserData(carport);  // Opretter ordrer for hver carport
        }
        ctx.render("admin.html");  // Viser admin-siden efter opdatering
    }
}






