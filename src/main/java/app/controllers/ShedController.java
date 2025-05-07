package app.controllers;

import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ShedMapper;
import io.javalin.http.Context;

import java.util.List;

public class ShedController {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void showShedWidthAndLength(Context ctx){
        try {
            List<Shed> shedWidthAndLength = ShedMapper.getShedWidthAndLength();
            ctx.attribute("shedWidthAndLength", shedWidthAndLength);
            ctx.render("createCarport.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
