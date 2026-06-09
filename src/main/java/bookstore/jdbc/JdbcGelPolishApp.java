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


    private static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS polishes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "brand VARCHAR(255), " +
                "price VARCHAR(255), " +
                "stock DOUBLE, " +
                "color INT," +
                "finish VARCHAR(255))" +
                "isbn VARCHAR(20)";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'polishes' checked/created.");
        }
    }


    private static void insertPolish(GelPolish polish) throws SQLException {
        String sql = "INSERT INTO polishes (brand, price, stock, colour, finish) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, polish.getBrand());
            pstmt.setDouble(2, polish.getPrice());
            pstmt.setDouble(3, polish.getStock());
            pstmt.setString(4, polish.getColourShade());
            pstmt.setString(5, polish.getFinish());
            pstmt.setString(6, polish.getIsbn());
            pstmt.setString(7, polish.getProductId()); // ask about the getting and setting ID for the database

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
                String finish = rs.getString("finish"); // Captured but not stored in POJO currently
                String isbn = rs.getString("isbn");

                System.out.printf("  [ID: %d] %s by %s ($%.2f) - %d copies%n",
                        brand, price, stock, colour, finish);
            }
            if (empty) System.out.println("  (Table is empty)");
        }
    }

    private static void updatePolishPrice(GelPolish polish) throws SQLException {
        // Updating based on polish product id
        String sql = "UPDATE polish SET price = ?, stock = ?, brand = ? WHERE id = ?";

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
        String sql = "DELETE FROM polishes WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int rows = pstmt.executeUpdate();
            System.out.println("Deleted " + rows + " row(s) with id: " + id);
        }
    }

}
