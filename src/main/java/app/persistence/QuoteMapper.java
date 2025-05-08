package app.persistence;

import app.entities.Order;
import app.entities.Quote;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuoteMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }


    public static List<Quote> getQuotesByEmail(String email) throws DatabaseException {
        String sql = """
        SELECT q.quote_id, q.final_price, q.valid_until_date, q.created_at_date, q.is_accepted
        FROM quotes q
        JOIN orders o ON q.quote_id = o.quote_id
        JOIN users u ON o.user_id = u.user_id
        WHERE u.email = ?;
        """;

        List<Quote> quoteList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("quote_id");
                LocalDate validUntil = rs.getDate("valid_until_date").toLocalDate();
                double finalPrice = rs.getDouble("final_price");
                LocalDate createdAt = rs.getDate("created_at_date").toLocalDate();
                boolean isAccepted = rs.getBoolean("is_accepted");

                quoteList.add(new Quote(id,validUntil, finalPrice, createdAt, isAccepted));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i at hente tilbud for bruger med email '" + email + "': " + e.getMessage());
        }

        return quoteList;
    }


    public static List<Quote> getAllQuets() throws DatabaseException {
        String sql = "SELECT * FROM quotes";
        List<Quote> quoteList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("quote_id");
                LocalDate validDate = LocalDate.parse(rs.getString("created_at_date"));
                double finalPrice = rs.getDouble("final_price");
                boolean isAccepted = rs.getBoolean("is_accepted");

                quoteList.add(new Quote(id, validDate, finalPrice, isAccepted));

            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i at hente alle tilbud" + e.getMessage());
        }
        return quoteList;
    }


    public static void updateQuoteAccepted(int quoteId, boolean accepted) throws DatabaseException {
        String sql = "UPDATE quotes SET is_accepted = ? WHERE quote_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, accepted);
            ps.setInt(2, quoteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke opdatere quote: " + e.getMessage());
        }
    }

    public static void deleteQuote(int quoteId) throws DatabaseException {
        String sql = "DELETE FROM quotes WHERE quote_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quoteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke slette quote: " + e.getMessage());
        }
    }


}
