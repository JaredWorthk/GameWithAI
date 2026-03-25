<%
    // Ngay khi Tomcat vừa ghé thăm trang gốc, lập tức "đá" nó sang HomeServlet
    response.sendRedirect(request.getContextPath() + "/home");
%>