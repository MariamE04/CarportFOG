package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.logging.Logger;

//HomeController er ansvarlig for at håndtere HTTP-forespørgsler relateret til brugerregistrering og login.
public class HomeController {
    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName()); //LOGGER bruges til at logge fejl og information.
    private static ConnectionPool connectionPool; //connectionPool holder en statisk reference til en databaseforbindelses-pulje.

    //setConnectionPool gør det muligt at sætte connectionPool fra en anden del af programmet.
    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    //Håndterer brugerregistrering
    public static int signUpUser(Context ctx) throws DatabaseException { //håndtere HTTP-forespørgslen fra brugeren.
        try { //Henter brugerens input fra HTTP-formularen.
            String email = ctx.formParam("email"); // få adgang til brugerens input: e-mail
            String password = ctx.formParam("password");
            long phoneNumber = Long.parseLong(ctx.formParam("phoneNumber"));

            User user = new User(email, password, phoneNumber); //Opretter et User-objekt med de indtastede oplysninger.
            boolean userExists = UserMapper.userExists(user); //Kalder userExists fra UserMapper for at tjekke, om brugeren allerede findes i databasen.


            if (userExists) { //Hvis brugeren allerede findes, sendes en fejlbesked, og signup.html vises.
                ctx.attribute("message", "Bruger findes allerede. Være vendlig at loge ind.");
                ctx.render("signup.html");
                return 0; // Indikerer at brugeren allerede findes

            } else { //Hvis brugeren ikke findes, forsøges sign-up via UserMapper.signUp.
                int result = UserMapper.signUp(email, password,phoneNumber);

                if (result == 1) {
                    User newUser = new User(email, password); //En ny User oprettes
                    //huske, hvem der er logget ind.
                    ctx.sessionAttribute("currentUser", newUser); // Gemmer hele User-objektet i sessionen (currentUser).
                    ctx.attribute("message", "You have now been registered");
                    ctx.status(200).render("startpage.html"); // omdirigeres til startpage.html.
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
       // String role = ctx.attribute("role");

        try {
            User loggedInUser = UserMapper.logIn(email, password);
            //Kalder logIn-metoden i UserMapper, der returnerer en bruger, hvis login-oplysningerne er korrekte.

            if (loggedInUser != null) { //Hvis login er korrekt
                ctx.sessionAttribute("currentUser", loggedInUser); //gemmes brugeren i sessionen som currentUser.

                //Hvis brugeren er administrator, gemmes en admin-session og omdirigeres til admin-siden.
                if ("admin".equals(loggedInUser.getRole())) {
                    ctx.sessionAttribute("admin", loggedInUser);
                    //System.out.println("TESTSTST DER VIRKER");
                    ctx.redirect("admin");

                } else { //Hvis det er en almindelig bruger, vises startpage.html.
                    ctx.render("startpage.html");
                }
            } else { //Hvis brugeren ikke findes, vises en fejlbesked på index.html.
                ctx.attribute("message", "Fejl i enten e-mail eller password. Prøv igen.");
                ctx.render("login.html");
            }
        } catch (DatabaseException e) { //Hvis der opstår en databasefejl under login, logges fejlen, og brugeren får en generisk fejlbesked.
            LOGGER.severe("Error during login: " + e.getMessage());
            ctx.status(500).result("Error during login.");
        }
    }
}
