package app.controllers;

import app.util.CarportSvg;
import app.util.Svg;
import io.javalin.http.Context;

import java.util.Locale;

public class OrderController {

    public static void showOrder(Context ctx){
        Locale.setDefault(new Locale("US"));

        int width = Integer.parseInt(ctx.queryParam("width"));   // fx fra ?width=600
        int length = Integer.parseInt(ctx.queryParam("length")); // fx fra ?length=780

        CarportSvg svg = new CarportSvg(width, length);

        ctx.attribute("svg", svg.toString());
        ctx.render("showOrder.html");
    }

}

