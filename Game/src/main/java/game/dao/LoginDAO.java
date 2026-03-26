package game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
    /**
     * HÀM ĐĂNG KÝ: Nhận thêm Email và tự động set display_name
     */
    public boolean register(String username, String password, String email) {
        // Thêm cột email và display_name (lấy tạm username làm tên hiển thị)
        String query = "INSERT INTO users (username, password, email, display_name) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection(); // Sử dụng DBContext chung
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, username); // Gán display_name bằng username cho có dữ liệu

            int rowAffected = ps.executeUpdate();
            return rowAffected > 0; // Sửa lỗi ở đây: Lớn hơn 0 là thành công

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HÀM ĐĂNG NHẬP (CÁCH TỐI ƯU HƠN): Trả về luôn userId thay vì true/false
     */
    public int loginUser(String username, String password) {
        String query = "SELECT user_id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id"); // Thành công -> Trả về ID của người này
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Thất bại (Sai pass hoặc không có tài khoản) -> Trả về -1
    }

    public boolean checkEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        Connection connection = new DataConnection().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerNewGoogleAccount(String email, String username) {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, NULL)";
        try (Connection connection = new DataConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy user_id dựa vào email
     */
    public int getUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi lấy User ID: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

}
