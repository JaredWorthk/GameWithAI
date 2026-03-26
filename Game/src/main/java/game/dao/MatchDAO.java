package game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MatchDAO {

    /**
     * Hàm 1: Lưu kết quả ván đấu và cộng dồn điểm (Sử dụng Transaction an toàn 100%)
     *
     * @param userId      ID của người chơi
     * @param gameId      ID của trò chơi (VD: 1 cho Cờ Vua, 2 cho Rắn săn mồi)
     * @param scoreEarned Số điểm kiếm được trong ván này
     * @param result      Kết quả ván đấu ("Win", "Loss", "Draw")
     * @return true nếu ghi thành công, false nếu có lỗi mạng/database
     */
    public boolean saveMatchResult(int userId, int gameId, int scoreEarned, String result) {
        Connection conn = null;
        PreparedStatement psHistory = null;
        PreparedStatement psUser = null;
        boolean isSuccess = false;

        try {
            conn = DBContext.getConnection();

            // TẮT CHẾ ĐỘ LƯU TỰ ĐỘNG - Bắt đầu Giao dịch (Transaction)
            conn.setAutoCommit(false);

            // Nhịp 1: Ghi vào sổ lịch sử (Bảng MatchHistory)
            String sqlHistory = "INSERT INTO matchhistory (user_id, game_id, score, result) VALUES (?, ?, ?, ?)";
            psHistory = conn.prepareStatement(sqlHistory);
            psHistory.setInt(1, userId);
            psHistory.setInt(2, gameId);
            psHistory.setInt(3, scoreEarned);
            psHistory.setString(4, result);
            psHistory.executeUpdate();

            // Nhịp 2: Cộng dồn điểm vào tài khoản chính (Bảng Users)
            String sqlUser = "UPDATE users SET total_score = total_score + ? WHERE user_id = ?";
            psUser = conn.prepareStatement(sqlUser);
            psUser.setInt(1, scoreEarned);
            psUser.setInt(2, userId);
            psUser.executeUpdate();

            // CHỐT SỔ - Lưu cả 2 nhịp vào Database
            conn.commit();
            isSuccess = true;

        } catch (Exception e) {
            System.err.println("Lỗi Transaction khi lưu điểm: " + e.getMessage());
            try {
                // QUAY NGƯỢC THỜI GIAN - Nếu lỗi ở bất kỳ nhịp nào, hủy bỏ toàn bộ
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Đã Rollback an toàn, dữ liệu không bị lệch.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // DỌN DẸP TÀI NGUYÊN
            try {
                if (psHistory != null) psHistory.close();
                if (psUser != null) psUser.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                    conn.close(); // Trả kết nối về Pool (HikariCP)
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * Hàm 2: Lấy Kỷ lục điểm số cao nhất của 1 User trong 1 Game cụ thể
     *
     * @param userId ID của người chơi
     * @param gameId ID của trò chơi
     * @return Điểm số cao nhất từng đạt được (Nếu chưa chơi bao giờ thì trả về 0)
     */
    public int getPersonalHighScore(int userId, int gameId) {
        int highScore = 0;
        // Tìm điểm (score) to nhất (MAX) của người này trong game này
        String sql = "SELECT MAX(score) as max_score FROM matchhistory WHERE user_id = ? AND game_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, gameId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    highScore = rs.getInt("max_score");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy kỷ lục cá nhân: " + e.getMessage());
        }
        return highScore;
    }
}