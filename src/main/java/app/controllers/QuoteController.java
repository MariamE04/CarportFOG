package app.controllers;

import app.entities.Quote;
import app.exceptions.DatabaseException;
import app.persistence.QuoteMapper;
import io.javalin.http.Context;

import java.util.List;

public class QuoteController {
    public static void getAllQuotes(Context ctx) throws DatabaseException {
        List<Quote> quoteList = QuoteMapper.getAllQuets();

        ctx.attribute("quotes", quoteList);
        ctx.render("user");
    }
}
