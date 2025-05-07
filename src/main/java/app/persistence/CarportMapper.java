package app.persistence;

import app.entities.Carport;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarportMapper {

    //Vores ConnectionPool instans
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }


    //Method to get all the widths and lengths
    public static List<Carport> getWidthAndLength() throws DatabaseException {
        List<Carport> widthAndLength = new ArrayList<>(); // List
        String sql = "SELECT * FROM public.carports"; // SQL query to search for the widths and lengths

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                int width = rs.getInt("carport_width"); // Extract the 'width' field from the row.
                int length = rs.getInt("carport_length"); // Extract the 'length' field.

                // Create a Carport object and add it to the list.
                widthAndLength.add(new Carport(width, length));
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return widthAndLength;  // Return the list of widthAndLength records
    }

    //Method to get the width the user chooses
    public static Carport getChosenWidthAndLength(int choice1, int choice2) throws DatabaseException {
        Carport carport = null; // Object to hold topping record.
        String sql = "SELECT * FROM public.carports WHERE carport_width = ? AND carport_length = ?"; // SQL query to fetch all records.

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ps.setInt(1, choice1);
            ps.setInt(2, choice2);
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                int width = rs.getInt("carport_width"); // Extract the 'topping' field from the row.
                int length = rs.getInt("carport_length"); // Extract the 'price' field.

                // Create a Carport object and add it to the list.
                carport = new Carport(width, length);
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return carport;  // Return the list of carport records
    }

}
