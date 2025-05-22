package app.persistence;

import app.entities.Carport;
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

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    //Bruges til at hente materialelængder i Calculator
    public static List<Material> getBeamsByLengths() throws DatabaseException {
        String sql = "SELECT * FROM materials WHERE material_id < 4 ORDER BY length ASC";

        List<Material> materialsList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String unit = rs.getString("unit");
                int amount = rs.getInt("amount");
                int length = rs.getInt("length");
                int price = rs.getInt("price");

                materialsList.add(new Material(id, name, description, unit, amount, length, price));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i at hente materialernes længder" + e.getMessage());
        }
        return materialsList;
    }

    public static ArrayList<String> getAllLengthsAndNames() {
        ArrayList<String> getAllLengthsFromMaterials = new ArrayList<>();
        int materialLength;
        String materialName;

        String sql = "SELECT length, name FROM materials";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                materialLength = rs.getInt("length");
                materialName = rs.getString("name");

                String total = materialLength + ":" + materialName;

                getAllLengthsFromMaterials.add(total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return getAllLengthsFromMaterials;
    }

    public static int getMaterialIdByChosenLengthAndName(int materialLength, String materialName) throws DatabaseException {
        int materialId;

        String sql = "select material_id from materials where length = ? and name = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, materialLength);
            ps.setString(2, materialName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                materialId = rs.getInt("material_id");
                return materialId;
            }
            throw new DatabaseException("Fejl i opdatering af ordre-detalje");

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i opdatering af ordredetaljens material_id", e.getMessage());
        }
    }

    public static void updateMaterialId(int materialId, int orderDetailId) throws DatabaseException {
        String sql = "update orderdetails set material_id = ? where order_detail_id = ? ";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, materialId);
            ps.setInt(2, orderDetailId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl - Ingen rækker blev opdateret...");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i opdatering af ordredetaljens material_id", e.getMessage());
        }
    }

    //Bruges til at hente stolper
    public static Material getPost() throws DatabaseException {
        String sql = "SELECT * FROM materials WHERE name = '97x97 mm. trykimp. Stolpe'";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String unit = rs.getString("unit");
                int amount = rs.getInt("amount");
                int length = rs.getInt("length");
                int price = rs.getInt("price");

                return new Material(id, name, description, unit, amount, length, price);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i at hente stolper" + e.getMessage());
        }
        return null;
    }

    //Bruges til at hente spær
    public static Material getRafter() throws DatabaseException {
        String sql = "SELECT * FROM materials WHERE name = '45x195 mm. spærtræ ubh.'";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String unit = rs.getString("unit");
                int amount = rs.getInt("amount");
                int length = rs.getInt("length");
                int price = rs.getInt("price");

                return new Material(id, name, description, unit, amount, length, price);
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente spær" + e.getMessage());
        }
        return null;
    }
}

