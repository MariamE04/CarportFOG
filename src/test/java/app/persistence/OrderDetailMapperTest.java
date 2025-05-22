package app.persistence;

import app.entities.Material;
import app.entities.OrderDetails;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDetailMapperTest {

    private static ConnectionPool connector;

    @BeforeAll
    static void setUpDatabase() {
        try {
            connector = ConnectionPool.getInstance("postgres", "bvf64wwa", "jdbc:postgresql://142.93.174.150:5432/%s?currentSchema=test", "carport");
            OrderDetailMapper.setConnectionPool(connector);

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
                                "width BIGINT NOT NULL, " +
                                "length BIGINT NOT NULL);" +

                                // CARPORTS
                                "CREATE TABLE IF NOT EXISTS test.carports (" +
                                "carport_id bigserial PRIMARY KEY, " +
                                "width BIGINT NOT NULL, " +
                                "length BIGINT NOT NULL, " +
                                "height BIGINT NOT NULL, " +
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
            stmt.execute("INSERT INTO test.sheds (width, length) VALUES (200, 300);");
            stmt.execute("INSERT INTO test.carports (width, length, height, roof_type, shed_id) VALUES (600, 700, 250, 'flat', 1);");
            stmt.execute("INSERT INTO test.carports (width, length, height, roof_type) VALUES (600, 700, 250, 'flat');");
            stmt.execute("INSERT INTO test.quotes (final_price, valid_until_date, created_at_date, is_accepted) VALUES (19999.99, '2025-12-31', '2025-01-01', false);");
            stmt.execute("INSERT INTO test.materials (name, description, unit, amount, length, price) VALUES ('wood beam', 'strong beam', 'pcs', 10, 240, 30.0);");
            stmt.execute("INSERT INTO test.orders (user_id, carport_id, quote_id, order_date, status, total_price) VALUES (1, 1, 1, '2025-01-01', 'pending', 19999.99);");
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
    void getOrderDetailsByOrder() throws DatabaseException {
        List<OrderDetails> orderDetailsList = OrderDetailMapper.getOrderDetailsByOrder(1);

        assertEquals(1, orderDetailsList.size());
        System.out.println(orderDetailsList.size());

        OrderDetails orderDetails = orderDetailsList.getFirst();
        assertEquals(1, orderDetails.getOrderId());
        assertEquals(5, orderDetails.getQuantity());


        Material materials = orderDetails.getMaterial();

        assertEquals(1, materials.getMaterialId());
        assertEquals("wood beam", materials.getName());
        assertEquals("strong beam", materials.getDescription());
        assertEquals("pcs", materials.getUnit());
        assertEquals(10, materials.getAmount());
        assertEquals(240, materials.getLength());
        assertEquals(30, materials.getPrice());
    }
@Test
    void databaseError(){
        //todo: ændre i SQL sætningen
        assertThrows(DatabaseException.class, () -> OrderDetailMapper.getOrderDetailsByOrder(2));
    }



}



