package game.servlets.api;

import com.google.gson.Gson;
import game.dao.RankDAO;
import game.dto.RankDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "RankApiServlet", value = "/api/rank")
public class RankApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. Nhận lệnh từ Frontend
        String filter = request.getParameter("filter");

        // Nhận thêm offset (mặc định là 0 nếu tải lần đầu)
        int offset = 0;
        try {
            if (request.getParameter("offset") != null) {
                offset = Integer.parseInt(request.getParameter("offset"));
            }
        } catch (NumberFormatException e) {
            offset = 0;
        }

        int limit = 10; // Giới hạn mỗi lần tải 10 người

        RankDAO rankDAO = new RankDAO();
        List<RankDTO> listRanks;

        // 2. NGÃ BA ĐƯỜNG: Lấy danh sách Top cao thủ
        if (filter == null || filter.equals("all")) {
            listRanks = rankDAO.getTopGlobalRanks(limit, offset);
        } else {
            try {
                int gameId = Integer.parseInt(filter);
                listRanks = rankDAO.getTopRanksByGame(gameId, limit, offset);
            } catch (NumberFormatException e) {
                listRanks = rankDAO.getTopGlobalRanks(limit, offset);
            }
        }

        // 3. LẤY THÔNG TIN HẠNG CỦA TÔI TỪ DATABASE THẬT
        String currentUsername = (String) request.getSession().getAttribute("account");
        RankDTO myCurrentRank = null;

        if (currentUsername != null) {
            if (filter == null || filter.equals("all")) {
                // Nếu đang xem Tất cả trò chơi
                myCurrentRank = rankDAO.getMyGlobalRank(currentUsername);
            } else {
                // Nếu đang xem một game cụ thể
                try {
                    int gameId = Integer.parseInt(filter);
                    myCurrentRank = rankDAO.getMyRankByGame(currentUsername, gameId);
                } catch (NumberFormatException e) {
                    myCurrentRank = rankDAO.getMyGlobalRank(currentUsername);
                }
            }
        }

        // 4. Đóng gói cả 2 dữ liệu vào Hộp chứa
        RankResponse responseData = new RankResponse(listRanks, myCurrentRank);

        // 5. Trả về chuẩn JSON
        Gson gson = new Gson();
        String jsonResult = gson.toJson(responseData);

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResult);
        out.flush();
    }
}

// Class này dùng để gom 2 loại dữ liệu lại thành 1 gói JSON duy nhất
class RankResponse {
    List<RankDTO> topRanks;
    RankDTO myRank;

    public RankResponse(List<RankDTO> topRanks, RankDTO myRank) {
        this.topRanks = topRanks;
        this.myRank = myRank;
    }
}