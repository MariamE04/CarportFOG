package app.persistence;

import app.entities.Material;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {
    private static ConnectionPool connectionPool;

    //Bruges til at hente materialelængder i Calculator
    public static List<Material> getMaterialsByLengths() throws DatabaseException {
        String sql = "SELECT * FROM materials ORDER BY length ASC";

        List<Material> materialsList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String unit = rs.getString("unit");
                int amount = rs.getInt("amount");
                int length = rs.getInt("length");
                int price = rs.getInt("price");

                materialsList.add(new Material(id, name, description, unit, amount, length, price));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente materialernes længder" + e.getMessage());
        }
        return materialsList;
    }

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

}
