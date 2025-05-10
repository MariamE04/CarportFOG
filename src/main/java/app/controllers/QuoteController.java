package app.controllers;

import app.entities.Quote;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.QuoteMapper;
import io.javalin.http.Context;

import java.util.List;

public class QuoteController {

    private static ConnectionPool connectionPool; //connectionPool holder en statisk reference til en databaseforbindelses-pulje.

    //setConnectionPool gør det muligt at sætte connectionPool fra en anden del af programmet.
    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }


    public static void getQuotesByUser(Context ctx) {
        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.status(401).result("User not logged in");
            return;
        }

        try {
            List<Quote> quotes = QuoteMapper.getQuotesByEmail(user.getEmail());
            // Filtrér quotes, der ikke er synlige
            quotes.removeIf(quote -> !quote.isVisible());  // Fjern tilbud, der ikke er synlige
            ctx.attribute("quotes", quotes);
            ctx.render("quotes_user.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl ved hentning af quotes til bruger: " + user.getEmail() + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static void respondToQute(Context ctx){
        int quoteId = Integer.parseInt(ctx.pathParam("id"));
        String response = ctx.formParam("response"); // "accept" eller "reject"

        try {
            if("accept".equals(response)){
                QuoteMapper.updateQuoteAccepted(quoteId, true);
            } else if("reject".equals(response)) {
                QuoteMapper.updateQuoteVisibility(quoteId,false );
            }

        } catch (DatabaseException e) {
            e.printStackTrace();
            ctx.status(500).result("Fejl i updateQuoteStatus: " + e.getMessage());
        }

        ctx.redirect("/quotes"); // flyt udenfor try-catch så det kører uanset hvad
    }


}
