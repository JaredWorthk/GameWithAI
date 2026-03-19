<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 30/01/2026
  Time: 6:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/CSS-User/share/Login.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
          integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
</head>

<body>
<div class="home-wrapper">
    <a href="<%=request.getContextPath()%>/JSP/JSP-User/share/Home.jsp" class="home-btn" title="Về trang chủ">
        <i class="fa-solid fa-house"></i>
    </a>
</div>
<div class="login-container">

    <form name="login" id="loginForm" class="login ${not empty showRegister ? 'hidden' : ''}"
          action="<%=request.getContextPath()%>/loginAuth" method="post">
        <h2>Đăng Nhập</h2>
        <p style="color: red; text-align: center;">${mess}</p>
        <div class="input-group">
            <input type="text" name="username" required>
            <label>Tên đăng nhập</label>
        </div>

        <div class="input-group">
            <input type="password" name="password" required>
            <label>Mật khẩu</label>
        </div>

        <button type="submit">Đăng Nhập</button>
        <div class="separator"><span>Hoặc</span></div>
        <%--        <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://10.163.26.20.nip.io:8080/Game_war_exploded/login-google&response_type=code&client_id=1041616980521-g6s310dvgn6o3kb0c08gfrmf6ch7votl.apps.googleusercontent.com&approval_prompt=force"--%>
        <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/Game_war_exploded/login-google&response_type=code&client_id=1041616980521-g6s310dvgn6o3kb0c08gfrmf6ch7votl.apps.googleusercontent.com&approval_prompt=force"
           class="btn-google">
            <img src="https://upload.wikimedia.org/wikipedia/commons/c/c1/Google_%22G%22_logo.svg" alt="Google logo">
            Đăng nhập với Google
        </a>
        <p style="text-align: center; font-size: 14px;">
            Chưa có tài khoản? <a onclick="toggleForms()" style="color: #03a9f4;">Đăng ký</a>
        </p>
    </form>
    <form name="register" id="registerForm" class="register ${not empty showRegister ? '' : 'hidden'}"
          action="<%=request.getContextPath()%>/register" method="post">
        <h2>Đăng Ký</h2>

        <div class="input-group">
            <input type="text" name="new_username" required>
            <label>Tên đăng nhập mới</label>
        </div>
        <div class="input-group">
            <input type="email" name="new_email" required>
            <label>Email</label>
        </div>
        <div class="input-group">
            <input type="password" name="new_password" required>
            <label>Mật khẩu</label>
        </div>

        <div class="input-group">
            <input type="password" name="confirm_password" required>
            <label>Nhập lại mật khẩu</label>
        </div>

        <button type="submit">Đăng Ký</button>
        <p style="text-align: center; font-size: 14px;">
            Đã có tài khoản? <a onclick="toggleForms()" style="color: #03a9f4;">Đăng nhập</a>
        </p>
    </form>

</div>
</body>
<script src="<%=request.getContextPath()%>/JS/JS-User/login/login.js"></script>
</html>
