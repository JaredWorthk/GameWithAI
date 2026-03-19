<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="header">
    <div class="logo-box">
        <h2>Game</h2>

    </div>

    <nav class="navbar">
        <a href="<%=request.getContextPath()%>/home">Trang chủ</a>
        <a href="<%=request.getContextPath()%>/rank">Xếp hạng</a>
        <a href="<%=request.getContextPath()%>/JSP/JSP-User/share/Community.jsp">Cộng đồng</a>
    </nav>

    <div class="user-info">
        <c:if test="${empty sessionScope.account}">
            <a href="<%=request.getContextPath()%>/JSP/JSP-User/share/Login.jsp">
                <button>Login</button>
            </a>
        </c:if>
        <c:if test="${not empty sessionScope.account}">
            <div class="user-dropdown-wrapper">
                <div class="user-icon" onclick="toggleUserMenu()">
                    <span><b>${sessionScope.account}</b></span>
                    <i class="fa-solid fa-user"></i>
                </div>

                <div class="dropdown-menu" id="userMenu">
                    <a href="<%=request.getContextPath()%>/JSP/JSP-User/user/User-info.jsp" class="menu-item">
                        <i class="fa-solid fa-id-card"></i> Thông tin cá nhân
                    </a>

                    <div class="divider"></div>

                    <a href="<%=request.getContextPath()%>/logout" class="menu-item logout-btn">
                        <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
                    </a>
                </div>

            </div>
        </c:if>
    </div>
</div>
