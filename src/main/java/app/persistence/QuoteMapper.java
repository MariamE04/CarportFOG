package app.persistence;

import app.entities.Material;
import app.entities.Order;
import app.entities.OrderDetails;
import app.entities.Quote;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuoteMapper {
    // Static reference til connection pool, som bruges til at hente forbindelse til databasen.
    private static ConnectionPool connectionPool;

    // Sætter connectionPool fra en ekstern kilde.
    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool; // Tildeler den nye connectionPool til den statiske variabel.
    }

    // Henter alle tilbud (quotes) for en bruger baseret på deres email.
    public static List<Quote> getQuotesByEmail(String email) throws DatabaseException {
        String sql = """
       SELECT q.quote_id, q.final_price, q.valid_until_date, q.created_at_date, q.is_accepted, q.is_visible
       FROM quotes q
       JOIN orders o ON q.order_id = o.order_id
       JOIN carports c ON o.carport_id = c.carport_id
       JOIN users u ON c.user_id = u.user_id
       WHERE u.email = ?;
       """;

        // Opretter en tom liste, som vil indeholde alle de hentede quotes.
        List<Quote> quoteList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();  // Henter en databaseforbindelse fra connection pool.
             PreparedStatement ps = connection.prepareStatement(sql)) { // Forbereder SQL-forespørgslen.

            ps.setString(1, email);  // Sætter email-parameteren i SQL-forespørgslen.
            ResultSet rs = ps.executeQuery();   // Udfører forespørgslen og gemmer resultatet i ResultSet.

            while (rs.next()) {
                int quoteId = rs.getInt("quote_id"); // Henter quote_id fra resultatet.
                LocalDate validityPeriod = rs.getDate("valid_until_date").toLocalDate();
                double price= rs.getDouble("final_price");
                LocalDate createdAt = rs.getDate("created_at_date").toLocalDate();
                boolean isAccepted = rs.getBoolean("is_accepted");
                boolean isVisible = rs.getBoolean("is_visible");

                // Opretter et Quote-objekt og tilføjer det til listen.
                quoteList.add(new Quote(quoteId,validityPeriod, price, createdAt, isAccepted, isVisible));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i at hente tilbud for bruger med email '" + email + "': " + e.getMessage());
        }

        // Returnerer den liste af tilbud, der er hentet.
        return quoteList;
    }


    // Opdaterer status på et tilbud og markerer det som accepteret (is_accepted).
    public static void updateQuoteAccepted(int quoteId, boolean accepted) throws DatabaseException {

        // SQL-forespørgsel til at opdatere is_accepted for et tilbud.
        String sql = "UPDATE quotes SET is_accepted = ? WHERE quote_id = ?";

        try (Connection connection = connectionPool.getConnection(); // Henter en forbindelse fra connection pool.
             PreparedStatement ps = connection.prepareStatement(sql)) { // Forbereder SQL-forespørgslen.

            ps.setBoolean(1, accepted);  // Sætter den nye værdi for is_accepted.
            ps.setInt(2, quoteId);   // Sætter quote_id i forespørgslen.

            ps.executeUpdate(); // Udfører opdateringen.

        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke opdatere quote: " + e.getMessage());
        }
    }

        // Opdaterer synligheden af et tilbud (is_visible).
        public static void updateQuoteVisibility(int quoteId, boolean isVisible) throws DatabaseException {

            // SQL-forespørgsel til at opdatere is_visible for et tilbud.
            String sql = "UPDATE quotes SET is_visible = ? WHERE quote_id = ?";

            try (Connection connection = connectionPool.getConnection(); // Henter en forbindelse fra connection pool.
                 PreparedStatement ps = connection.prepareStatement(sql)) { // Forbereder SQL-forespørgslen.

                ps.setBoolean(1, isVisible); // Sætter den nye værdi for is_visible.
                ps.setInt(2, quoteId);  // Sætter quote_id i forespørgslen.

                ps.executeUpdate(); // Udfører opdateringen.

            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke opdatere is_visible for quote: " + e.getMessage());
            }
        }

    public static List<Quote> getAllQuotes() throws DatabaseException {
        String sql = "SELECT * FROM quotes";
        List<Quote> quoteList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int quoteId = rs.getInt("quote_id");
                LocalDate validityPeriod = rs.getDate("valid_until_date").toLocalDate();
                double price = rs.getDouble("final_price");
                LocalDate createdAt = rs.getDate("created_at_date").toLocalDate();
                boolean isAccepted = rs.getBoolean("is_accepted");
                boolean isVisible = rs.getBoolean("is_visible");

                quoteList.add(new Quote(quoteId, validityPeriod, price, createdAt, isAccepted, isVisible));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i getAllQuotes: " + e.getMessage());
        }

        return quoteList;
    }

    public static void addQuote(int orderId, Quote quote) throws DatabaseException{
        String sql = "INSERT INTO quotes (final_price, valid_until_date, created_at_date, is_accepted, is_visible, order_id)" +
                "VALUES (?,?,?,?,?,?)";

        try(Connection connection = connectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setDouble(1, quote.getPrice());
            ps.setDate(2, Date.valueOf(quote.getValidityPeriod()));
            ps.setDate(3, Date.valueOf(quote.getDateCreated()));
            ps.setBoolean(4, quote.isAccepted());
            ps.setBoolean(5, quote.isVisible());
            ps.setInt(6, orderId);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Fejl med at tilføje tilbud til databasen", e.getMessage());
        }
    }
  
    public static Quote getQuoteById(int id) throws DatabaseException {
        Quote quote = null;

        String sql = "SELECT * FROM quotes WHERE quote_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean isVisible = rs.getBoolean("is_visible");
                boolean isAccepted = rs.getBoolean("is_accepted");
                quote = new Quote(id, isVisible,isAccepted);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting quote by id", e.getMessage());
        }

        return quote;
    }

    public static List<OrderDetails> getOrderDetailsByQuoteId(int quoteId) throws DatabaseException{
        List<OrderDetails> orderDetails = new ArrayList<>();
        String sql ="SELECT * FROM orderdetails JOIN materials ON orderdetails.material_id = materials.material_id\n" +
                "JOIN quotes ON orderdetails.order_id = quotes.order_id WHERE quote_id = ?";

        try(Connection connection = connectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, quoteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int quantity = rs.getInt("quantity");

                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String unit = rs.getString("unit");
                int amount = rs.getInt("amount");
                int length = rs.getInt("length");
                int price = rs.getInt("price");
                int orderDetailId = rs.getInt("order_detail_id");
                int orderId = rs.getInt("order_id");

                Material material = new Material(materialId, name, description, unit, amount, length, price);

                orderDetails.add(new OrderDetails(material, quantity, orderId, orderDetailId));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl ved at hente ordre ud fra quote-id", e.getMessage());

        }
        return orderDetails;
    }

}
