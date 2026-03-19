<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chơi Game - ${game.gameName}</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/CSS-User/game/Play.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
          integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
</head>
<body>

<div class="game-header">
    <div class="home-wrapper">
        <a href="<%=request.getContextPath()%>/JSP/JSP-User/Main-content.jsp" class="home-btn" title="Về trang chủ"
           target="_top">
            <i class="fa-solid fa-house"></i>
        </a>
    </div>

    <h2>${game.gameName}</h2>

    <div class="score-board">
        Kỷ lục của bạn: &nbsp;<span style="color: gold;">${highScore}</span>
    </div>
</div>

<iframe class="game-frame" src="<%=request.getContextPath()%>/GAME/${game.folderName}/index.html" frameborder="0"
        scrolling="no"></iframe>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
    // Lắng nghe tiếng "hét" từ cái game Iframe bên trong
    window.addEventListener("message", function (event) {

        // Kiểm tra xem có đúng là gói tin gửi điểm không
        if (event.data && event.data.type === 'SAVE_SCORE') {
            let finalScore = event.data.score;

            // THỰC HIỆN AJAX TẠI ĐÂY (Nơi JSP hoạt động hoàn hảo)
            $.ajax({
                url: "<%=request.getContextPath()%>/save-match",
                method: "POST",
                data: {
                    gameId: ${game.gameId}, // Đã hàn gắn lại thẻ EL của JSP không bị xuống dòng
                    score: finalScore,
                    result: "Win"           // Đã gom lại trên 1 dòng chuẩn chỉ
                },
                success: function (response) {
                    // Thông báo mượt mà khi lưu điểm thành công
                    console.log("Đã lưu kỷ lục: " + finalScore);
                    alert("Lưu điểm thành công! Điểm của bạn: " + finalScore); // Thêm alert để dễ test

                    // Tùy chọn: Tải lại trang để cập nhật cái Kỷ lục góc trên cùng bên phải
                    // setTimeout(() => window.location.reload(), 1000);
                },
                error: function (xhr) {
                    console.error("Lỗi lưu điểm: " + xhr.responseText);
                    alert("Lưu điểm thất bại: " + xhr.responseText);
                }
            });
        }
    });
</script>
</body>
</html>