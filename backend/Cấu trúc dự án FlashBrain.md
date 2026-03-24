### Thư mục gốc: FlashBrain-Workspace

Plaintext

```
FlashBrain-Workspace/          # Thư mục gốc chứa toàn bộ dự án
│
├── .gitignore                 # Bỏ qua các file build của Android và node_modules của Node.js
├── README.md                  # File báo cáo markdown cực kỳ quan trọng cho điểm số (Chứa API docs, DB Schema)
│
├── FlashBrain-Android/        # 📱 CLIENT: Ứng dụng Mobile (Kotlin + Jetpack Compose)
│   ├── app/src/main/java/com/developer/flashbrain/
│   │   ├── /              # Các thành phần dùng chung (Utils, Constants, Network Result)
│   │   ├── di/                # Dependency Injection (Hilt - AppModule, NetworkModule, DatabaseModule)
│   │   ├── domain/            # TẦNG LÕI (Không phụ thuộc Android framework)
│   │   │   ├── model/         # Entities thuần túy (User, Deck, Flashcard)
│   │   │   ├── repository/    # Interfaces (VD: IFlashcardRepository, IAuthRepository)
│   │   │   └── use_case/      # Logic nghiệp vụ (CalculateSM2UseCase, SyncDataUseCase)
│   │   ├── data/              # TẦNG DỮ LIỆU
│   │   │   ├── local/         # Chế độ Offline-first (Room DB: Dao, Entity, Database)
│   │   │   ├── remote/        # Giao tiếp Server (Retrofit: API Interfaces, DTOs hứng JSON)
│   │   │   └── repository/    # Triển khai Interface (Quyết định lấy data từ Local hay Remote)
│   │   ├── presentation/      # TẦNG GIAO DIỆN (MVVM + Jetpack Compose)
│   │   │   ├── theme/         # Colors, Typography, Shapes (Material Design)
│   │   │   ├── components/    # UI dùng lại (CustomButton, FlashcardItem, ProgressBar)
│   │   │   └── screens/       # Các màn hình (Home, Study, Auth - chứa file .kt và ViewModel)
│   │   └── worker/            # Background Tasks (DailyReminderWorker chạy bằng WorkManager)
│   │
│   ├── build.gradle.kts       # Khai báo thư viện Android (Compose, Room, Retrofit, Hilt...)
│   └── AndroidManifest.xml    # Cấp quyền Internet, định nghĩa Application
│
└── FlashBrain-Backend/        # ⚙️ SERVER: RESTful API (Node.js + Express + MongoDB)
    ├── src/
    │   ├── domain/            # TẦNG LÕI (Database Schemas)
    │   │   ├── models/        # Mongoose Schemas (User.js, Deck.js, Flashcard.js)
    │   │   └── exceptions/    # Custom Errors (NotFoundError.js, UnauthorizedError.js)
    │   │
    │   ├── application/       # TẦNG NGHIỆP VỤ (Xử lý Logic)
    │   │   ├── services/      # Xử lý luồng dữ liệu (AuthService.js, DeckService.js, SyncService.js)
    │   │   └── utils/         # Helper functions (jwt.js, bcrypt.js, sm2Algorithm.js)
    │   │
    │   ├── infrastructure/    # TẦNG HẠ TẦNG (Kết nối Database)
    │   │   ├── database/      # dbConnect.js (Code kết nối MongoDB Atlas)
    │   │   └── repositories/  # Thao tác trực tiếp DB (DeckRepository.js)
    │   │
    │   ├── presentation/      # TẦNG GIAO DIỆN API (Endpoints)
    │   │   ├── controllers/   # Hứng Request, gọi Service, trả Response (AuthController.js, SyncController.js)
    │   │   ├── routes/        # Định nghĩa Endpoints (authRoutes.js, deckRoutes.js)
    │   │   └── middlewares/   # Chặn/Kiểm tra Request (authMiddleware.js, errorHandler.js)
    │   │
    │   └── app.js             # File khởi tạo Express, gắn các Routes và Middlewares
    │
    ├── .env                   # Biến môi trường: MONGO_URI, JWT_SECRET, PORT (KHÔNG PUSH lên GitHub)
    ├── package.json           # Khai báo thư viện Node.js (express, mongoose, jsonwebtoken, cors...)
    └── server.js              # Entry point: Nơi gọi app.listen() để chạy server
```

### Cách luồng code chạy xuyên suốt 2 hệ thống:

1. **Người dùng đăng nhập trên Android:** File `AuthScreen.kt` gọi `AuthViewModel`.
    
2. `AuthViewModel` gọi `LoginUseCase` ở tầng **domain**.
    
3. `LoginUseCase` yêu cầu `AuthRepository` (ở tầng **data**) gọi api.
    
4. Lớp **remote** (Retrofit) bắn một request `POST` có chứa JSON lên endpoint `/api/auth/login`.
    
5. Phía Backend, `authRoutes.js` bắt được request này, chuyển cho `AuthController.js`.
    
6. `AuthController` lấy body, đưa cho `AuthService.js` để đối chiếu mật khẩu đã băm (hash).
    
7. `AuthService` gọi `UserRepository` để tìm User trong **MongoDB Atlas**.
    
8. Tìm thấy, `AuthService` sinh ra một chuỗi JWT, trả ngược về Controller, Controller `.json()` về lại cho Retrofit bên Android.
    
9. Android nhận Token, lưu vào máy (Shared Preferences hoặc DataStore) và chuyển sang màn hình Home.
    

Cấu trúc này cực kỳ ăn khớp với Rubric chấm điểm: Phân tách module tốt, Backend xử lý gọn gàng qua API, và dễ dàng quản lý commit trên nhánh `feature/...`.

Bạn có muốn chúng ta đi sâu vào file `Flashcard.js` (Mongoose Schema) của Backend và `FlashcardEntity.kt` (Room DB) của Android để xem hai bên này lưu trữ cấu trúc từ vựng giống và khác nhau như thế nào không?