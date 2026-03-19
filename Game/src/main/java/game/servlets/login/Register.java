package game.servlets.login;

import game.dao.LoginDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Register", value = "/register")
public class Register extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("new_username");
        String email = request.getParameter("new_email"); // Bổ sung hứng Email
        String password = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu nhập lại không khớp!");
            request.setAttribute("showRegister", "true");
            request.getRequestDispatcher("JSP/JSP-User/share/Login.jsp").forward(request, response);
            return;
        }

        LoginDAO dao = new LoginDAO();
        // Truyền thêm biến email vào hàm
        boolean isRegister = dao.register(username, password, email);

        if (isRegister) {
            request.setAttribute("mess", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("JSP/JSP-User/share/Login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký thất bại, Username hoặc Email đã tồn tại!");
            request.setAttribute("showRegister", "true");
            request.getRequestDispatcher("JSP/JSP-User/share/Login.jsp").forward(request, response);
        }
    }
}