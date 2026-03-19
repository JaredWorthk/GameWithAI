<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Game Hub - Trải nghiệm cực mượt</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/CSS-User/share/Header.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/CSS-User/share/Home.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/CSS-User/share/Rank.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/CSS-User/share/Community.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/CSS-User/user/User-info.css">
    <script>
        // Lấy tên thư mục dự án (Ví dụ: /Game_war_exploded)
        window.ctxPath = '<%=request.getContextPath()%>';
    </script>
    <style>
        /* Hiệu ứng chuyển cảnh */
        #main-content {
            transition: opacity 0.2s ease-in-out;
            opacity: 1;

            /* --- BỔ SUNG 4 DÒNG NÀY ĐỂ NỐI CẦU FLEXBOX --- */
            flex: 1; /* Chiếm toàn bộ phần không gian còn lại dưới Header */
            display: flex;
            flex-direction: column;
            overflow: hidden; /* Cấm khối này phình to ra khỏi màn hình */
        }

        #main-content.fade-out {
            opacity: 0;
        }
    </style>
</head>
<body>

<jsp:include page="share/Header.jsp"/>

<div id="main-content">
    <%
        // Nhận đường dẫn khúc ruột từ các Servlet (như HomeServlet, RankServlet) truyền sang
        String fragment = (String) request.getAttribute("fragmentPath");

        // NẾU KHÔNG CÓ RUỘT (Do người dùng vừa mở Server hoặc gõ bậy đường dẫn)
        if (fragment == null) {
            // Đá thẳng cổ về "Trạm bơm" HomeServlet để nó lo liệu từ A đến Z
            response.sendRedirect(request.getContextPath() + "/home");
            return; // Lệnh này cực kỳ quan trọng: Bắt Tomcat dừng vẽ ngay lập tức!
        }
    %>

    <jsp:include page="<%= fragment %>"/>
</div>

<script src="<%=request.getContextPath()%>/JS/JS-User/share/Router.js"></script>
<script src="<%=request.getContextPath()%>/JS/JS-User/share/Rank.js"></script>
<script src="<%=request.getContextPath()%>/JS/JS-User/user/User-info.js"></script>
<script src="<%=request.getContextPath()%>/JS/JS-User/share/Header.js"></script>
<script src="<%=request.getContextPath()%>/JS/JS-User/share/Community.js"></script>
</body>
</html>