package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private static ConnectionPool connector;

    @BeforeAll
    static void setUpDatabase() {
        try {
            connector = ConnectionPool.getInstance("postgres", "bvf64wwa", "jdbc:postgresql://142.93.174.150:5432/%s?currentSchema=test", "carport");

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
            stmt.execute("INSERT INTO test.sheds (shed_width, shed_length) VALUES (200, 300);");
            stmt.execute("INSERT INTO test.carports (carport_width, carport_length, roof_type, shed_id) VALUES (600, 700, 'flat', 1);");
            stmt.execute("INSERT INTO test.quotes (final_price, valid_until_date, created_at_date, is_accepted) VALUES (19999.99, '2025-12-31', '2025-01-01', false);");
            stmt.execute("INSERT INTO test.materials (name, description, unit, amount, length, price) VALUES ('wood beam', 'strong beam', 'pcs', 10, 240.5, 30.0);");
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
    void signUp() {
        try {
            UserMapper.setConnectionPool(connector); // Sæt connection pool

            // Forsøg at oprette en ny bruger
            int result = UserMapper.signUp("test01@example.com", "password123", 12345678);
            if (result == 1) {
                System.out.println("User successfully signed up!");
            } else {
                System.out.println("User already exists or no rows affected.");
            }

            // Forsøg at oprette samme bruger igen (bør ikke lykkes)
            result = UserMapper.signUp("test@example.com", "newpassword456", 87654321);
            if (result == 1) {
                System.out.println("New user created successfully (shouldn't happen).");
            } else {
                System.out.println("User already exists, sign-up failed (as expected).");
            }

        } catch (DatabaseException e) {
            e.printStackTrace();
            Assertions.fail("Sign-up failed unexpectedly: " + e.getMessage());
        }
    }



    @Test
    void testUserExists() {
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM test.users WHERE email = ?")) {
            ps.setString(1, "test@example.com");
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "User should exist in the database");
        } catch (SQLException e) {
            fail("Query failed: " + e.getMessage());
        }
    }
}
