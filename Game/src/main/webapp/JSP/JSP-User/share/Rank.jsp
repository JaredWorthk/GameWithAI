<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="page-transition" id="rank-content">
    <div class="rank-container">

        <div class="rank-header">
            <h1 class="rank-title"><i class="fa-solid fa-trophy"></i> Bảng Vàng Vinh Danh</h1>

            <div class="custom-dropdown" id="game-filter-dropdown">
                <input type="hidden" id="game-filter" value="all">

                <div class="dropdown-selected">
                    <span id="rank-dropdown-text">Tất cả Trò chơi</span>
                    <i class="fa-solid fa-chevron-down" id="rank-dropdown-icon"></i>
                </div>

                <ul class="dropdown-options">
                    <li data-value="all">Tất cả Trò chơi</li>

                    <c:forEach items="${listGames}" var="g">
                        <li data-value="${g.gameId}">${g.gameName}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <div class="rank-scroll-area">

            <div class="podium" id="podium-container">
            </div>

            <div class="rank-list lazy-container" id="rank-list-container">
            </div>
            <div class="lazy-loading-spinner" id="rank-loader" style="display: none;">
                <i class="fa-solid fa-circle-notch fa-spin"></i> Đang tải thêm dữ liệu...
            </div>
            <button class="btn-load-more" onclick="loadMoreRanks()">Tải thêm BXH</button>

        </div>
        <div class="current-user-rank">
            <div class="rank-info">
                <span class="rank-number">${sessionScope.account != null ? "142" : "???"}</span>
                <div class="user-detail">
                    <i class="fa-solid fa-user-astronaut"></i>
                    <h4>${sessionScope.account != null ? sessionScope.account : "Chưa đăng nhập"}</h4>
                </div>
            </div>
            <span class="rank-score">${sessionScope.account != null ? "1,200 pts" : "0 pts"}</span>
        </div>

    </div>
</div>

<script>
    if (typeof loadRankingData === "function") {
        loadRankingData('all');
    }
</script>