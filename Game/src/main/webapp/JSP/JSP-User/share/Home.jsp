<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="container">
    <div class="hero-section">
        <h1>Welcome to <span>Game Hub</span></h1>
        <p>Chọn trò chơi yêu thích của bạn và bắt đầu ngay!</p>
    </div>
    <div class="main-layout">

        <div class="game-grid">
            <c:forEach items="${listGames}" var="g">

                <div class="game-card" style="${g.status == 'Coming_Soon' ? 'opacity: 0.6;' : ''}">

                    <div class="game-thumb ${g.thumbnailClass}"
                         style="${g.status == 'Coming_Soon' ? 'background: #333;' : ''}"></div>

                    <div class="game-info">
                        <h3 class="game-title">${g.gameName}</h3>
                        <span class="game-cat">${g.category}</span>

                        <c:choose>
                            <c:when test="${g.status == 'Active'}">
                                <a href="<%=request.getContextPath()%>/play?id=${g.gameId}" class="play-btn">Chơi
                                    Ngay</a>
                            </c:when>
                            <c:otherwise>
                                <a href="#" class="play-btn" style="background: #555; cursor: not-allowed;"
                                   onclick="return false;">Sắp ra mắt</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </c:forEach>
        </div>
        <div class="categories sidebar">
            <h3>Lọc Trò Chơi</h3>

            <button class="cat-btn active">Tất cả</button>

            <c:forEach items="${listCategories}" var="cat">
                <button class="cat-btn">${cat}</button>
            </c:forEach>
        </div>
    </div>
</div>
