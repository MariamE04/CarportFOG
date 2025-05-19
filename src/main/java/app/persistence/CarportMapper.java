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
        String sql = "INSERT INTO public.carports (carport_width, carport_length, roof_type, shed_id, user_id)"+
                "VALUES(?,?,'fladt',null,?)";


        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, carport.getWidth());
            ps.setInt(2, carport.getLength());
            ps.setInt(3, carport.getUser().getId());

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

    public static Carport getCarportById(int carportId) throws DatabaseException{
        Carport carport = null;

        String sql = "select * from public.carports where carport_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, carportId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int width = rs.getInt("carport_width");
                int length = rs.getInt("carport_length");
                String roofType = rs.getString("roof_type");
                int id = rs.getInt("carport_id");
                //Tilføj shed her hvis skur skal med senere

                 carport = new Carport(width, length, roofType, id);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl ved hentning af carport med id = " + carportId, e.getMessage());
        }
        return carport;

    }

    public static void updateCarport(int width, int length, int carportId) throws DatabaseException{
        String sql = "update public.carports set carport_width = ?, carport_length = ? where carport_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, width);
            ps.setInt(2, length);
            ps.setInt(3, carportId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl i opdatering af carport");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl i opdatering af carports mål", e.getMessage());
        }
    }


    public static void getCarportIdByOrderId(int order_id){
        String sql = "select carports.carport_id  orders ON carports.carport_id = orders.carport_id where carport_id = ?";
    }

    }
