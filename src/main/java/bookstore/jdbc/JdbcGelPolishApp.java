package bookstore.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import bookstore.pojos.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// this class SHOULD be totally done


public class JdbcGelPolishApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASSWORD = "itstudies12345";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static void main(String[] args) {
        try {
            // 1. Ensure Table Exists
            createTable();

            // 2. Create (Insert) a Book
            System.out.println("--- INSERTING POLISH ---");
            GelPolish newPolish = new GelPolish("Red", "matte", "nyx", 5.0, 5);
            insertPolish(newPolish);
            listPolishes();

            // 3. Edit (Update) a Book
            // We'll update the price and copies of 'The Hobbit'
            System.out.println("\n--- UPDATING POLISH ---");
            newPolish.setPrice(99.99);
            newPolish.setStock(50);
            updatePolishPrice(newPolish);
            listPolishes();

            // 4. Delete a Book
            System.out.println("\n--- DELETING POLISH ---");
            deletePolishByID(newPolish.getProductId());
            listPolishes();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS polishes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "brand VARCHAR(255), " +
                "price DOUBLE, " +
                "stock DOUBLE, " +
                "colour VARCHAR(255), " +
                "shine VARCHAR(255), " +
                "productID VARCHAR(255))";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'polishes' checked/created.");
        }
    }


    private static void insertPolish(GelPolish polish) throws SQLException {
        String sql = "INSERT INTO polishes (brand, price, stock, colour, shine, productID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, polish.getBrand());
            pstmt.setDouble(2, polish.getPrice());
            pstmt.setDouble(3, polish.getStock());
            pstmt.setString(4, polish.getColourShade());
            pstmt.setString(5, polish.getFinish());
            pstmt.setString(6, polish.getProductId()); // ask about the getting and setting ID for the database

            int rows = pstmt.executeUpdate();
            System.out.println("Inserted " + rows + " row(s).");
        }
    }

    private static void listPolishes() throws SQLException {
        String sql = "SELECT * FROM polishes";
        List<GelPolish> polishes = new ArrayList<>();

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Current Database Content:");
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                // Reconstruct polish object from DB
                String brand = rs.getString("brand");
                double price = rs.getDouble("price");
                double stock = rs.getDouble("stock");
                String colour = rs.getString("colour");
                String shine = rs.getString("shine"); // Captured but not stored in POJO currently
                String productID = rs.getString("productID");

                System.out.printf("  [ID: %s] %s by %s (finish: %s). Price: $%.2f%n / Stock: $%.2f%n - ",
                        productID, colour, brand, shine, price, stock);
            }
            if (empty) System.out.println("  (Table is empty)");
        }
    }

    private static void updatePolishPrice(GelPolish polish) throws SQLException {
        // Updating based on polish product id
        String sql = "UPDATE polishes SET price = ?, stock = ?, brand = ? WHERE productID = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, polish.getPrice()); //price
            pstmt.setInt(2, polish.getStock()); //stock
            pstmt.setString(3, polish.getBrand()); //brand
            pstmt.setString(4, polish.getProductId()); //id

            int rows = pstmt.executeUpdate();
            System.out.println("Updated " + rows + " row(s) for id: " + polish.getProductId());
        }
    }

    private static void deletePolishByID(String id) throws SQLException {
        String sql = "DELETE FROM polishes WHERE productID = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int rows = pstmt.executeUpdate();
            System.out.println("Deleted " + rows + " row(s) with id: " + id);
        }
    }

}
