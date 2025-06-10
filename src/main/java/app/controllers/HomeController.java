package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.UserMapper;
import app.util.PasswordUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.logging.Logger;

//HomeController er ansvarlig for at håndtere HTTP-forespørgsler relateret til brugerregistrering og login.
public class HomeController {
    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName()); //LOGGER bruges til at logge fejl og information.

    public static void addRoutes(Javalin app){
        // Rute til sign-up
        app.post("/signUp", ctx -> HomeController.signUpUser(ctx)); //POST: Opretter ny bruger.
        app.get("/signUp", ctx -> ctx.render("/signUp.html")); //GET: Viser formularen.

        // Rute til login
        app.post("/login", ctx -> HomeController.userLogIn(ctx)); //POST: Logger brugeren ind.
        app.get("/login", ctx -> ctx.render("login.html")); //Viser login-formularen (her: index.html).

        app.get("/logout", ctx -> {
            ctx.req().getSession().invalidate(); // Fjerner alle session data
            ctx.redirect("/login"); // Sender brugeren tilbage til login-siden
        });

    }

    //Håndterer brugerregistrering
    public static int signUpUser(Context ctx) throws DatabaseException { //håndtere HTTP-forespørgslen fra brugeren.
        try { //Henter brugerens input fra HTTP-formularen.
            String email = ctx.formParam("email"); // få adgang til brugerens input: e-mail
            String password = ctx.formParam("password");
            long phoneNumber = Long.parseLong(ctx.formParam("phoneNumber"));

            String phoneNumberStr = ctx.formParam("phoneNumber"); // Hent som String

            // Valider telefonnummer som String (8 cifre)
            if (!phoneNumberValidity(phoneNumberStr)) {
                ctx.attribute("message", "Telefonnummer skal være præcis 8 cifre.");
                ctx.render("signup.html");
                return 0;
            }

            // Valider password
            if (!passwordValidity(password)) {
                ctx.attribute("message", "Password skal være mellem 3 og 10 tegn.");
                ctx.render("signup.html");
                return 0;
            }

            //long phoneNumber1 = Long.parseLong(phoneNumberStr); // Konverter først efter validering

            User user = new User(email, password, phoneNumber); //Opretter et User-objekt med de indtastede oplysninger.
            boolean userExists = UserMapper.userExists(user); //Kalder userExists fra UserMapper for at tjekke, om brugeren allerede findes i databasen.

            if (userExists) { //Hvis brugeren allerede findes, sendes en fejlbesked, og signup.html vises.
                ctx.attribute("message", "Bruger findes allerede. Være vendlig at loge ind.");
                ctx.render("signup.html");
                return 0; // Indikerer at brugeren allerede findes

            } else { //Hvis brugeren ikke findes, forsøges sign-up via UserMapper.signUp.
                String hashedPassword = PasswordUtil.hashPassword(password);
                int result = UserMapper.signUp(email, hashedPassword, phoneNumber);


                if (result == 1) {
                    User newUser = new User(email, hashedPassword, phoneNumber); //En ny User oprettes
                    //huske, hvem der er logget ind.
                    ctx.sessionAttribute("currentUser", newUser); // Gemmer hele User-objektet i sessionen (currentUser).
                    ctx.attribute("message", "You have now been registered");
                    ctx.status(200).render("index.html"); // omdirigeres til startpage.html.
                    return 1; // Indikerer succesfuld oprettelse

                } else {
                    ctx.status(400).result("Sign-up failed.");
                    return -1; // Indikerer fejl ved sign-up
                }
            }
        } catch (Exception e) {
            ctx.status(500).result("An error occurred: " + e.getMessage()); //Hvis der opstår en fejl under sign-up, sendes en generisk fejlbesked.
            return -2; // Indikerer Intern serverfejl.
        }
    }

    //Håndterer brugerlogin
    public static void userLogIn(Context ctx) { //Modtager: ctx (HTTP-forespørgslen).

        //Henter email og password fra login-formularen.
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User loggedInUser = UserMapper.logIn(email);

            if (loggedInUser != null && PasswordUtil.checkPassword(password, loggedInUser.getPassword())) {
                ctx.sessionAttribute("currentUser", loggedInUser);
                if ("admin".equals(loggedInUser.getRole())) {
                    ctx.sessionAttribute("admin", loggedInUser);
                    ctx.redirect("admin");
                } else {
                    ctx.render("index.html");
                }
            } else {
                ctx.attribute("message", "Fejl i enten e-mail eller password. Prøv igen.");
                ctx.render("login.html");
            }

        } catch (DatabaseException e) { //Hvis der opstår en databasefejl under login, logges fejlen, og brugeren får en generisk fejlbesked.
            LOGGER.severe("Error during login: " + e.getMessage());
            ctx.status(500).result("Error during login.");
        }
    }

    // Tjekker om password er mellem 3 og 10 tegn
    private static boolean passwordValidity(String password) {
        if (password == null) return false;
        int length = password.length();
        return length >= 3 && length <= 10;
    }

    // Tjekker om telefonnummer er præcis 8 cifre og kun tal
    private static boolean phoneNumberValidity(String phoneNumber1) {
        if (phoneNumber1 == null) return false;
        return phoneNumber1.matches("\\d{8}"); //betyder: Returner true hvis phoneNumber1 indeholder præcis 8 cifre, ellers false.
        //Strengen skal bestå af præcis 8 cifre.
    }
}
