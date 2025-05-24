package app.controllers;

import app.entities.Material;
import app.entities.OrderDetails;
import app.entities.Quote;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.OrderMapper;

import app.persistence.QuoteMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class QuoteController {

    public static void addRoutes(Javalin app){
        app.post("/addQuote", ctx -> QuoteController.addQuoteToDB(ctx));
        app.get("/quotes", QuoteController::getQuotesByUser);
        app.post("/quotes/{id}", QuoteController::respondToQuote);
        //app.get("/pay/{id}", ctx -> QuoteController.getOrderDetailsByOrderId(ctx));
    }

    //Henter tilbud for den aktuelle bruger baseret på sessionen
    public static void getQuotesByUser(Context ctx) {
        expirationDate();

        // Henter den aktuelle bruger fra sessionen.
        User user = ctx.sessionAttribute("currentUser");

        // Hvis brugeren ikke er logget ind, returneres en 401-fejl.
        if (user == null) {
            ctx.status(401).result("User not logged in");
            return;
        }

        try {
            // Henter alle tilbud for brugeren via email.
            List<Quote> quotes = QuoteMapper.getQuotesByEmail(user.getEmail());

            // Filtrér quotes, der ikke er synlige (fjerner tilbud der ikke er synlige for brugeren).
            Iterator<Quote> iterator = quotes.iterator(); // Opretter en iterator for at kunne fjerne elementer under iteration.
            while (iterator.hasNext()) {
                Quote quote = iterator.next(); // Henter næste tilbud.
                if (!quote.isVisible()) {     // Tjekker om tilbuddet ikke er synligt.
                    iterator.remove();       // Fjerner tilbuddet fra listen, hvis det ikke er synligt.
                }
            }

            // Sætter den filtrerede liste af tilbud som attribut i konteksten til visning på brugerens side.
            ctx.attribute("quotes", quotes);

            // bruges til at vise tilbuddene på brugerens side.
            ctx.render("quotes_user.html");

        } catch (DatabaseException e) {
            // Hvis der opstår en databasefejl, sættes en fejlbesked som attribut og kastes et runtime exception.
            ctx.attribute("message", "Fejl ved hentning af quotes til bruger: " + user.getEmail() + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // Behandler svar på tilbud (accept eller afvis).
    public static void respondToQuote(Context ctx){

        // Henter quoteId fra URL-stien (pathParam) og konverterer den til et heltal.
        int quoteId = Integer.parseInt(ctx.pathParam("id"));

        // Henter brugerens svar (accept eller reject) fra formularen.
        String response = ctx.formParam("response"); // "accept" eller "reject"

        try {
            //accepterer tilbuddet: opdateres quote som accepteret i databasen
            if("accept".equals(response)){
                QuoteMapper.updateQuoteAccepted(quoteId, true);
                QuoteMapper.updateQuoteVisibility(quoteId, true);

                // Opdater ordre status til godkendt
                OrderMapper.updateOrderStatusByQuoteId(quoteId, "Godkendt");

                // Redirect til betalingsside
                ctx.redirect("/pay/" + quoteId);
                return; // sørg for at vi ikke også kører redirect nedenfor

            // brugeren afviser tilbuddet: opdateres quote som usynligt.
            } else if("reject".equals(response)) {
                QuoteMapper.updateQuoteVisibility(quoteId,false );

                // Opdater ordre status til afvist
                OrderMapper.updateOrderStatusByQuoteId(quoteId, "Afvist");

            }

        } catch (DatabaseException e) {
            e.printStackTrace();
            ctx.status(500).result("Fejl i updateQuoteStatus: " + e.getMessage());
        }

        // Efter at have opdateret, sendes brugeren tilbage til listen af tilbud.
        ctx.redirect("/quotes"); // flyt udenfor try-catch så det kører uanset hvad
    }

    public static void expirationDate() {
        try {
            List<Quote> allQuotes = QuoteMapper.getAllQuotes();

            for (Quote quote : allQuotes) {
                if (quote.isVisible() && !quote.isAccepted()
                        && quote.getDateCreated().plusDays(14).isBefore(LocalDate.now())) {
                    QuoteMapper.updateQuoteVisibility(quote.getQuoteId(), false);
                    OrderMapper.updateOrderStatusByQuoteId(quote.getQuoteId(), "Udløbet");
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Fejl i expirationDate: " + e.getMessage());
        }
    }

    public static void addQuoteToDB(Context ctx) throws DatabaseException{
        int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));


        double price = OrderMapper.getPrice(orderNumber);


        //List<OrderDetails> orderDetails= OrderDetailMapper.getOrderDetailsByOrder(orderNumber);


        LocalDate validityPeriod = LocalDate.now().plusDays(14);

        Quote quote = new Quote(validityPeriod, price, orderNumber);
        QuoteMapper.addQuote(orderNumber, quote);

        ctx.attribute("quote", quote);
        ctx.redirect("/admin");
    }

public static void getOrderDetailsByOrderId(Context ctx) throws DatabaseException{
    int quoteId = Integer.parseInt(ctx.pathParam("id"));
    Quote quote = QuoteMapper.getQuoteById(quoteId);

    // Tjek om tilbuddet er accepteret
    if (!quote.isAccepted()) {
        ctx.render("quote_not_accepted.html"); // vi antager du har lavet en route til denne side
        return;
    }

    List<OrderDetails> orderDetails= QuoteMapper.getOrderDetailsByQuoteId(quoteId);
    ctx.attribute("orderDetails", orderDetails);
    ctx.render("pay_quote.html");

}

    /*public static void getQuoteByOrderAndUser(Context ctx) throws DatabaseException{

        //int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));

        User currentUser = ctx.sessionAttribute("currentUser");
        int userId = currentUser.getId();

        List<Quote> quotesList=  QuoteMapper.getQuoteByOrder(userId);

        ctx.attribute("quotesList", quotesList);
        ctx.render("quotes_user.html");

    }*/
}