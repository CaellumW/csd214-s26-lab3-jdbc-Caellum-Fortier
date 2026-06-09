package bookstore.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import bookstore.pojos.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// This class SHOULD be fully done


public class JdbcTireApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASSWORD = "itstudies12345";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    private static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS tires (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "manufacturer VARCHAR(255), " +
                "price DOUBLE, " +
                "diameter INT) " +
                "isbn VARCHAR(20)";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'tires' checked/created.");
        }
    }


    private static void insertTire(Tire tire) throws SQLException {
        String sql = "INSERT INTO tires (manufacturer, price, diameter) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tire.getManufacturer());
            pstmt.setDouble(2, tire.getPrice());
            pstmt.setDouble(3, tire.getDiameter());
            pstmt.setString(4, tire.getIsbn());

            int rows = pstmt.executeUpdate();
            System.out.println("Inserted " + rows + " row(s).");
        }
    }

    private static void listTires() throws SQLException {
        String sql = "SELECT * FROM tires";
        List<Tire> tires = new ArrayList<>();

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Current Database Content:");
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                // Reconstruct Tire object from DB
                String manufacturer = rs.getString("manufacturer");
                double price = rs.getDouble("price");
                int diameter = rs.getInt("diameter");
                int id = rs.getInt("id"); // Captured but not stored in POJO currently
                String isbn = rs.getString("isbn");

                System.out.printf("  [ID: %d] %s for ($%.2f) - %d cm%n",
                        id, manufacturer, price, diameter);
            }
            if (empty) System.out.println("  (Table is empty)");
        }
    }

    private static void updatePriceByID(Tire tire) throws SQLException {
        // Updating based on ID for tire
        String sql = "UPDATE tires SET price = ?, diameter = ?, manufacturer = ? WHERE ID = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, tire.getPrice()); //price
            pstmt.setInt(2, tire.getDiameter()); //diameter
            pstmt.setString(3, tire.getManufacturer()); //manufacturer
            pstmt.setString(4, tire.getProductId()); // product id

            int rows = pstmt.executeUpdate();
            System.out.println("Updated " + rows + " row(s) for tire: " + tire.getProductId());
        }
    }

    private static void deleteTireByID(String id) throws SQLException {
        String sql = "DELETE FROM tires WHERE ID = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int rows = pstmt.executeUpdate();
            System.out.println("Deleted " + rows + " row(s) with id: " + id);
        }
    }

}
