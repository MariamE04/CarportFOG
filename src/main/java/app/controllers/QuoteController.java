package app.controllers;

import app.entities.OrderDetails;
import app.entities.Quote;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.OrderMapper;

import app.persistence.QuoteMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;


public class QuoteController {

    public static void addRoutes(Javalin app){
        app.post("/addQuote", ctx -> QuoteController.addQuoteToDB(ctx)); // Opretter et tilbud i databasen
        app.get("/quotes", QuoteController::getQuotesByUser);    // Henter tilbud for den loggede bruger
        app.post("/quotes/{id}", QuoteController::respondToQuote); // Behandler accept/afvisning af tilbud
    }

    //Henter tilbud for den aktuelle bruger baseret på sessionen
    public static void getQuotesByUser(Context ctx) {
        expirationDate(); // Tjekker for udløbne tilbud og opdaterer deres synlighed.

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

            // Sætter den filtrerede liste som attribut og viser dem i brugergrænsefladen.
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
                QuoteMapper.updateOrderStatusByQuoteId(quoteId, "Godkendt");

                // Redirect til betalingsside
                ctx.redirect("/pay/" + quoteId);
                return; // sørg for at vi ikke også kører redirect nedenfor

            // brugeren afviser tilbuddet: opdateres quote som usynligt.
            } else if("reject".equals(response)) {
                QuoteMapper.updateQuoteVisibility(quoteId,false );

                // Opdater ordre status til afvist
                QuoteMapper.updateOrderStatusByQuoteId(quoteId, "Afvist");

            }

        } catch (DatabaseException e) {
            e.printStackTrace();
            ctx.status(500).result("Fejl i updateQuoteStatus: " + e.getMessage());
        }

        // Efter at have opdateret, sendes brugeren tilbage til listen af tilbud.
        ctx.redirect("/quotes"); // flyt udenfor try-catch så det kører uanset hvad
    }

    // Tjekker og håndterer udløb af tilbud efter 14 dage, så de bliver usynlige og markeret som "Udløbet".
    public static void expirationDate() {
        try {
            List<Quote> allQuotes = QuoteMapper.getAllQuotes();

            for (Quote quote : allQuotes) {
                if (quote.isVisible() && !quote.isAccepted()
                        && quote.getDateCreated().plusDays(14).isBefore(LocalDate.now())) {
                    QuoteMapper.updateQuoteVisibility(quote.getQuoteId(), false);
                    QuoteMapper.updateOrderStatusByQuoteId(quote.getQuoteId(), "Udløbet");
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Fejl i expirationDate: " + e.getMessage());
        }
    }

    // Tilføjer et nyt tilbud til databasen baseret på ordre-nummer fra formularen
    public static void addQuoteToDB(Context ctx) throws DatabaseException{
        // Hent ordre-nummer fra formularen som et heltal
        int orderNumber = Integer.parseInt(ctx.formParam("orderNumber"));

        // Hent prisen på ordren fra databasen via OrderMapper
        double price = OrderMapper.getPrice(orderNumber);

        // Sæt gyldighedsperiode til 14 dage frem fra dags dato
        LocalDate validityPeriod = LocalDate.now().plusDays(14);

        // Opret et nyt Quote-objekt med gyldighedsperiode, pris og ordre-nummer
        Quote quote = new Quote(validityPeriod, price, orderNumber);

        // Tilføj tilbuddet til databasen gennem QuoteMapper
        QuoteMapper.addQuote(orderNumber, quote);

        // Sæt tilbuddet som en attribut i HTTP-konteksten
        ctx.attribute("quote", quote);

        // Redirect brugeren til admin-siden efter tilføjelse af tilbud
        ctx.redirect("/admin");
    }

    // Henter ordredetaljer for et tilbud, hvis tilbuddet er accepteret.
    public static void getOrderDetailsByOrderId(Context ctx) throws DatabaseException{
        int quoteId = Integer.parseInt(ctx.pathParam("id"));
        Quote quote = QuoteMapper.getQuoteById(quoteId);

        // Tjek om tilbuddet er accepteret
        if (!quote.isAccepted()) {
        ctx.render("quote_not_accepted.html");
        return;
    }

        List<OrderDetails> orderDetails= QuoteMapper.getOrderDetailsByQuoteId(quoteId);
        ctx.attribute("orderDetails", orderDetails);
        ctx.render("pay_quote.html");
    }
}