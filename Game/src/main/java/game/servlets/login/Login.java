package game.servlets.login;

import game.dao.LoginDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "Login", value = "/loginAuth")
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("JSP/share/Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginDAO dao = new LoginDAO();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Gọi hàm tối ưu: Vừa check đăng nhập, vừa lấy ID
        int userId = dao.loginUser(username, password);

        if (userId > 0) { // Nếu ID > 0 tức là đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("account", username);
            session.setAttribute("userId", userId); // Cấp thẻ ID ngay lập tức để chơi game

            // Xóa chữ JSP/ ở đầu đi cho chuẩn đường dẫn Context Path
            response.sendRedirect(request.getContextPath() + "/JSP/JSP-User/Main-content.jsp");
        } else {
            request.setAttribute("mess", "Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("username", username); // Giữ lại tên cho User đỡ phải gõ lại
            // Trả về lại trang Login thay vì Main-content
            request.getRequestDispatcher("/JSP/JSP-User/share/Login.jsp").forward(request, response);
        }
    }
}