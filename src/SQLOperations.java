import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class SQLOperations {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/university";
    private static final String user = "root";
    private static final String password = "";

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showTableData(DefaultTableModel tableModel, String tableName) {
        try (Connection con = getConnection()) {
            if (con == null) {
                throw new RuntimeException("Database Connection Failed");
            }
            String query = "SELECT * FROM " + tableName;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int col = 1; col <= columnCount; col++) {
                tableModel.addColumn(meta.getColumnName(col));
            }
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                for (int col = 1; col <= columnCount; col++) {
                    row.add(rs.getString(col));
                }
                tableModel.addRow(row);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteSql(String tableName, String keyColumn, String keyValue){
        String query = "DELETE FROM " + tableName + " WHERE " + keyColumn + " = ?";
        try (Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query)){
            if (con==null){
                throw new RuntimeException("Database connection failed");
            }
            pstmt.setString(1,keyValue);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow ==0){
                throw new SQLException("Deleting row failed");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void insertSql(String tableName,String name,String surname,String course){
        String query = "INSERT INTO " + tableName + " (Name, Surname, CourseCode) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            if (con == null) {
                throw new RuntimeException("Database connection failed");
            }
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, course);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new SQLException("Inserting row failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateSql(String tableName, String name, String surname, String course, String id, String idValue) {
        String query = "UPDATE " + tableName + " SET Name = ?, Surname = ?, CourseCode = ? WHERE " + id + " = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            if (con == null) {
                throw new RuntimeException("Database connection failed");
            }
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, course);
            pstmt.setString(4, idValue);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new SQLException("Updating row failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean controlCourseTable(String txt){
        boolean res =false;
        String query = "SELECT * FROM courses WHERE CourseName LIKE ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            if (con == null) {
                throw new RuntimeException("Database connection failed");
            }
            pstmt.setString(1,"%" + txt + "%");
            try (ResultSet resultSet = pstmt.executeQuery()){
                res = resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public static String findCourseCode(String course) {
        String courseCode = null;
        String query = "Select CourseCode FROM courses WHERE CourseName = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, course);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    courseCode = resultSet.getString("CourseCode");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseCode;
    }

}

