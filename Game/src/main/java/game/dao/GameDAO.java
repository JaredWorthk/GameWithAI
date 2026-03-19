package game.dao;

import game.dto.GameDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    /**
     * Lấy thông tin chi tiết của Game theo ID
     */
    public GameDTO getGameById(int id) {
        String sql = "SELECT * FROM Games WHERE game_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GameDTO(
                            rs.getInt("game_id"),
                            rs.getString("game_name"),
                            rs.getString("category"),
                            rs.getString("status"),
                            rs.getString("thumbnail_class"),
                            rs.getString("folder_name")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error to take game information: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy toàn bộ danh sách Game để hiển thị lên trang chủ
     */
    public List<GameDTO> getAllGames() {
        List<GameDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Games";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                GameDTO game = new GameDTO(
                        rs.getInt("game_id"),
                        rs.getString("game_name"),
                        rs.getString("category"),
                        rs.getString("status"),
                        rs.getString("thumbnail_class"),
                        rs.getString("folder_name")
                );
                list.add(game);
            }
        } catch (Exception e) {
            System.err.println("Error to take the game list: " + e.getMessage());
        }
        return list;
    }

    public List<String> getAllCategory() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT category FROM games";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("category");
                list.add(category);
            }
        } catch (Exception e) {
            System.err.println("Error to take the category list: " + e.getMessage());
        }
        return list;
    }

}