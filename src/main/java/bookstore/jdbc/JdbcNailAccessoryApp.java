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
                "stock INT, " +
                "productID VARCHAR(255))";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'accessories' checked/created.");
        }
    }


    public static void main(String[] args) {
        try {
            // 1. Ensure Table Exists
            createTable();

            // 2. Create (Insert) a Book
            System.out.println("--- INSERTING ACCESSORY ---");
            NailAccessory newAccessory = new NailAccessory("Drill", "Nyx", 20.0, 50);
            insertAccessory(newAccessory);
            listAccessories();

            // 3. Edit (Update) a Book
            // We'll update the price and copies of 'The Hobbit'
            System.out.println("\n--- UPDATING ACCESSORY ---");
            newAccessory.setPrice(99.99);
            newAccessory.setStock(50);
            updateAccessoryPrice(newAccessory);
            listAccessories();

            // 4. Delete a Book
            System.out.println("\n--- DELETING POLISH ---");
            deleteAccessoryByID(newAccessory.getProductId());
            listAccessories();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertAccessory(NailAccessory accessory) throws SQLException {
        String sql = "INSERT INTO accessories (accessory, brand, price, stock, productID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, accessory.getAccessoryType());
            pstmt.setDouble(2, accessory.getPrice());
            pstmt.setInt(3, accessory.getStock());
            pstmt.setString(4, accessory.getIsbn());
            pstmt.setString(5, accessory.getProductId());

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
                String productID = rs.getString("productID"); // Captured but not stored in POJO currently

                System.out.printf("  [ID: %s] %s by %s (Price: $%.2f) - %d stock%n",
                        productID, accessoryType, brand, price, stock);
            }
            if (empty) System.out.println("  (Table is empty)");
        }
    }

    private static void updateAccessoryPrice(NailAccessory accessoryItem) throws SQLException {
        // Updating based on ID for accessory
        String sql = "UPDATE accessories SET price = ?, stock = ?, brand = ?, accessory = ? WHERE productID = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, accessoryItem.getPrice());
            pstmt.setInt(2, accessoryItem.getStock());
            pstmt.setString(3, accessoryItem.getBrand());
            pstmt.setString(4, accessoryItem.getAccessoryType());
            pstmt.setString(5, accessoryItem.getProductId());


            int rows = pstmt.executeUpdate();
            System.out.println("Updated " + rows + " row(s) for id: " + accessoryItem.getProductId());
        }
    }

    private static void deleteAccessoryByID(String id) throws SQLException {
        String sql = "DELETE FROM accessories WHERE productID = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int rows = pstmt.executeUpdate();
            System.out.println("Deleted " + rows + " row(s) with id: " + id);
        }
    }
}
