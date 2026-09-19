SLDS REST API
=============

Backend Java Spring Boot cho Sign Language Detection System (Capstone Project 2).
Các nhóm user story trong tài liệu được ánh xạ thành REST API:

- Authentication: POST /api/auth/signup, POST /api/auth/signin
- User management and directory: GET/PUT/DELETE /api/users, GET /api/users/me, POST /api/users/invite
- Conversations and messages: /api/conversations và /messages
- Sign-language recognition: /api/recognition/text-to-sign, voice-to-sign, sign-to-text, sign-to-voice, train
- Video rooms: POST/GET /api/rooms, /join, /leave
- Real-time messaging transport: STOMP WebSocket endpoint /ws, broker /topic

Yêu cầu
-------
- JDK 17 trở lên
- Maven 3.9 trở lên

Chạy và đóng gói
-----------------
```text
mvn spring-boot:run
mvn clean package
java -jar target/slds-api-1.0.0.jar
```

API chạy mặc định tại http://localhost:8080.
Tài khoản mẫu: demo@slds.local / password.
Đăng nhập lấy token, sau đó gửi header `Authorization: Bearer <token>` cho các API cần xác thực.

Ví dụ
-----
```http
POST /api/auth/signin
Content-Type: application/json

{"email":"demo@slds.local","password":"password"}
```

Thiết kế hiện tại dùng bộ nhớ tiến trình để có thể chạy demo nhanh. Khi triển khai thật,
cần thay SldsService bằng repository/database, mã hóa password, JWT và kết nối dịch vụ
nhận dạng/voice/video thực tế.