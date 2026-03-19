// --- BẬT SÁNG THANH NAV DỰA THEO URL TRANG HIỆN TẠI VÀ KHI F5 ---
document.addEventListener("DOMContentLoaded", function () {
    // 1. Lấy URL hiện tại và tham số "page" (phát sinh khi bị ép F5 qua Trạm gác)
    const currentUrl = window.location.href;
    const urlParams = new URLSearchParams(window.location.search);
    const pageParam = urlParams.get('page'); // VD: Lấy ra chữ 'Rank', 'Home'...

    // 2. Gom tất cả các thẻ <a> trong thanh Navbar vào một mảng
    const navLinks = document.querySelectorAll('.header .navbar a');

    // Dọn dẹp: Tắt hết đèn của tất cả các nút trước khi kiểm tra để tránh chớp nháy
    navLinks.forEach(item => item.classList.remove('active'));

    let isMatched = false;

    // 3. Quét từng thẻ <a> một để tìm nút cần thắp sáng
    navLinks.forEach(link => {
        const href = link.getAttribute('href');

        // Bỏ qua nếu link là dấu #
        if (href === "#") return;

        // ĐIỀU KIỆN SÁNG ĐÈN 1 (KHI F5):
        // Nếu URL có chứa ?page=Rank, và href của thẻ <a> này cũng có chữ "Rank"
        // (Dùng toLowerCase để chuyển hết về chữ thường, tránh lỗi gõ hoa/thường)
        const matchByPageParam = pageParam && href.toLowerCase().includes(pageParam.toLowerCase());

        // ĐIỀU KIỆN SÁNG ĐÈN 2 (KHI CLICK BÌNH THƯỜNG):
        // Chuyển trang AJAX -> URL chứa chính xác đường dẫn của href
        const matchByUrl = currentUrl.includes(href);

        // Nếu thỏa mãn 1 trong 2 trường hợp trên, bật sáng nút đó và đánh dấu đã tìm thấy
        if (matchByPageParam || matchByUrl) {
            link.classList.add('active');
            isMatched = true;
        }
    });

    // 4. Trạm chốt cuối: ĐÃ ĐƯỢC NÂNG CẤP
    if (!isMatched && navLinks.length > 0) {
        // CHỈ bật Home mặc định nếu không có tham số page, hoặc tham số đó là 'Home'
        // Tránh tình trạng đang ở Profile (không có trên Navbar) mà lại ép Home sáng
        if (!pageParam || pageParam.toLowerCase() === 'home') {
            navLinks[0].classList.add('active');
        }
    }
});

// --- XỬ LÝ BẬT/TẮT MENU DROPDOWN CỦA USER ---
// 1. Hàm bật/tắt Menu khi click vào User Icon
function toggleUserMenu() {
    const userMenu = document.getElementById("userMenu");
    if (userMenu) {
        userMenu.classList.toggle("show");
    }
}

// 2. Đóng menu nếu người dùng lỡ click ra vùng trống ngoài màn hình
window.onclick = function (event) {
    // Kiểm tra xem nơi vừa click CÓ NẰM TRONG khối user-dropdown-wrapper không
    if (!event.target.closest('.user-dropdown-wrapper')) {
        const dropdown = document.getElementById("userMenu");
        // Nếu click ra ngoài và menu đang mở thì đóng nó lại
        if (dropdown && dropdown.classList.contains('show')) {
            dropdown.classList.remove('show');
        }
    }
}