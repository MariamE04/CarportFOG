package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
import app.persistence.*;

import app.util.FileUtil;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.apache.commons.io.output.QueueOutputStream;


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
            config.staticFiles.add("/public"); // where it is in your resources
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7071);

        UserMapper.setConnectionPool(connectionPool);
        OrderMapper.setConnectionPool(connectionPool);
        OrderDetailMapper.setConnectionPool(connectionPool);
        CarportMapper.setConnectionPool(connectionPool);
        MaterialMapper.setConnectionPool(connectionPool);
        QuoteMapper.setConnectionPool(connectionPool);

        // Routing
        app.get("/", ctx -> ctx.redirect("/index"));
        app.get("/index", ctx -> ctx.render("index.html"));
        HomeController.addRoutes(app);
        SvgController.addRoutes(app);
        CarportController.addRoutes(app);
        QuoteController.addRoutes(app);
        OrderController.addRoutes(app);
        OrderDetailController.addRoutes(app);
    }
}