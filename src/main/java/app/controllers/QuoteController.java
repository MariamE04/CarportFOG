package app.controllers;

import app.entities.Quote;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.QuoteMapper;
import io.javalin.http.Context;

import java.util.List;
import java.util.logging.Logger;

public class QuoteController {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName()); //LOGGER bruges til at logge fejl og information.
    private static ConnectionPool connectionPool; //connectionPool holder en statisk reference til en databaseforbindelses-pulje.

    //setConnectionPool gør det muligt at sætte connectionPool fra en anden del af programmet.
    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }


    /*public static void getAllQuotes(Context ctx) throws DatabaseException {
        List<Quote> quoteList = QuoteMapper.getAllQuets();

        ctx.attribute("quotes", quoteList);
        ctx.render("user");
    }*/

      public static void getQuotesByUser(Context ctx){
        User user = ctx.sessionAttribute("currentUser");

          if (user == null) {
              ctx.status(401).result("User not logged in");
              return;
          }

        try {
            List<Quote> qoutes = QuoteMapper.getQuotesByEmail(user.getEmail());
            ctx.attribute("quotes", qoutes);
            ctx.render("quotes_user.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl ved hentning af qoutes til bruger: " + user.getEmail() + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void respondToQute(Context ctx){
        int quoteId = Integer.parseInt(ctx.pathParam("id"));
        String response = ctx.formParam("response"); // "accept" eller "reject"

        try{
            if("accept".equals(response)){
                QuoteMapper.updateQuoteAccepted(quoteId, true);
            } else if("reject".equals(response)) {
                QuoteMapper.deleteQuote(quoteId);
            }
            ctx.redirect("/quotes");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Fejl ved opdatering af tilbud: " + e.getMessage());
        }

    }

}
