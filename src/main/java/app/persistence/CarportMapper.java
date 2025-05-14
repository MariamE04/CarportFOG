package app.persistence;

import app.entities.Carport;
import app.entities.Quote;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarportMapper {

    //Vores ConnectionPool instans
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }


    public static void addWidthAndLength(Carport carport) throws DatabaseException {
        String sql = "INSERT INTO public.carports (carport_width, carport_length, carport_height, roof_type, shed_id)"+
                "VALUES(?,?,200,fladt,null)";


        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, carport.getWidth());
            ps.setInt(2, carport.getLength());

            ps.executeUpdate();

        }catch (SQLException e){
            throw new DatabaseException("Fejl med at indsætte carport mål", e.getMessage());
        }

    }

    public static Carport getCarportByQuoteId(int quoteId) {
        String sql = "SELECT c.carport_width, c.carport_length\n" +
                "FROM quotes q\n" +
                "JOIN orders o ON q.order_id = o.order_id\n" +
                "JOIN carports c ON o.carport_id = c.carport_id\n" +
                "WHERE q.quote_id = ?\n";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quoteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int width = rs.getInt("carport_width");
                int length = rs.getInt("carport_length");
                return new Carport(width, length);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
