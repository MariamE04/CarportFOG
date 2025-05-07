package app.persistence;

import app.entities.Quote;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuoteMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public static List<Quote> getAllQuets() throws DatabaseException {
        String sql = "SELECT * FROM quotes";
        List<Quote> quoteList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int id =rs.getInt("quote_id");
                LocalDate validDate = LocalDate.parse(rs.getString("created_at_date"));
                double finalPrice = rs.getDouble("final_price");
                boolean isAccepted = rs.getBoolean("is_accepted");

                quoteList.add(new Quote(id,validDate,finalPrice, isAccepted));

            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i at hente alle tilbud" + e.getMessage());
        }
    }
}
