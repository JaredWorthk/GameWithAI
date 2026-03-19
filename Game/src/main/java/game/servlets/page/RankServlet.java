package game.servlets.page;

import game.dao.GameDAO;
import game.dto.GameDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "RankServlet", value = "/rank")
public class RankServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. LẤY DỮ LIỆU ĐỔ VÀO DROPDOWN
        GameDAO gameDAO = new GameDAO();
        request.setAttribute("listGames", gameDAO.getAllGames());

        // 2. QUY TẮC RENDER KÉP (Phân biệt trình duyệt F5 hay AJAX gọi)
        String requestedWith = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        if (isAjax) {
            // Trường hợp 1: Chuyển trang mượt mà bằng AJAX -> Ném trả đúng khúc ruột Rank.jsp (Dùng forward)
            request.getRequestDispatcher("/JSP/JSP-User/share/Rank.jsp").forward(request, response);
        } else {
            // Trường hợp 2: Người dùng ấn F5 hoặc gõ thẳng Link -> Lấy Khung xương Main-content, nhét ruột Rank vào
            request.setAttribute("fragmentPath", "/JSP/JSP-User/share/Rank.jsp");
            request.getRequestDispatcher("/JSP/JSP-User/Main-content.jsp").forward(request, response);
        }
    }
}