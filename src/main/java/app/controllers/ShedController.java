package app.controllers;

import app.entities.Shed;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ShedMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class ShedController {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void showShedWidthAndLength(Context ctx){

            List<Shed> shedWidthAndLength = new ArrayList<>();
            int i = 240;
            while(i<=780){
                shedWidthAndLength.add(new Shed(i,i));
                i = i+60;
            }

            ctx.attribute("shedWidthAndLength", shedWidthAndLength);
            ctx.render("createCarport.html");


    }
}
