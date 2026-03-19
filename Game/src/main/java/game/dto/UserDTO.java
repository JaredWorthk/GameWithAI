package game.dto;

import java.sql.Timestamp;

public class UserDTO {
    // Toàn bộ các cột trong bảng Users
    private int userId;
    private String username;
    private String password;
    private String email;
    private String displayName;
    private String role;
    private int totalScore;
    private String avatarIcon;
    private Timestamp createdAt;

    // 1. Constructor mặc định (Bắt buộc phải có để dùng với các Framework sau này)
    public UserDTO() {
    }

    // 2. Constructor ĐẦY ĐỦ THAM SỐ (Dùng khi cần lấy toàn bộ thông tin)
    public UserDTO(int userId, String username, String password, String email, String displayName,
                   String role, int totalScore, String avatarIcon, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.totalScore = totalScore;
        this.avatarIcon = avatarIcon;
        this.createdAt = createdAt;
    }

    // 3. Constructor cho trang XẾP HẠNG - RANK (Chỉ cần 5 thông số cốt lõi)
    public UserDTO(int userId, String username, String displayName, int totalScore, String avatarIcon) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.totalScore = totalScore;
        this.avatarIcon = avatarIcon;
    }

    // 4. Constructor cho trang HỒ SƠ CÁ NHÂN - USER INFO (Cần thêm Email và Role)
    public UserDTO(int userId, String username, String email, String displayName, String role, int totalScore, String avatarIcon) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.totalScore = totalScore;
        this.avatarIcon = avatarIcon;
    }

    // --- BẮT ĐẦU GETTER VÀ SETTER (Dùng phím tắt Alt+Insert trong IntelliJ để sinh tự động nếu thiếu) ---
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public String getAvatarIcon() { return avatarIcon; }
    public void setAvatarIcon(String avatarIcon) { this.avatarIcon = avatarIcon; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}