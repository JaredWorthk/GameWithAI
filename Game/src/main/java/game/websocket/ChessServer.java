package game.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Khai báo đường dẫn kết nối WebSocket (Ví dụ: ws://localhost:8080/Project/chess)
@ServerEndpoint("/chess")
public class ChessServer {
    private static Session playerWhite = null;
    private static Session playerBlack = null;
    // Danh sách lưu trữ tất cả các trình duyệt (người chơi) đang kết nối
    private static Set<Session> allClients = Collections.synchronizedSet(new HashSet<Session>());

    private static String currentFen = "start";

    // Sự kiện khi có 1 người vừa mở trang web
    @OnOpen
    public void onOpen(Session session) throws IOException {
        allClients.add(session);
        String role = "";

        // 2. Role assignment logic
        if (playerWhite == null) {
            playerWhite = session;
            role = "w"; // w = white
        } else if (playerBlack == null) {
            playerBlack = session;
            role = "b"; // b = black
        } else {
            role = "spectator";
        }

        // 3. Send role assignment message
        String roleMessage = "{\"type\": \"role\", \"color\": \"" + role + "\", \"fen\": \"" + currentFen + "\"}";
        session.getBasicRemote().sendText(roleMessage);

        System.out.println("NEW CONNECTION: " + session.getId() + " | Role: " + role);
    }

    // Sự kiện khi Tổng đài nhận được 1 nước đi từ 1 người chơi
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Received move from " + session.getId() + ": " + message);
        try {
            JsonObject jsonMessage = JsonParser.parseString(message).getAsJsonObject();
            if (jsonMessage.has("fen")) {
                currentFen = jsonMessage.get("fen").getAsString(); // Lưu lại hình ảnh bàn cờ mới nhất
            }
        } catch (Exception e) {
            System.out.println("Loi doc JSON: " + e.getMessage());
        }

        // Phát lại tin nhắn nước đi cho những người khác
        for (Session client : allClients) {
            if (!client.equals(session) && client.isOpen()) {
                client.getBasicRemote().sendText(message);
            }
        }
    }

    // Sự kiện khi có người tắt web hoặc mất mạng
    @OnClose
    public void onClose(Session session) {
        allClients.remove(session);
        // 4. Free up the seat if a player disconnects
        if (session.equals(playerWhite)) {
            playerWhite = null;
            // When player out : reset the game -> currentFen = start;
             currentFen = "start";
            System.out.println("White player disconnected. White seat is now empty.");
        } else if (session.equals(playerBlack)) {
            playerBlack = null;
            System.out.println("Black player disconnected. Black seat is now empty.");
        } else {
            System.out.println("A spectator disconnected.");
        }
    }

    // Sự kiện khi có lỗi gián đoạn mạng
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}