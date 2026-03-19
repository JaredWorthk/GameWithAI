// --- CÁC BIẾN TOÀN CẦU CHO PHÂN TRANG ---
let currentOffset = 0;
let currentLimit = 10; // Khớp với limit trong Servlet
let currentFilter = 'all';

// Trái tim của trang Rank: Đã nâng cấp Hỗ trợ Phân trang và Bảo mật
function loadRankingData(filterValue, isLoadMore = false) {

    // Nếu là chọn Game mới (Không phải bấm Tải thêm) -> Reset lại từ đầu
    if (!isLoadMore) {
        currentOffset = 0;
        currentFilter = filterValue;
        $('#podium-container').addClass('fade-loading').empty();
        $('#rank-list-container').addClass('fade-loading').removeClass('auth-blur').empty();
        $('.login-overlay-msg').remove(); // Xóa biển báo Đăng nhập cũ
        $('.btn-load-more').hide();
    }

    $('#rank-loader').show();

    $.ajax({
        url: window.ctxPath + '/api/rank',
        method: 'GET',
        data: {filter: currentFilter, offset: currentOffset}, // Gửi thêm offset
        dataType: 'json',
        success: function (data) {
            $('#rank-loader').hide();

            let rankList = data.topRanks;
            let myRank = data.myRank;

            // --- 1. CẬP NHẬT THANH TRẠNG THÁI ---
            if (myRank) {
                $('.current-user-rank .rank-number').text(myRank.rankPosition);
                $('.current-user-rank .rank-score').text(myRank.score.toLocaleString() + ' pts');
                $('.current-user-rank h4').text(myRank.playerName);
                $('.current-user-rank .user-detail i').attr('class', 'fa-solid ' + myRank.avatarIcon);
            } else {
                $('.current-user-rank .rank-number').text("???");
                $('.current-user-rank .rank-score').text("Đăng nhập để xem");
                $('.current-user-rank h4').text("Chưa đăng nhập");
                $('.current-user-rank .user-detail i').attr('class', 'fa-solid fa-user-secret');
            }

            // --- 2. TRƯỜNG HỢP RỖNG DỮ LIỆU ---
            if (!rankList || rankList.length === 0) {
                if (!isLoadMore) {
                    $('#podium-container').removeClass('fade-loading');
                    $('#rank-list-container').removeClass('fade-loading')
                        .html('<p style="text-align:center; color:#b2b2bc; padding:50px 0;">Chưa có dữ liệu xếp hạng.</p>');
                } else {
                    $('.btn-load-more').hide(); // Hết người tải rồi thì giấu nút đi
                }
                return;
            }

            // --- 3. VẼ BỤC PODIUM (Chỉ vẽ ở Lần tải đầu tiên) ---
            if (!isLoadMore) {
                let podiumHtml = '';
                if (rankList.length > 1) podiumHtml += buildPodiumItem(rankList[1], 2);
                if (rankList.length > 0) podiumHtml += buildPodiumItem(rankList[0], 1);
                if (rankList.length > 2) podiumHtml += buildPodiumItem(rankList[2], 3);
                $('#podium-container').html(podiumHtml).removeClass('fade-loading');
            }

            // --- 4. VẼ DANH SÁCH (Top 4 trở đi, hoặc đắp nối tiếp) ---
            let listHtml = '';
            // Nếu tải lần đầu, bắt đầu vòng lặp từ i=3 (Bỏ 3 ông trên bục). Nếu tải thêm, vòng lặp từ i=0
            let startIndex = isLoadMore ? 0 : 3;

            for (let i = startIndex; i < rankList.length; i++) {
                let p = rankList[i];
                listHtml += `
                    <div class="rank-list-item">
                        <span class="rank-number">${p.rankPosition}</span>
                        <div class="rank-info">
                            <i class="fa-solid ${p.avatarIcon}"></i>
                            <h4>${p.playerName}</h4>
                        </div>
                        <span class="rank-score">${p.score.toLocaleString()} pts</span>
                    </div>
                `;
            }

            // Đắp HTML
            if (!isLoadMore) {
                $('#rank-list-container').html(listHtml).removeClass('fade-loading');
            } else {
                $('#rank-list-container').append(listHtml); // NỐI THÊM VÀO ĐUÔI
            }

            // --- 5. LOGIC LÀM MỜ VÀ NÚT TẢI THÊM ---
            if (!myRank) {
                // CHƯA ĐĂNG NHẬP: Ép làm mờ, hiện thông báo, giấu nút tải thêm
                $('#rank-list-container').addClass('auth-blur');
                if ($('.login-overlay-msg').length === 0) {
                    $('#rank-list-container').after(`
                        <div class="login-overlay-msg">
                            <h3>Nội dung VIP</h3>
                            <p>Đăng nhập để mở khóa danh sách Top cao thủ!</p>
                            <a href="${window.ctxPath}/JSP/JSP-User/share/Login.jsp">Đăng nhập ngay</a>
                        </div>
                    `);
                }
                $('.btn-load-more').hide();
            } else {
                // ĐÃ ĐĂNG NHẬP: Tính toán phân trang
                currentOffset += rankList.length; // Cộng dồn số người đã tải

                // Nếu mảng trả về bằng đúng limit, nghĩa là có thể DB vẫn còn người chưa tải hết
                if (rankList.length === currentLimit) {
                    $('.btn-load-more').show();
                } else {
                    $('.btn-load-more').hide(); // Cụt đuôi rồi, hết người
                }
            }
        },
        error: function (xhr) {
            // 👇 ĐÃ KHÔI PHỤC LẠI HÀM ERROR ĐỂ CHỐNG KẸT GIAO DIỆN
            console.error("Lỗi khi tải BXH:", xhr);
            $('#rank-loader').hide();
            if (!isLoadMore) {
                $('#podium-container').removeClass('fade-loading').empty();
                $('#rank-list-container').removeClass('fade-loading')
                    .html('<p style="text-align:center; color:#ff4d4d; padding:50px 0;">Lỗi kết nối máy chủ. Vui lòng thử lại!</p>');
            }
        }
    });
}

// Hàm hỗ trợ nặn bục Podium
function buildPodiumItem(player, rank) {
    let crown = rank === 1 ? '<i class="fa-solid fa-crown crown-icon"></i>' : '';
    return `
        <div class="podium-item top-${rank}">
            ${crown}
            <div class="avatar-wrap"><i class="fa-solid ${player.avatarIcon}"></i></div>
            <div class="rank-badge">#${rank}</div>
            <h3 class="player-name">${player.playerName}</h3>
            <p class="player-score">${player.score.toLocaleString()} pts</p>
        </div>
    `;
}

// HÀM TẢI THÊM ĐÃ ĐƯỢC THỨC TỈNH
function loadMoreRanks() {
    loadRankingData(currentFilter, true);
}

// Xuất hàm ra global để HTML gọi được
window.loadMoreRanks = loadMoreRanks;