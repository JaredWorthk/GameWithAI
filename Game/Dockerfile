# Bước 1: Khởi tạo môi trường Maven + JDK 23 để build code
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
# Copy toàn bộ code vào trong container
COPY . .
# Chạy lệnh đóng gói thành file .war
RUN mvn clean package -DskipTests

# Bước 2: Khởi tạo môi trường Tomcat 11 để chạy web
FROM tomcat:11.0
# Xóa các app mặc định của Tomcat để tránh nặng server
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy file .war vừa build ở Bước 1 vào thư mục gốc của Tomcat và đổi tên thành ROOT.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng 8080
EXPOSE 8080
CMD ["catalina.sh", "run"]