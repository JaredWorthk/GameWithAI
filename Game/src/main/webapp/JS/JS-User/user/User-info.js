const modal = document.getElementById('passwordModal');

function switchTab(event, tabId) {
    // 1. Tìm tất cả các tab nội dung và Ẩn chúng đi
    const tabContents = document.querySelectorAll('.tab-content');
    tabContents.forEach(tab => {
        tab.classList.remove('active');
    });

    // 2. Tìm tất cả các nút bấm trên Nav và tắt sáng
    const navTabs = document.querySelectorAll('.nav-tab');
    navTabs.forEach(btn => {
        btn.classList.remove('active');
    });

    // 3. Bật sáng cái nút vừa được bấm
    event.currentTarget.classList.add('active');

    // 4. Hiển thị nội dung của Tab tương ứng
    document.getElementById(tabId).classList.add('active');
}

// Bật popup
function openModal() {
    modal.classList.add('show');
}

// Tắt popup
function closeModal() {
    modal.classList.remove('show');
}

// Bấm ra vùng đen xám bên ngoài cũng tự động đóng popup
window.onclick = function (event) {
    if (event.target == modal) {
        closeModal();
    }
}

// --- KHUNG LOGIC CHO SLIDER (Trượt ngang) ---
let currentSlide = 0;

function nextSlide(sliderId) {
    console.log("Chuẩn bị trượt sang phải cho slider: " + sliderId);
    // Logic tương lai: Tính toán chiều rộng của card và sửa style.transform = `translateX(...)`
}

function prevSlide(sliderId) {
    console.log("Chuẩn bị trượt sang trái cho slider: " + sliderId);
    // Logic tương lai: Tính toán lùi lại
}

// --- KHUNG LOGIC CHO LAZY LOAD (Cuộn dọc) ---
function loadMoreHistory() {
    // 1. Hiện con quay loading
    document.getElementById('history-loader').style.display = 'block';

    // 2. Ẩn nút "Xem thêm" đi
    event.target.style.display = 'none';

    console.log("Đang gọi API fetch thêm dữ liệu từ Database...");

    // 3. Giả lập quá trình đợi mạng (Timeout 1.5 giây)
    setTimeout(() => {
        console.log("Tải xong! Chèn thêm các thẻ <div class='history-item'> mới vào DOM.");
        // Code render HTML mới sẽ nằm ở đây...

        // 4. Tắt con quay loading và hiện lại nút "Xem thêm"
        document.getElementById('history-loader').style.display = 'none';
        event.target.style.display = 'block';
    }, 1500);
}

// --- HÀM SẮP XẾP THÀNH TỰU ---
function sortAchievements() {
    // 1. Lấy giá trị đang được chọn trong Dropdown
    const sortType = document.getElementById('achieve-sort').value;

    // 2. Tìm cái khung chứa các thẻ bài
    const track = document.getElementById('achieve-track');

    // 3. Lấy tất cả các thẻ bài gom vào 1 mảng (Array) để dễ sắp xếp
    const cards = Array.from(track.getElementsByClassName('achieve-card'));

    // 4. Bắt đầu thuật toán đổi chỗ
    cards.sort((a, b) => {
        if (sortType === 'rarity-desc') {
            // Sắp xếp theo Độ hiếm (Từ cao xuống thấp)
            return parseInt(b.getAttribute('data-rarity')) - parseInt(a.getAttribute('data-rarity'));
        } else if (sortType === 'level-desc') {
            // Sắp xếp theo Cấp độ (Từ cao xuống thấp)
            return parseInt(b.getAttribute('data-level')) - parseInt(a.getAttribute('data-level'));
        } else {
            // Về Mặc định
            return parseInt(a.getAttribute('data-default')) - parseInt(b.getAttribute('data-default'));
        }
    });

    // 5. Quét mảng đã sắp xếp và đẩy chúng lại vào màn hình HTML
    // (Hiệu ứng DOM tự động nhấc thẻ cũ ra và đặt thẻ mới vào đúng vị trí)
    cards.forEach(card => {
        track.appendChild(card);
    });
}

// 1. Hàm bật/tắt hiển thị cái Menu
function toggleDropdown() {
    document.getElementById('custom-sort-dropdown').classList.toggle('active');
}

// 2. Hàm xử lý khi người dùng chọn 1 mục
function selectOption(value, text) {
    // Đổi chữ hiển thị trên nút bấm
    document.getElementById('dropdown-text').innerText = text;

    // Gắn giá trị ngầm vào thẻ input ẩn
    document.getElementById('achieve-sort').value = value;

    // Đóng cái menu lại
    document.getElementById('custom-sort-dropdown').classList.remove('active');

    // Kích hoạt ngay hàm sắp xếp thuật toán cũ của bạn!
    sortAchievements();
}

// 3. Thông minh hơn: Đóng menu nếu người dùng click bừa ra chỗ khác ngoài màn hình
window.addEventListener('click', function (e) {
    const dropdown = document.getElementById('custom-sort-dropdown');
    // Nếu cú click chuột KHÔNG nằm trong phạm vi của dropdown
    if (dropdown && !dropdown.contains(e.target)) {
        dropdown.classList.remove('active');
    }
});