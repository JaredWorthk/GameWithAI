package game.servlets.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Dòng này cực kỳ quan trọng: Nó bảo ông bảo vệ này hãy đứng canh gác ở cửa thư mục /JSP/game/
// Bất cứ ai muốn vào chơi game (Chess, Snake...) đều phải bước qua đây.
@WebFilter(urlPatterns = {"/JSP/JSP-User/game/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Ép kiểu để dùng được các hàm của HTTP
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Lấy session hiện tại
        HttpSession session = req.getSession(false);

        // Kiểm tra xem session có tồn tại và có chứa "account" (đã login) không
        boolean isLoggedIn = (session != null && session.getAttribute("account") != null);

        if (isLoggedIn) {
            chain.doFilter(request, response);
        } else {
            // NẾU CHƯA LOGIN: Bắt "quay xe" về thẳng trang đăng nhập
            System.out.println("Access denide : " + req.getRequestURI());
            res.sendRedirect(req.getContextPath() + "/JSP/JSP-User/share/Login.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}