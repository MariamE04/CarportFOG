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



}
