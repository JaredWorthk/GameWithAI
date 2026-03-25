package game.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBContext {
    private static HikariDataSource dataSource;

    // Khối static này chỉ chạy đúng 1 lần duy nhất khi Server Tomcat bật lên
    static {
        try {
            // Nạp Driver (Bắt buộc với một số bản Tomcat cũ)
            Class.forName("com.mysql.cj.jdbc.Driver");

            HikariConfig config = new HikariConfig();

            // THAY ĐỔI CÁC THÔNG TIN NÀY CHO KHỚP VỚI MÁY CỦA BẠN
            config.setJdbcUrl(System.getenv("DB_URL"));
            config.setUsername(System.getenv("DB_USER"));
            config.setPassword(System.getenv("DB_PASS")); // Mật khẩu MySQL của bạn

            // CẤU HÌNH TỐI ƯU CHO 500 CCU TRÊN LAPTOP
            config.setMaximumPoolSize(50); // Thuê sẵn 50 nhân viên. 500 người vào thì 50 người được phục vụ trước, 450 người xếp hàng chờ vài mili-giây.
            config.setMinimumIdle(10);     // Lúc rảnh rỗi nhất cũng giữ lại 10 nhân viên.
            config.setConnectionTimeout(30000); // Đợi 30 giây nếu hết người phục vụ, quá thời gian mới báo lỗi.
            config.setIdleTimeout(600000); // Cho nhân viên nghỉ ngơi nếu 10 phút không có khách.

            // Tối ưu hóa bộ nhớ đệm ngầm của MySQL Driver
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            System.out.println("✅ HikariCP Connection Pool started successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error to start Database Connection Pool!");
        }
    }

    // Bất cứ khi nào các trang JSP hay Servlet cần lấy dữ liệu, chỉ cần gọi hàm này
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) {

    }
}