package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.HomeController;
import app.controllers.OrderController;
import app.controllers.OrderDetailController;
import app.controllers.SvgController;
import app.persistence.ConnectionPool;
import app.persistence.OrderDetailMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
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


        // Routing
        app.get("/", ctx -> ctx.redirect("/index"));
        app.get("/index", ctx -> ctx.render("index.html"));

        //Rute til ordre
       // app.get("admin", ctx -> ctx.render("admin"));

        //Viser startsiden.
        app.get("startpage", ctx -> ctx.render("startpage.html"));

        // Rute til sign-up
        app.post("/signUp", ctx -> HomeController.signUpUser(ctx)); //POST: Opretter ny bruger.
        app.get("/signUp", ctx -> ctx.render("/signUp.html")); //GET: Viser formularen.

        // Rute til login
        app.post("/login", ctx -> HomeController.userLogIn(ctx)); //POST: Logger brugeren ind.

        app.get("/login", ctx -> ctx.render("index.html")); //Viser login-formularen (her: index.html).

        //app.get("/admin", ctx -> ctx.render("admin.html"));

        app.get("showOrder", ctx -> SvgController.showOrder(ctx));

        app.get("/admin", ctx -> {
            OrderController.getAllOrders(ctx);
        });

        //app.get("/orderdetails", ctx -> OrderDetailController.getOrderDetailsByOrderNumber(ctx));

        app.post("orderdetails", ctx -> OrderDetailController.getOrderDetailsByOrderNumber(ctx));
        app.get("orderdetails", ctx -> ctx.render("orderdetails"));
    }
}