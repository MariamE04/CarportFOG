package app.controllers;

import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.util.List;

public class CarportController {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void showWidthAndLength(Context ctx){
        try {
            List<Carport> widthAndLength = CarportMapper.getWidthAndLength();
            ctx.attribute("widthAndLength", widthAndLength);
            ctx.render("createcupcake.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

}
