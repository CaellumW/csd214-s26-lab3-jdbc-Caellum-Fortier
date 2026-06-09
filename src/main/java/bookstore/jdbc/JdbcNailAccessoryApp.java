package bookstore.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import bookstore.pojos.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcNailAccessoryApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASSWORD = "itstudies12345";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS accessories (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "accessory VARCHAR(255), " +
                "brand VARCHAR(255), " +
                "price DOUBLE, " +
                "stock INT)" +
                "isbn VARCHAR(20)";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'accessories' checked/created.");
        }
    }


    private static void insertAccessory(NailAccessory accessory) throws SQLException {
        String sql = "INSERT INTO accessories (id, accessory, brand, price, stock) VALUES (?, ?, ?, ?, ? ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accessory.getProductId());
            pstmt.setString(2, accessory.getAccessoryType());
            pstmt.setDouble(3, accessory.getPrice());
            pstmt.setInt(4, accessory.getStock());
            pstmt.setString(5, accessory.getIsbn());

            int rows = pstmt.executeUpdate();
            System.out.println("Inserted " + rows + " row(s).");
        }
    }

    private static void listAccessories() throws SQLException {
        String sql = "SELECT * FROM accessories";
        List<NailAccessory> accessories = new ArrayList<>();

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Current Database Content:");
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                // Reconstruct Accessory object from DB
                String brand = rs.getString("brand");
                String accessoryType = rs.getString("accessory");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                int id = rs.getInt("productID"); // Captured but not stored in POJO currently
                String isbn = rs.getString("isbn");

                System.out.printf("  [ID: %d] %s by %s ($%.2f) - %d copies%n",
                        id, brand, accessoryType, price, stock);
            }
            if (empty) System.out.println("  (Table is empty)");
        }
    }

    private static void updateAccessoryByID(NailAccessory item) throws SQLException {
        // Updating based on ID for accessory
        String sql = "UPDATE accessories SET price = ?, stock = ?, brand = ?, accessory = ? WHERE productID = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, item.getPrice());
            pstmt.setInt(2, item.getStock());
            pstmt.setString(3, item.getAccessoryType());
            pstmt.setString(4, item.getBrand());

            int rows = pstmt.executeUpdate();
            System.out.println("Updated " + rows + " row(s) for id: " + item.getProductId());
        }
    }

    private static void deleteAccessoryByID(String id) throws SQLException {
        String sql = "DELETE FROM accessory WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int rows = pstmt.executeUpdate();
            System.out.println("Deleted " + rows + " row(s) with id: " + id);
        }
    }
}
