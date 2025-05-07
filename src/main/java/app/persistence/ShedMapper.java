package app.persistence;

import app.entities.Carport;
import app.entities.Shed;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShedMapper {

    //Vores ConnectionPool instans
    private static ConnectionPool connectionPool;

    public static void setConnectionPool(ConnectionPool newConnectionPool){
        connectionPool = newConnectionPool;
    }


    public static List<Shed> getShedWidthAndLength() throws DatabaseException {
        List<Shed> shedWidthAndLength = new ArrayList<>(); // List
        String sql = "SELECT * FROM public.shed"; // SQL query to search for the widths and lengths

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                int width = rs.getInt("shed_width"); // Extract the 'width' field from the row.
                int length = rs.getInt("shed_length"); // Extract the 'length' field.

                // Create a Shed object and add it to the list.
                shedWidthAndLength.add(new Shed(width, length));
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return shedWidthAndLength;  // Return the list of shedWidthAndLength records
    }

    //Method to get the width the user chooses
    public static Shed getChosenShedWidthAndLength(int choice1, int choice2) throws DatabaseException {
        Shed shed = null; // Object to hold topping record.
        String sql = "SELECT * FROM public.shed WHERE shed_width = ? AND shed_length = ?"; // SQL query to fetch all records.

        try (
                Connection connection = connectionPool.getConnection(); // Acquire a database connection.
                PreparedStatement ps = connection.prepareStatement(sql) // Prepare the SQL statement.
        ) {
            ps.setInt(1, choice1);
            ps.setInt(2, choice2);
            ResultSet rs = ps.executeQuery(); // Execute the query and fetch results.
            while (rs.next()) { // Iterate over each row in the result set.
                int width = rs.getInt("shed_width"); // Extract the 'topping' field from the row.
                int length = rs.getInt("shed_length"); // Extract the 'price' field.

                // Create a Shed object and add it to the list.
                shed = new Shed(width, length);
            }
            //Catch exceptions
        } catch (SQLException e) {
            throw new DatabaseException("Error", e.getMessage());
        }
        return shed;  // Return the list of shed records
    }

}
