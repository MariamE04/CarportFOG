package app.persistence;


import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Klassen UserMapper bruges til at håndtere databaseoperationer relateret til brugere.
public class UserMapper {

    //Dependency injection
    //Gør det muligt at sætte forbindelsen udefra (fx fra Main),
    // så UserMapper kan bruge den til at snakke med databasen.

    private static ConnectionPool connectionPool; //Privat statisk attribut, som holder en forbindelse til databasen.

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    //Statisk metode, der forsøger at tilføje en ny bruger til databasen.
    public static int signUp(String email, String password, long phoneNumber) throws DatabaseException { //Statisk metode, så den kan kaldes uden at instantiere CupCakeMapper.
        User user = new User(email, password, phoneNumber); //Opretter et User-objekt med de indtastede oplysninger.

        // forsøger at indsætte en bruger, men hvis email allerede findes, gør den ingenting.
        String sql = "INSERT INTO users (email, password, phone_number) VALUES (?,?,?) ON CONFLICT (email) DO NOtHING";

        try (
                Connection connection = connectionPool.getConnection(); //henter en forbindelse til databasen.
                PreparedStatement ps = connection.prepareStatement(sql); //indsætte værdier sikkert.
        ) {
            //Disse erstatter ? i SQL'en.
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setLong(3,user.getPhoneNumber());

            //Eksekverer indsættelsen og returnerer hvor mange rækker, der blev ændret (0 eller 1).
            int rowsAffected = ps.executeUpdate(); //kører INSERT-sætningen
            return rowsAffected; //returnerer antal rækker der blev oprettet

        } catch (SQLException e) { //kastet med en fejlbesked.
            String msg = "There was an error during your sign-up for the workout log. Please try again.";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    //Opretter en bruger med indtastede loginoplysninger.
    public static User logIn(String email, String password) throws DatabaseException { //Statisk metode, så den kan kaldes uden at instantiere CupCakeMapper.
        User user = new User(email, password);

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?"; //SQL-sætning til at finde en bruger, hvis både email og kodeord passer.

        try ( //Forbinder til databasen, sætter parametre og udfører forespørgslen.
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery(); //eksekverer SELECT-sætningen og får et ResultSet


            if (rs.next()) { //returnerer true, hvis der er en række (bruger findes).
                return user;
            } else {
                return null; // Forkert email eller kode -> returner null
            }

        } catch (SQLException e) { //Ved fejl kastes en DatabaseException.
            throw new DatabaseException("Login-error: try again", e.getMessage());
        }
    }


    public static boolean userExists(User user) throws DatabaseException {
        String sql = "SELECT 1 FROM users WHERE email = ?"; //Tjekker om en bruger med den angivne email allerede findes.

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Returnerer true hvis brugeren findes, ellers false

        } catch (SQLException e) {
            throw new DatabaseException("Error checking if user exists.", e.getMessage());
        }
    }


   /* public static ArrayList<User> getAllUsers(User user) throws DatabaseException {

        ArrayList<User> allUsers = new ArrayList<>();
        String sql = "SELECT * FROM public.users";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                   String email = rs.getString("email");
                    String password = rs.getString("password");
                    int balance = rs.getInt("balance");

                    allUsers.add(new User(email, password));
                }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl, kunne ikke hente alle kunderne |"+e.getMessage());
        }
        return allUsers;
    }*/

}
