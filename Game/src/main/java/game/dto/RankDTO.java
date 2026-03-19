package game.dto;

public class RankDTO {
    private int userId;
    private String playerName; // Lấy từ cột display_name (hoặc username)
    private String avatarIcon; // Lấy từ cột avatar_icon
    private int score;         // Lúc All thì là total_score, lúc Game thì là SUM(score)
    private int rankPosition;  // Thứ hạng (1, 2, 3...) - Sẽ do Java tự đánh số

    // Constructor rỗng
    public RankDTO() {
    }

    // Constructor đầy đủ
    public RankDTO(int userId, String playerName, String avatarIcon, int score) {
        this.userId = userId;
        this.playerName = playerName;
        this.avatarIcon = avatarIcon;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAvatarIcon() {
        return avatarIcon;
    }

    public void setAvatarIcon(String avatarIcon) {
        this.avatarIcon = avatarIcon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
    }
}