package app.persistence;

import app.entities.*;
import app.entities.Order;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {
    private static ConnectionPool connector;

    @BeforeAll
    static void setUpDatabase() {

        try {
            connector = ConnectionPool.getInstance("postgres", "bvf64wwa", "jdbc:postgresql://142.93.174.150:5432/%s?currentSchema=test", "carport");
            OrderMapper.setConnectionPool(connector);

            try (Connection conn = connector.getConnection(); Statement stmt = conn.createStatement()) {
                String sql =
                        "DROP TABLE IF EXISTS test.orderdetails CASCADE;" +
                                "DROP TABLE IF EXISTS test.orders CASCADE;" +
                                "DROP TABLE IF EXISTS test.users CASCADE;" +
                                "DROP TABLE IF EXISTS test.carports CASCADE;" +
                                "DROP TABLE IF EXISTS test.materials CASCADE;" +
                                "DROP TABLE IF EXISTS test.quotes CASCADE;" +
                                "DROP TABLE IF EXISTS test.sheds CASCADE;" +

                                // USERS
                                "CREATE TABLE IF NOT EXISTS test.users (" +
                                "user_id bigserial PRIMARY KEY, " +
                                "email VARCHAR(255) NOT NULL UNIQUE, " +
                                "password VARCHAR(255) NOT NULL, " +
                                "role VARCHAR(50), " +
                                "phone_number BIGINT);" +

                                // SHEDS
                                "CREATE TABLE IF NOT EXISTS test.sheds (" +
                                "shed_id bigserial PRIMARY KEY, " +
                                "shed_width BIGINT NOT NULL, " +
                                "shed_length BIGINT NOT NULL);" +

                                // CARPORTS
                                "CREATE TABLE IF NOT EXISTS test.carports (" +
                                "carport_id bigserial PRIMARY KEY, " +
                                "carport_width BIGINT NOT NULL, " +
                                "carport_length BIGINT NOT NULL, " +
                                "carport_height BIGINT NOT NULL, " +
                                "roof_type VARCHAR(255) NOT NULL, " +
                                "shed_id BIGINT REFERENCES test.sheds(shed_id));" +

                                // QUOTES
                                "CREATE TABLE IF NOT EXISTS test.quotes (" +
                                "quote_id bigserial PRIMARY KEY, " +
                                "final_price DOUBLE PRECISION NOT NULL, " +
                                "valid_until_date DATE NOT NULL, " +
                                "created_at_date DATE NOT NULL, " +
                                "is_accepted BOOLEAN DEFAULT false);" +

                                // ORDERS
                                "CREATE TABLE IF NOT EXISTS test.orders (" +
                                "order_id bigserial PRIMARY KEY, " +
                                "user_id BIGINT NOT NULL REFERENCES test.users(user_id), " +
                                "carport_id BIGINT NOT NULL REFERENCES test.carports(carport_id), " +
                                "quote_id BIGINT NOT NULL REFERENCES test.quotes(quote_id), " +
                                "order_date DATE NOT NULL, " +
                                "status VARCHAR(50) NOT NULL, " +
                                "total_price DOUBLE PRECISION NOT NULL);" +

                                // MATERIALS
                                "CREATE TABLE IF NOT EXISTS test.materials (" +
                                "material_id bigserial PRIMARY KEY, " +
                                "name VARCHAR(255) NOT NULL, " +
                                "description VARCHAR(255), " +
                                "unit VARCHAR(50) NOT NULL, " +
                                "amount BIGINT NOT NULL, " +
                                "length DOUBLE PRECISION, " +
                                "price DOUBLE PRECISION NOT NULL);" +

                                // ORDERDETAILS
                                "CREATE TABLE IF NOT EXISTS test.orderdetails (" +
                                "order_detail_id bigserial PRIMARY KEY, " +
                                "order_id BIGINT NOT NULL REFERENCES test.orders(order_id), " +
                                "material_id BIGINT NOT NULL REFERENCES test.materials(material_id), " +
                                "quantity BIGINT NOT NULL);";

                stmt.execute(sql);
            }
        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

    @BeforeEach
    void resetData() {
        try (Connection conn = connector.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM test.orderdetails;");
            stmt.execute("DELETE FROM test.orders;");
            stmt.execute("DELETE FROM test.carports;");
            stmt.execute("DELETE FROM test.materials;");
            stmt.execute("DELETE FROM test.quotes;");
            stmt.execute("DELETE FROM test.sheds;");
            stmt.execute("DELETE FROM test.users;");

            // Tilføj testdata
            stmt.execute("INSERT INTO test.users (email, password, role, phone_number) VALUES ('test@example.com', 'secret', 'admin', 12345678);");
            stmt.execute("INSERT INTO test.users (email, password, role, phone_number) VALUES ('test@example2.com', 'secret2', 'admin2', 12345679);");
            stmt.execute("INSERT INTO test.sheds (shed_width, shed_length) VALUES (200, 300);");
            stmt.execute("INSERT INTO test.carports (carport_width, carport_length, carport_height, roof_type, shed_id) VALUES (600, 700, 250, 'flat', 1);");
            stmt.execute("INSERT INTO test.carports (carport_width, carport_length, carport_height, roof_type) VALUES (350, 900, 300, 'kegle');");
            stmt.execute("INSERT INTO test.quotes (final_price, valid_until_date, created_at_date, is_accepted) VALUES (19999.99, '2025-12-31', '2025-01-01', false);");
            stmt.execute("INSERT INTO test.quotes (final_price, valid_until_date, created_at_date, is_accepted) VALUES (88888.99, '2023-11-28', '2022-03-07', false);");
            stmt.execute("INSERT INTO test.materials (name, description, unit, amount, length, price) VALUES ('wood beam', 'strong beam', 'pcs', 10, 240.5, 30.0);");
            stmt.execute("INSERT INTO test.orders (user_id, carport_id, quote_id, order_date, status, total_price) VALUES (1, 1, 1, '2025-01-01', 'pending', 19999.99);");
            stmt.execute("INSERT INTO test.orders (user_id, carport_id, quote_id, order_date, status, total_price) VALUES (2, 2, 2, '2023-02-05', 'pending', 8792.99);");
            stmt.execute("INSERT INTO test.orderdetails (order_id, material_id, quantity) VALUES (1, 1, 5);");

        } catch (SQLException e) {

            fail("Test data setup failed: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        assertNotNull(connector.getConnection(), "Connection should not be null");
    }

    @Test
    void testGetAllOrder() throws DatabaseException {
        List<Order> ordersList = OrderMapper.getAllOrders();
        assertEquals(2, ordersList.size());

        Order order = ordersList.getFirst();

        assertEquals(1, order.getOrder_id());
        assertEquals(1, order.getUser_id());
        assertEquals(1, order.getOrder_id());
        assertEquals(LocalDate.of(2025,1,1), order.getDate_created());
        assertEquals("pending", order.getStatus());
        assertEquals(19999.99, order.getTotal_price());

        Shed shed = ordersList.get(0).getShed();
        assertNotNull(shed);

        Shed shed1 = ordersList.get(1).getShed();
        assertNull(shed1);
    }

    @Test
    void databaseError(){
        //todo: Gå ind og lave en fejl i sql sætningen
        assertThrows(DatabaseException.class, () -> OrderMapper.getAllOrders());

    }


    @Test
    void updateOrderStatusByQuoteId() throws DatabaseException {
        int orderId = 1;
        String expectedStatus = "bekræftet";

        // Act: Opdater status i databasen
        OrderMapper.updateOrderStatusByQuoteId(orderId, expectedStatus);

        // Assert: Hent order_id og Order-objektet og tjek status
        OrderMapper.updateOrderStatusByQuoteId(orderId, expectedStatus);

        Order order = OrderMapper.getOrderId(orderId);

        assertEquals(expectedStatus, order.getStatus());
    }



    /*
    @Test
    void getAllcolonesCorrect() throws DatabaseException{
        List<Order> ordersList = orderMapper.getAllOrders();
        LocalDate localDate = LocalDate.parse("2025-01-01");
        Shed shed = new Shed(1, 300,200);
        Carport carport = new Carport(1, 600,700,"flat", shed);
        Order order = new Order(1, localDate ,19999.99, "pending", 1, 1, carport, shed);
        assertEquals(order, ordersList.getFirst());

    }

     */

}


