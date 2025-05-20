package app.persistence;


import app.entities.Material;
import app.entities.OrderDetails;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailMapper {

    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }

    public OrderDetailMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;

    }

    public static List<OrderDetails> getOrderDetailsByOrder(int orderId) throws DatabaseException {
        List<OrderDetails> orderDetails = new ArrayList<>();
        String sql = "SELECT *\n" +
                "FROM orderdetails\n" +
                "JOIN orders ON orderdetails.order_id = orders.order_id " +
                "JOIN materials ON orderdetails.material_id = materials.material_id WHERE orders.order_id = ?";


        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int quantity = rs.getInt("quantity");

                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String unit = rs.getString("unit");
                int amount = rs.getInt("amount");
                int length = rs.getInt("length");
                int price = rs.getInt("price");
                int orderDetailId = rs.getInt("order_detail_id");

                Material material = new Material(materialId, name, description, unit, amount, length, price);

                orderDetails.add(new OrderDetails(material, quantity, orderId, orderDetailId));
            }


        }catch (SQLException e){
            throw new DatabaseException("Fejl med at hente ordrer detaljerne ud fra ordre", e.getMessage());
        }
        return orderDetails;

    }

    public static void addOrderDetail(int orderId, Material material, int quantity) throws DatabaseException {
        String sql = "INSERT INTO orderdetails (order_id, material_id, quantity) VALUES(?,?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, orderId);
            ps.setInt(2, material.getMaterialId());
            ps.setInt(3, quantity);

            ps.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException(e.getMessage());
        }
    }


    public static void updateQuantity(int quantity, int orderDetailId) throws DatabaseException{
        String sql ="UPDATE orderdetails SET quantity = ? WHERE order_detail_id = ? ";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, quantity);
            ps.setInt(2, orderDetailId);


            ps.executeUpdate();
        } catch (SQLException e){
            throw new DatabaseException(e.getMessage());
        }

    }

}
