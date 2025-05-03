package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    private static ConnectionPool connectionPool;

    public OrderMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public static void addOrder(Order order) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id, carport_id, quote_id, order_date, status, total_price) " +
                "VALUES(?,?,?,?,?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, order.getOrderId());
            ps.setInt(2, order.getCarportId());
            ps.setInt(3, order.getQuoteId());
            ps.setDate(4, Date.valueOf(order.getDateCreated()));
            ps.setString(5, order.isPaymentStatus());
            ps.setDouble(6, order.getPrice());

            ps.executeUpdate();

        }catch (SQLException e){
            throw new DatabaseException("Fejl med at indsætte ordreren", e.getMessage());
        }

    }

    public static int getLatestOrderNr() throws DatabaseException{
        String sql = "SELECT order_nr FROM orders ORDER BY order_nr DESC";
        int ordernumber;

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            rs.next();
            ordernumber = rs.getInt("order_nr");

        }catch (SQLException e){
            throw new RuntimeException();
        }
        return ordernumber;
    }

    public static List<Order> getAllOrders() throws DatabaseException{
        String sql = "SELECT * FROM orders";
        List<Order> ordersList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("order_id");
                LocalDate localDate = LocalDate.parse(rs.getString("order_date"));
                Double price = rs.getDouble("total_price");
                String paymentStatus = rs.getString("status");
                int userId = rs.getInt("user_id");
                int carportId = rs.getInt("carportId");
                int quoteId = rs.getInt("quoteId");

                ordersList.add(new Order(id, localDate, price, paymentStatus, userId, carportId, quoteId));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente alle ordrene" + e.getMessage());
        }
        return ordersList;
    }

    public static List<Order> getOrdersByUserId(int userId) throws DatabaseException{
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        List<Order> ordersList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("order_id");
                LocalDate localDate = LocalDate.parse(rs.getString("order_date"));
                Double price = rs.getDouble("total_price");
                String paymentStatus = rs.getString("status");
                int carportId = rs.getInt("carportId");
                int quoteId = rs.getInt("quoteId");

                ordersList.add(new Order(id, localDate, price, paymentStatus, userId, carportId, quoteId));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente ordrene fra "+ userId + e.getMessage());
        }
        return ordersList;
    }


}