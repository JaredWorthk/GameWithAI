<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    // TRẠM GÁC SPA
    String requestedWith = request.getHeader("X-Requested-With");
    Boolean isInternal = (Boolean) request.getAttribute("isInternalInclude");
    if (!"XMLHttpRequest".equals(requestedWith) && (isInternal == null || !isInternal)) {
        response.sendRedirect(request.getContextPath() + "/?page=Community");
        return;
    }
%>

<div class="community-container">

    <div class="comm-nav">
        <button class="comm-tab active" onclick="switchCommTab('tab-updates', this)">Cập Nhật</button>
        <button class="comm-tab" onclick="switchCommTab('tab-threads', this)">Cộng Đồng</button>
    </div>

    <div id="tab-updates" class="comm-content active">

        <div class="fb-post">
            <div class="fb-header">
                <div class="fb-avatar"><i class="fa-solid fa-gamepad"></i></div>
                <div class="fb-author">
                    <h4>Admin GameHub</h4>
                    <span>2 giờ trước • <i class="fa-solid fa-earth-americas"></i></span>
                </div>
            </div>
            <div class="fb-body">
                <p>🔥 BẢN CẬP NHẬT MÙA 2 ĐÃ CHÍNH THỨC RA MẮT! 🔥<br>
                    Chúng tôi đã thêm hệ thống rank mới cho Cờ Vua và sửa lỗi kết nối của Rắn Săn Mồi. Hãy vào game và trải nghiệm ngay nhé các kiện tướng!</p>
                <div class="fb-image">
                    <i class="fa-regular fa-image" style="font-size: 40px;"></i>
                </div>
            </div>
            <div class="fb-actions">
                <button class="fb-action-btn"><i class="fa-regular fa-thumbs-up"></i> Thích (1.2k)</button>
                <button class="fb-action-btn"><i class="fa-regular fa-comment"></i> Bình luận (45)</button>
                <button class="fb-action-btn"><i class="fa-solid fa-share"></i> Chia sẻ</button>
            </div>
        </div>

    </div>

    <div id="tab-threads" class="comm-content">

        <div class="thread-input-box">
            <div class="thread-avatar"><i class="fa-solid fa-user"></i></div>
            <input type="text" placeholder="Bắt đầu một cuộc thảo luận mới...">
            <button class="btn-post">Đăng</button>
        </div>

        <div class="thread-post">
            <div class="thread-left">
                <div class="thread-avatar"><i class="fa-solid fa-user-ninja"></i></div>
                <div class="thread-line"></div> </div>
            <div class="thread-right">
                <div class="thread-header">
                    <h4>ShadowHunter</h4>
                    <span>15 phút trước</span>
                </div>
                <div class="thread-body">
                    Có ai biết cách vượt qua mốc 3000 điểm trong Rắn Săn Mồi không? Tới khúc đó rắn chạy nhanh quá mình bấm không kịp 😭
                </div>
                <div class="thread-actions">
                    <i class="fa-regular fa-heart"></i>
                    <i class="fa-regular fa-comment"></i>
                </div>
            </div>
        </div>

        <div class="thread-post" style="border-bottom: none;">
            <div class="thread-left">
                <div class="thread-avatar"><i class="fa-solid fa-user-astronaut"></i></div>
            </div>
            <div class="thread-right">
                <div class="thread-header">
                    <h4>FakerVN</h4>
                    <span>5 phút trước</span>
                </div>
                <div class="thread-body">
                    @ShadowHunter Mẹo là bạn đừng cố ăn mồi sát tường nhé, cứ đi hình zigzag ở giữa map thôi.
                </div>
                <div class="thread-actions">
                    <i class="fa-regular fa-heart"></i>
                    <i class="fa-regular fa-comment"></i>
                </div>
            </div>
        </div>

    </div>

</div>

