package app.persistence;

import app.entities.Carport;
import app.entities.Order;
import app.entities.Quote;
import app.entities.Shed;
import app.entities.User;
import app.exceptions.DatabaseException;
import org.apache.xpath.operations.Or;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }

    public static void addOrder(Order order) throws DatabaseException {
        String sql = "INSERT INTO orders (carport_id, order_date, status, total_price) " +
                "VALUES(?,?,?,?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, order.getCarport().getCarportId());
            ps.setDate(2, Date.valueOf(order.getDate_created()));
            ps.setString(3, order.getStatus());
            ps.setDouble(4, order.getTotal_price());

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Fejl med at indsætte ordreren", e.getMessage());
        }
    }

    public static int getLatestOrderNr() throws DatabaseException {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("order_id");
            } else {
                return 0; // Ingen ordrer endnu
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i getLatestOrderNr", e.getMessage());
        }
    }

    public static List<Order> getAllOrders() throws DatabaseException {
        String sql = "SELECT * FROM orders\n" +
                "LEFT JOIN carports ON orders.carport_id = carports.carport_id\n" +
                "LEFT JOIN sheds ON carports.shed_id = sheds.shed_id\n" +
                "LEFT JOIN quotes ON orders.order_id = quotes.order_id\n" +
                "JOIN users ON carports.user_id = users.user_id\n" +
                "ORDER BY\n" +
                "  CASE\n" +
                "    WHEN status = 'Afventer betaling' THEN 1\n" +
                "    WHEN status = 'Afvist' THEN 2\n" +
                "    WHEN status = 'Godkendt' THEN 3\n" +
                "    WHEN status = 'Uløbet' THEN 4\n" +
                "    ELSE 5\n" +
                "  END;\n";

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
                String email = rs.getString("email");
                int carportId = rs.getInt("carport_id");

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

                Carport carport = new Carport(carportId, carportWidth, carportLength, roofType, shed, new User(userId, email));

                ordersList.add(new Order(id, localDate, price, paymentStatus, userId, carport, shed));
            }

            System.out.println("Har hentet størrelsen på listen her" + ordersList.size());

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente alle ordrene" + e.getMessage());
        }
        return ordersList;
    }

    public static void updatePrice(int id, double price) throws DatabaseException {
        String sql = "UPDATE orders SET total_price = ? WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection(); // Henter en forbindelse fra connection pool.
             PreparedStatement ps = connection.prepareStatement(sql)) { // Forbereder SQL-forespørgslen.

            ps.setDouble(1, price);  // Sætter den nye pris.
            ps.setInt(2, id);   // Sætter order_id i forespørgslen.

            ps.executeUpdate(); // Udfører opdateringen.

        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke opdatere order pris: " + e.getMessage());
        }
    }


    public static double getPrice(int orderId) throws DatabaseException {
        double price = 0;

        String sql = "SELECT total_price FROM orders WHERE order_id = ?";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                 price += rs.getDouble("total_price");
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente ordrene fra "+ orderId + e.getMessage());
        }
        return price;
    }

    public static Order getOrderId(int id) throws DatabaseException {
        Order order = null;

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                // Tilføj evt. andre felter som du har i Order-klassen
                order = new Order(id, status); // Du skal have en constructor i Order-klassen til dette
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error getting order by id", e.getMessage());
        }
        return order;
    }

    public static List<Carport> getCarportsWithoutOrders() throws DatabaseException{
        String sql = "SELECT * FROM carports LEFT JOIN orders ON carports.carport_id = orders.carport_id WHERE order_id IS NULL";

        List<Carport> carportList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("carport_id");
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");
                String roofType = rs.getString("roof_type");
                //int shed = rs.getInt("shed_id");
                int user = rs.getInt("user_id");

                carportList.add(new Carport(id, carportWidth, carportLength, roofType, null, new User(user)));
            }

        } catch (SQLException e){
            throw new DatabaseException("Fejl i at hente tomme carporte" + e.getMessage());
        }
        return carportList;
    }
}