package game.servlets.page;

import game.dao.GameDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Đăng ký đường dẫn ngắn gọn, dễ nhớ
@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. Lấy dữ liệu Game từ Database (Lúc nào cũng phải làm)
        GameDAO gameDAO = new GameDAO();
        request.setAttribute("listGames", gameDAO.getAllGames());

        List<String> listCategories = gameDAO.getAllCategory();
        request.setAttribute("listCategories", listCategories);
        // 2. KIỂM TRA AI ĐANG GỌI CỬA?
        String requestedWith = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        if (isAjax) {
            // NẾU LÀ AJAX (JS Gọi): Chỉ ném trả lại đúng cái khúc ruột Home.jsp
            request.getRequestDispatcher("/JSP/JSP-User/share/Home.jsp").forward(request, response);
        } else {
            // NẾU LÀ TRÌNH DUYỆT (F5 hoặc Gõ thẳng URL):
            // - Bước A: Gói khúc ruột Home.jsp lại
            request.setAttribute("fragmentPath", "/JSP/JSP-User/share/Home.jsp");
            // - Bước B: Lôi cái Khung xương Main-content ra, nhét ruột vào rồi trả về toàn bộ
            request.getRequestDispatcher("/JSP/JSP-User/Main-content.jsp").forward(request, response);
        }
    }
}