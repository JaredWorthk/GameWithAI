document.addEventListener("DOMContentLoaded", () => {
    const mainContent = document.getElementById("main-content");
    const navLinks = document.querySelectorAll('.header .navbar a, .dropdown-menu a:not(.logout-btn)');

    navLinks.forEach(link => {
        link.addEventListener('click', async function (e) {
                // 1. CHUYỂN DÒNG NÀY LÊN ĐẦU TIÊN:
                // Khóa chặt mồm trình duyệt lại ngay lập tức, cấm tải lại trang trong mọi trường hợp!
                e.preventDefault();

                const targetUrl = this.getAttribute('href');

                // 2. Bây giờ mới kiểm tra: Nếu là dấu # hoặc đang đứng ở đúng trang đó rồi thì nghỉ, không làm gì cả
                if (targetUrl === '#' || window.location.pathname.includes(targetUrl)) {
                    return;
                }

                navLinks.forEach(item => item.classList.remove('active'));
                this.classList.add('active');
                const userMenu = document.getElementById("userMenu");
                if (userMenu && userMenu.classList.contains('show')) {
                    userMenu.classList.remove('show');
                }
                mainContent.classList.add('fade-out');

                try {
                    // Thêm 'headers' để trình duyệt nói cho Server biết: "Tôi là AJAX đây!"
                    const response = await fetch(targetUrl, {
                        headers: {
                            'X-Requested-With': 'XMLHttpRequest'
                        }
                    });
                    const htmlFragment = await response.text();

                    setTimeout(() => {
                        mainContent.innerHTML = htmlFragment;
                        mainContent.classList.remove('fade-out');
                        history.pushState(null, '', targetUrl);
                        if (targetUrl.includes('rank')) {
                            // Nếu vừa chuyển sang trang Rank, gọi API lấy dữ liệu ngay!
                            if (typeof loadRankingData === 'function') {
                                loadRankingData('all');
                            }
                        }
                    }, 200);

                } catch (error) {
                    console.error("Lỗi tải trang:", error);
                    // Nếu server lỗi thì mới cho phép tải lại trang kiểu cũ
                    window.location.href = targetUrl;
                }
            }
        )
        ;
    });

    // Xử lý nút Back/Forward của trình duyệt
    window.addEventListener('popstate', async () => {
        const url = window.location.pathname;
        mainContent.classList.add('fade-out');
        const response = await fetch(url);
        const htmlFragment = await response.text();
        setTimeout(() => {
            mainContent.innerHTML = htmlFragment;
            mainContent.classList.remove('fade-out');
        }, 200);
    });
    const initialUrl = window.location.pathname; // Đọc xem F5 xong đang đứng ở đâu

    // Nếu URL có chứa chữ 'rank' (nghĩa là đang F5 ở trang Xếp hạng)
    if (initialUrl.includes('rank')) {
        // Trì hoãn 1 chút xíu (100ms) để đảm bảo file Rank.js đã load xong các hàm
        setTimeout(() => {
            if (typeof loadRankingData === 'function') {
                loadRankingData('all');
            }
        }, 100);
    }
});

// CAMERA GIÁM SÁT: XỬ LÝ SỰ KIỆN CHO CÁC NÚT BỊ RENDER LẠI (EVENT DELEGATION)
document.addEventListener('click', function (e) {

    // 1. TÍNH NĂNG LỌC TRÒ CHƠI Ở TRANG HOME
    // Kiểm tra xem nơi người dùng vừa click có phải là cái nút ".cat-btn" không?
    if (e.target.closest('.categories.sidebar .cat-btn')) {
        const clickedBtn = e.target.closest('.cat-btn');

        // Lấy danh sách tất cả các nút hiện tại (dù là nút mới hay cũ)
        const allBtns = document.querySelectorAll('.categories.sidebar .cat-btn');

        // Đổi đèn (Active)
        allBtns.forEach(b => b.classList.remove('active'));
        clickedBtn.classList.add('active');

        // Lấy từ khóa lọc
        const selectedCategory = clickedBtn.textContent.trim().toLowerCase();
        const gameCards = document.querySelectorAll('.game-grid .game-card');

        // Quét và ẩn/hiện game
        gameCards.forEach(card => {
            const gameCatElement = card.querySelector('.game-cat');
            const gameCatText = gameCatElement ? gameCatElement.textContent.trim().toLowerCase() : "";

            if (selectedCategory === 'tất cả' || selectedCategory === gameCatText) {
                card.style.display = 'block';
            } else if (selectedCategory === 'xếp hạng') {
                card.style.display = 'none';
            } else {
                card.style.display = 'none';
            }
        });
    }

    // 2. TÍNH NĂNG MỞ/ĐÓNG DROPDOWN
    const clickedDropdownHeader = e.target.closest('.dropdown-selected');

    if (clickedDropdownHeader) {
        // Chỉ tìm cái menu con nằm bên trong đúng cái Dropdown vừa click
        const parentDropdown = clickedDropdownHeader.closest('.custom-dropdown');
        const optionsMenu = parentDropdown.querySelector('.dropdown-options');
        if (optionsMenu) {
            optionsMenu.classList.toggle('show');
        }
    } else {
        // Nếu click ra ngoài màn hình -> Tự động đóng TẤT CẢ các dropdown đang mở
        if (!e.target.closest('.custom-dropdown')) {
            document.querySelectorAll('.dropdown-options.show').forEach(menu => {
                menu.classList.remove('show');
            });
        }
    }

    // 3. TÍNH NĂNG CHỌN GAME TRONG DROPDOWN
    const clickedLi = e.target.closest('.dropdown-options li');
    if (clickedLi) {
        const filterValue = clickedLi.getAttribute('data-value');
        const filterName = clickedLi.textContent.trim();

        const parentDropdown = clickedLi.closest('.custom-dropdown');
        const dropdownText = parentDropdown.querySelector('#rank-dropdown-text');
        const hiddenInput = parentDropdown.querySelector('#game-filter');

        if (dropdownText && hiddenInput) {
            dropdownText.textContent = filterName;
            hiddenInput.value = filterValue;
        }

        // Đóng menu sau khi chọn
        clickedLi.closest('.dropdown-options').classList.remove('show');

        // GỌI AJAX
        if (typeof loadRankingData === 'function') {
            loadRankingData(filterValue);
        }
    }
});