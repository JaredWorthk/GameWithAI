    package game.servlets.game;

    import game.dao.GameDAO;
    import game.dao.MatchDAO;
    import game.dto.GameDTO;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;

    import java.io.IOException;

    @WebServlet(name = "PlayGameServlet", value = "/play")
    public class PlayGameServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // 1. Kiểm tra đăng nhập
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                // Nếu chưa đăng nhập, đá về trang Login
                response.sendRedirect(request.getContextPath() + "/JSP/JSP-User/share/Login.jsp");
                return;
            }

            try {
                // 2. Lấy ID của game mà user muốn chơi (Truyền từ URL, ví dụ: /play?id=2)
                int gameId = Integer.parseInt(request.getParameter("id"));

                // 3. Lấy thông tin chi tiết của Game
                GameDAO gameDAO = new GameDAO();
                GameDTO game = gameDAO.getGameById(gameId);

                if (game == null || "Coming_Soon".equals(game.getStatus())) {
                    response.getWriter().write("Game không tồn tại hoặc chưa ra mắt!");
                    return;
                }

                // 4. Lấy kỷ lục điểm số cao nhất của User này trong Game này
                MatchDAO matchDAO = new MatchDAO();
                int highScore = matchDAO.getPersonalHighScore(userId, gameId);

                // 5. Đóng gói dữ liệu ném sang trang JSP
                request.setAttribute("game", game);
                request.setAttribute("highScore", highScore);

                // 6. Chuyển hướng sang giao diện khung chơi game
                request.getRequestDispatcher("/JSP/JSP-User/game/Play.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                response.getWriter().write("Đường dẫn game không hợp lệ!");
            }
        }
    }