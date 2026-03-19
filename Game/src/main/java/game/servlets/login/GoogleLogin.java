package game.servlets.login;

import game.dao.LoginDAO;
import game.dto.GoogleDTO;
import game.services.login.GoogleUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "GoogleLogin", value = "/login-google")
public class GoogleLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/JSP/JSP-User/share/Login.jsp");
            return;
        }

        String accessToken = GoogleUtils.getToken(code);

        GoogleDTO googleUser = GoogleUtils.getUserInfo(accessToken);
        LoginDAO dao = new LoginDAO();
        if (!dao.checkEmail(googleUser.getEmail())) {
            dao.registerNewGoogleAccount(googleUser.getEmail(), googleUser.getName());
        }
        // Lấy số ID từ Database lên
        int userId = dao.getUserIdByEmail(googleUser.getEmail());
        HttpSession session = request.getSession();
        session.setAttribute("account", googleUser.getName());
        session.setAttribute("userId", userId);
        response.sendRedirect(request.getContextPath() + "/JSP/JSP-User/Main-content.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}