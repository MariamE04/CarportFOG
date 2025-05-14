package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
import app.persistence.*;

import app.util.Calculator;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "bvf64wwa";
    private static final String URL = "jdbc:postgresql://142.93.174.150:5432/%s?currentSchema=public";
    private static final String DB = "carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    public static void main(String[] args){

        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");

            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        HomeController.setConnectionPool(connectionPool);
        UserMapper.setConnectionPool(connectionPool);
        OrderMapper.setConnectionPool(connectionPool);
        OrderDetailMapper.setConnectionPool(connectionPool);
        OrderDetailController.setConnectionPool(connectionPool);

        CarportController.setConnectionPool(connectionPool);
        CarportMapper.setConnectionPool(connectionPool);
        ShedController.setConnectionPool(connectionPool);
        ShedMapper.setConnectionPool(connectionPool);
        MaterialMapper.setConnectionPool(connectionPool);

        QuoteMapper.setConnectionPool(connectionPool);
        QuoteController.setConnectionPool(connectionPool);



        // Routing
        app.get("/", ctx -> ctx.redirect("/index"));
        app.get("/index", ctx -> ctx.render("index.html"));

        //Rute til ordre
        //app.get("admin", ctx -> ctx.render("admin"));

        //Viser startsiden.
        app.get("startpage", ctx -> ctx.render("startpage.html"));

        // Rute til sign-up
        app.post("/signUp", ctx -> HomeController.signUpUser(ctx)); //POST: Opretter ny bruger.
        app.get("/signUp", ctx -> ctx.render("/signUp.html")); //GET: Viser formularen.

        // Rute til login
        app.post("/login", ctx -> HomeController.userLogIn(ctx)); //POST: Logger brugeren ind.
        app.get("/login", ctx -> ctx.render("login.html")); //Viser login-formularen (her: index.html).


        app.get("showOrder", ctx -> SvgController.showOrder(ctx));

        app.get("/admin", ctx -> {
            OrderController.getAllOrders(ctx);
        });

        app.post("orderdetails", ctx -> OrderDetailController.getOrderDetailsByOrderNumber(ctx));
        app.get("orderdetails", ctx -> ctx.render("orderdetails"));


        app.get("/quotes", QuoteController::getQuotesByUser);
        app.post("/quotes/{id}", QuoteController::respondToQute);

        app.get("/pay/{id}", QuoteController::showPaymentPage);

        // Ruter for at vise ordren og betale for carport
        //app.get("/pay/{id}", SvgController::showOrder); // Rute til at vise og generere ordren


        app.get("/pdf/{filename}", ctx -> {
            String filename = ctx.pathParam("filename");
            ctx.redirect("/public/pdf/" + filename);
        });


        // Rute til createCarport
        app.get("createCarport", ctx ->{
            CarportController.showWidthAndLength(ctx);
            ShedController.showShedWidthAndLength(ctx);
        });

        app.post("createCarport", ctx ->{
            CarportController.sendOrder(ctx);
        });
    }
}