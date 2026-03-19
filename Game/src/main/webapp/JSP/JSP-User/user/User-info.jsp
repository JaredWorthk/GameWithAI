<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // TRẠM GÁC
    String requestedWith = request.getHeader("X-Requested-With");
    Boolean isInternal = (Boolean) request.getAttribute("isInternalInclude");

    // Nếu KHÔNG có thẻ AJAX (bấm F5) VÀ CŨNG KHÔNG có Thẻ VIP (bị gọi lén)
    if (!"XMLHttpRequest".equals(requestedWith) && (isInternal == null || !isInternal)) {
        // Đá người dùng về trang gốc (để Tomcat tự gọi welcome-file)
        response.sendRedirect(request.getContextPath() + "/?page=Profile");
        return; // Dừng lại ngay!
    }
%>
<div class="profile-container">

    <div class="profile-header">
        <div class="avatar-lg">
            <i class="fa-solid fa-user-astronaut"></i></div>
        <div class="user-details">
            <h1>${sessionScope.account != null ? sessionScope.account : "Player Unknown"}</h1>
            <div class="achievements">
                <span class="badge badge-award"><i class="fa-solid fa-award"></i> Thành tựu: Tân binh</span>
                <span class="badge badge-game"><i class="fa-solid fa-ghost"></i> Kỷ lục: 9999 Điểm Snake</span>
            </div>
        </div>
    </div>

    <div class="profile-nav">
        <button class="nav-tab active" onclick="switchTab(event, 'tab-profile')">Hồ sơ cá nhân</button>
        <button class="nav-tab" onclick="switchTab(event, 'tab-achievements')">Thành tựu</button>
        <button class="nav-tab" onclick="switchTab(event, 'tab-history')">Lịch sử đấu</button>
    </div>

    <div class="profile-content">

        <div id="tab-profile" class="tab-content active">
            <form action="<%=request.getContextPath()%>/update-profile" method="post">
                <div class="form-group">
                    <label>Địa chỉ Email (Không thể thay đổi)</label>
                    <input type="email" value="google_email@example.com" readonly>
                </div>
                <div class="form-group">
                    <label>Tên hiển thị trong Game</label>
                    <input type="text" name="displayName" value="${sessionScope.account}">
                </div>
                <div class="form-group">
                    <label>Bảo mật tài khoản</label>
                    <div>
                        <button type="button" class="btn-action btn-outline" onclick="openModal()">
                            <i class="fa-solid fa-lock"></i> Đổi mật khẩu
                        </button>
                    </div>
                </div>
                <hr style="border: 0; border-top: 1px solid #444455; margin: 30px 0;">
                <button type="submit" class="btn-action">
                    <i class="fa-solid fa-floppy-disk"></i> Lưu Thay Đổi
                </button>
            </form>
        </div>

        <div id="tab-achievements" class="tab-content">
            <div class="section-header">
                <h2 class="section-title">Huy hiệu & Kỷ lục</h2>

                <div class="header-actions">
                    <div class="custom-dropdown" id="custom-sort-dropdown">
                        <input type="hidden" id="achieve-sort" value="default">

                        <div class="dropdown-selected" onclick="toggleDropdown()">
                            <span id="dropdown-text">Mặc định</span>
                            <i class="fa-solid fa-chevron-down" id="dropdown-icon"></i>
                        </div>

                        <ul class="dropdown-options">
                            <li onclick="selectOption('default', 'Mặc định')">Mặc định</li>
                            <li onclick="selectOption('rarity-desc', 'Độ hiếm (Giảm dần)')">Độ hiếm (Giảm dần)</li>
                            <li onclick="selectOption('level-desc', 'Cấp độ (Cao nhất)')">Cấp độ (Cao nhất)</li>
                        </ul>
                    </div>

                    <div class="slider-controls">
                        <button class="btn-slide" onclick="prevSlide('achieve-slider')"><i
                                class="fa-solid fa-chevron-left"></i></button>
                        <button class="btn-slide" onclick="nextSlide('achieve-slider')"><i
                                class="fa-solid fa-chevron-right"></i></button>
                    </div>
                </div>
            </div>

            <div class="slider-viewport" id="achieve-slider">
                <div class="achievement-grid sliding-track" id="achieve-track">

                    <div class="achieve-card unlocked" data-rarity="5" data-level="99" data-default="1">
                        <i class="fa-solid fa-trophy"></i>
                        <h4>Vua Rắn</h4>
                        <p>Đạt 5000 điểm trong Snake</p>
                        <span class="rarity-tag legendary">Legendary</span>
                    </div>

                    <div class="achieve-card unlocked" data-rarity="3" data-level="50" data-default="2">
                        <i class="fa-solid fa-chess-knight"></i>
                        <h4>Kiện Tướng</h4>
                        <p>Thắng 10 ván cờ liên tiếp</p>
                        <span class="rarity-tag epic">Epic</span>
                    </div>

                    <div class="achieve-card locked" data-rarity="4" data-level="0" data-default="3">
                        <i class="fa-solid fa-lock"></i>
                        <h4>Bất Bại</h4>
                        <p>Chơi 100 ván không thua</p>
                        <span class="rarity-tag mythic">Mythic</span>
                    </div>

                </div>
            </div>
        </div>

        <div id="tab-history" class="tab-content">
            <h2 class="section-title">Lịch sử trận đấu</h2>

            <div class="history-list lazy-container" id="history-container">
                <div class="history-item win">...</div>
                <div class="history-item loss">...</div>
            </div>

            <div class="lazy-loading-spinner" id="history-loader" style="display: none;">
                <i class="fa-solid fa-circle-notch fa-spin"></i> Đang tải thêm dữ liệu...
            </div>

            <button class="btn-load-more" onclick="loadMoreHistory()">Xem thêm lịch sử</button>
        </div>

    </div>

</div>

<div class="modal-overlay" id="passwordModal">
    <div class="modal-box">
        <h3>Đổi Mật Khẩu</h3>
        <form action="<%=request.getContextPath()%>/change-password" method="post">
            <div class="form-group">
                <label>Mật khẩu hiện tại</label>
                <input type="password" name="oldPassword" required placeholder="Nhập mật khẩu cũ...">
            </div>
            <div class="form-group">
                <label>Mật khẩu mới</label>
                <input type="password" name="newPassword" required placeholder="Nhập mật khẩu mới...">
            </div>
            <div class="form-group">
                <label>Xác nhận mật khẩu mới</label>
                <input type="password" name="confirmPassword" required placeholder="Nhập lại mật khẩu mới...">
            </div>
            <div class="modal-actions">
                <button type="button" class="btn-cancel" onclick="closeModal()">Hủy</button>
                <button type="submit" class="btn-action">Cập nhật</button>
            </div>
        </form>
    </div>
</div>
