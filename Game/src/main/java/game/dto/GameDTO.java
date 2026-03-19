package game.dto;

public class GameDTO {
    private int gameId;
    private String gameName;
    private String category;
    private String status;
    private String thumbnailClass;
    private String folderName;

    public GameDTO() {}

    public GameDTO(int gameId, String gameName, String category, String status, String thumbnailClass, String folderName) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.category = category;
        this.status = status;
        this.thumbnailClass = thumbnailClass;
        this.folderName = folderName;
    }

    // --- GETTER & SETTER ---
    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getThumbnailClass() { return thumbnailClass; }
    public void setThumbnailClass(String thumbnailClass) { this.thumbnailClass = thumbnailClass; }

    public String getFolderName() { return folderName; }
    public void setFolderName(String folderName) { this.folderName = folderName; }
}