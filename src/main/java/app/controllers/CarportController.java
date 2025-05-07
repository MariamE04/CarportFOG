package app.controllers;

import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class CarportController {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }



    public static void showWidthAndLength(Context ctx){

            List<Carport> widthAndLength = new ArrayList<>();
            int i = 240;
            while(i<=780){
                widthAndLength.add(new Carport(i,i));
                i = i+60;
            }

            ctx.attribute("widthAndLength", widthAndLength);
            ctx.render("createCarport.html");

        }




}
