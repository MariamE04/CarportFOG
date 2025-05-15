package app.persistence;

import app.entities.Carport;
import app.entities.Order;
import app.entities.Shed;
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

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void addOrder(Order order) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id, carport_id, quote_id, order_date, status, total_price) " +
                "VALUES(?,?,?,?,?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, order.getOrder_id());
            ps.setInt(2, order.getOrder_id());
            ps.setDate(3, Date.valueOf(order.getDate_created()));
            ps.setString(4, order.getStatus());
            ps.setDouble(5, order.getTotal_price());

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
        String sql = "SELECT * FROM orders JOIN carports ON orders.carport_id = carports.carport_id\n" +
                "LEFT JOIN sheds ON carports.shed_id = sheds.shed_id\n";

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
                int carportId = rs.getInt("carport_id");
                int quoteId = rs.getInt("quote_id");

                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");
                String roofType = rs.getString("roof_type");
                int shedWidth = rs.getInt("shed_width");
                int shedLength = rs.getInt("shed_length");
                int shedId = rs.getInt("shed_id");

                Shed shed = null;
                if(shedId > 0){
                    shed = new Shed( shedId, shedLength, shedWidth);
                }

                Carport carport = new Carport(carportId, carportWidth, carportLength, roofType, shed);

                ordersList.add(new Order(id, localDate, price, paymentStatus, userId, quoteId, carport, shed));
            }

            System.out.println("Har hentet størrelsen på listen her" + ordersList.size());

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

                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");

                int shedWidth = rs.getInt("carport_width");
                int shedLength = rs.getInt("shed_length");
                String roofType = rs.getString("roof_type");
                Shed shed = new Shed(shedLength, shedWidth);

                Carport carport = new Carport(carportId ,carportWidth, carportLength, roofType, shed);
                ordersList.add(new Order(id, localDate, price, paymentStatus, userId, quoteId, carport, shed));            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente ordrene fra "+ userId + e.getMessage());
        }
        return ordersList;
    }

    public static void updateOrderStatusByQuoteId(int quoteId, String status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET status = ? WHERE order_id = (SELECT order_id FROM quotes WHERE quote_id = ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, quoteId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved opdatering af order-status med quote_id: " + e.getMessage());
        }
    }


}