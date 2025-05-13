package app.persistence;

import app.entities.Carport;
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

}
