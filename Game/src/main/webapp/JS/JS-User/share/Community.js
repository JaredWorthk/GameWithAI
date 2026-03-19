function switchCommTab(tabId, btnElement) {
    // Giấu tất cả các tab content
    document.querySelectorAll('.comm-content').forEach(tab => {
        tab.classList.remove('active');
    });
    // Tắt đèn tất cả các nút
    document.querySelectorAll('.comm-tab').forEach(btn => {
        btn.classList.remove('active');
    });

    // Bật tab được chọn
    document.getElementById(tabId).classList.add('active');
    btnElement.classList.add('active');
}