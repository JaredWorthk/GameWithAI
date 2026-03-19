package game.servlets.game;

import game.dao.MatchDAO;
import game.dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "SaveMatchServlet", value = "/save-match")
public class SaveMatchServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. KIỂM TRA ĐĂNG NHẬP
        HttpSession session = request.getSession();

        // GIẢ ĐỊNH: Khi đăng nhập thành công, bạn đã lưu Object UserDTO hoặc userId vào Session.
        // Tùy vào code của bạn, hãy sửa lại dòng này cho khớp nhé!
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // Nếu chưa đăng nhập mà dám chơi, trả về lỗi 401 (Unauthorized)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Vui lòng đăng nhập để lưu điểm!");
            return;
        }

        try {
            // 2. LẤY DỮ LIỆU TỪ GAME GỬI LÊN
            int gameId = Integer.parseInt(request.getParameter("gameId"));
            int score = Integer.parseInt(request.getParameter("score"));
            String result = request.getParameter("result"); // "Win", "Loss", hoặc "Draw"

            // 3. GỌI DAO ĐỂ GHI VÀO DATABASE (Dùng Transaction)
            MatchDAO matchDAO = new MatchDAO();
            boolean isSaved = matchDAO.saveMatchResult(userId, gameId, score, result);

            // 4. TRẢ LỜI CHO JAVASCRIPT BÊN FRONTEND BIẾT KẾT QUẢ
            if (isSaved) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Lưu điểm thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Lỗi Database khi lưu điểm.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Dữ liệu gửi lên không hợp lệ.");
        }
    }
}