package game.dao;

import game.dto.RankDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RankDAO {

    // 1. LẤY BẢNG XẾP HẠNG TỔNG (TẤT CẢ TRÒ CHƠI)
    public List<RankDTO> getTopGlobalRanks(int limit, int offset) {
        List<RankDTO> list = new ArrayList<>();
        // Cập nhật SQL: Thêm OFFSET
        String sql = "SELECT user_id, display_name, username, avatar_icon, total_score " +
                     "FROM Users ORDER BY total_score DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset); // Truyền offset vào SQL
            ResultSet rs = ps.executeQuery();

            int currentRank = offset + 1;
            while (rs.next()) {
                // Ưu tiên dùng display_name, nếu chưa có thì dùng username
                String name = rs.getString("display_name");
                if (name == null || name.isEmpty()) {
                    name = rs.getString("username");
                }

                RankDTO rank = new RankDTO(
                        rs.getInt("user_id"),
                        name,
                        rs.getString("avatar_icon"),
                        rs.getInt("total_score")
                );
                rank.setRankPosition(currentRank++); // Java tự động đánh số thứ hạng
                list.add(rank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. LẤY BẢNG XẾP HẠNG THEO TỪNG GAME CỤ THỂ
    public List<RankDTO> getTopRanksByGame(int gameId, int limit, int offset) {
        List<RankDTO> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.display_name, u.username, u.avatar_icon, SUM(m.score) as game_score " +
                     "FROM Users u " +
                     "JOIN MatchHistory m ON u.user_id = m.user_id " +
                     "WHERE m.game_id = ? " +
                     "GROUP BY u.user_id, u.display_name, u.username, u.avatar_icon " +
                     "ORDER BY game_score DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, gameId);
            ps.setInt(2, limit);
            ps.setInt(3, offset); // Bổ sung param số 3
            ResultSet rs = ps.executeQuery();

            int currentRank = offset + 1;
            while (rs.next()) {
                String name = rs.getString("display_name");
                if (name == null || name.isEmpty()) {
                    name = rs.getString("username");
                }

                RankDTO rank = new RankDTO(
                        rs.getInt("user_id"),
                        name,
                        rs.getString("avatar_icon"),
                        rs.getInt("game_score")
                );
                rank.setRankPosition(currentRank++);
                list.add(rank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. LẤY HẠNG CỦA CHÍNH TÔI (TẤT CẢ TRÒ CHƠI)
    public RankDTO getMyGlobalRank(String username) {
        String sql = "SELECT user_id, display_name, username, avatar_icon, total_score, " +
                     "(SELECT COUNT(*) FROM Users u2 WHERE u2.total_score > u.total_score) + 1 AS rank_position " +
                     "FROM Users u WHERE username = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("display_name");
                if (name == null || name.isEmpty()) name = rs.getString("username");

                RankDTO rank = new RankDTO(
                        rs.getInt("user_id"), name, rs.getString("avatar_icon"), rs.getInt("total_score")
                );
                rank.setRankPosition(rs.getInt("rank_position"));
                return rank;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4. LẤY HẠNG CỦA CHÍNH TÔI (THEO TỪNG GAME CỤ THỂ)
    public RankDTO getMyRankByGame(String username, int gameId) {
        RankDTO myRank = null;
        int myScore = 0;

        // BƯỚC A: Lấy thông tin user và điểm của user đó trong game này
        String sqlUser = "SELECT u.user_id, u.display_name, u.username, u.avatar_icon, " +
                         "IFNULL((SELECT SUM(score) FROM MatchHistory WHERE user_id = u.user_id AND game_id = ?), 0) AS game_score " +
                         "FROM Users u WHERE u.username = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUser)) {

            ps.setInt(1, gameId);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                myScore = rs.getInt("game_score");
                String name = rs.getString("display_name");
                if (name == null || name.isEmpty()) name = rs.getString("username");

                myRank = new RankDTO(rs.getInt("user_id"), name, rs.getString("avatar_icon"), myScore);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // BƯỚC B: Đếm xem có bao nhiêu người điểm cao hơn mình trong game này
        if (myRank != null) {
            String sqlRank = "SELECT COUNT(*) + 1 AS rank_position FROM (" +
                             "  SELECT SUM(score) as sum_score FROM MatchHistory " +
                             "  WHERE game_id = ? GROUP BY user_id HAVING sum_score > ?" +
                             ") as higher_scorers";
            try (Connection conn = new DBContext().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlRank)) {

                ps.setInt(1, gameId);
                ps.setInt(2, myScore);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    myRank.setRankPosition(rs.getInt("rank_position"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myRank;
    }
}