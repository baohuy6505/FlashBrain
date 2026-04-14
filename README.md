# FlashBrain - Hệ thống học tập thông minh với Flashcards

**FlashBrain** là ứng dụng hỗ trợ ghi nhớ kiến thức dựa trên phương pháp **Spaced Repetition (Lặp lại ngắt quãng)** sử dụng thuật toán **SM-2**. Ứng dụng tích hợp hệ thống đồng bộ đám mây giúp người dùng học tập mọi lúc mọi nơi.

-----

## 🚀 Tính năng chính

### 📱 Android App

  * **Học tập thông minh:** Áp dụng thuật toán SM-2 để tối ưu hóa thời gian ôn tập.
  * **Quản lý bộ thẻ (Deck):** Tạo, sửa, xóa các bộ thẻ theo chủ đề.
  * **Thống kê & Streak:** Theo dõi tiến độ học tập hàng ngày qua biểu đồ tuần và chuỗi ngày học tập (Streak).
  * **Chế độ Offline:** Học tập không cần mạng nhờ cơ sở dữ liệu local (Room DB).
  * **Đồng bộ:** Tự động đồng bộ dữ liệu với Server khi có kết nối mạng.

### 🌐 Backend Server

  * **RESTful API:** Cung cấp các Endpoint cho User, Decks và Progress.
  * **Bảo mật:** Xác thực người dùng qua JWT (JSON Web Token).
  * **Lưu trữ:** Kết nối MongoDB Atlas và Cloudinary (để lưu ảnh thẻ).

-----

## 🛠 Công nghệ sử dụng

### Mobile (Android)

  * **Ngôn ngữ:** Kotlin
  * **UI:** Jetpack Compose (Modern Toolkit)
  * **Kiến trúc:** MVVM + Clean Architecture
  * **DI:** Dagger Hilt
  * **Networking:** Retrofit & OkHttp
  * **Local DB:** Room Database
  * **Async:** Kotlin Coroutines & Flow

### Backend

  * **Runtime:** Node.js (Express)
  * **Database:** MongoDB Atlas
  * **Containerization:** Docker & Docker Compose

-----

## 🏗 Kiến trúc hệ thống (MVVM)

Dự án tuân thủ nghiêm ngặt mô hình **MVVM** để đảm bảo tính dễ bảo trì và mở rộng:

1.  **View:** Sử dụng Jetpack Compose để lắng nghe các `StateFlow` từ ViewModel.
2.  **ViewModel:** Xử lý logic nghiệp vụ, quản lý trạng thái giao diện và giao tiếp với Repository.
3.  **Model (Data Layer):** Bao gồm Repository Pattern để điều phối dữ liệu giữa Local (Room) và Remote (API).

-----

## 🤖 Quy trình CI/CD

Hệ thống được thiết lập quy trình tự động hóa chuyên nghiệp:

  * **Android CI:** Sử dụng **GitHub Actions** tự động kiểm tra code và đóng gói file **APK (Debug)** mỗi khi có phiên bản mới trên nhánh `master`.
  * **Backend CD:** Kết nối trực tiếp với **Render**, tự động build Docker Image và Deploy mỗi khi code được cập nhật.

-----

## 📁 Cấu trúc thư mục

```text
FlashBrain/
├── android/               # Toàn bộ mã nguồn ứng dụng Android (Kotlin)
│   ├── app/
│   │   └── src/main/java/com/developer/flashbrain/
│   │       ├── data/      # Repository, DB, API
│   │       ├── domain/    # UseCase, Model, Repository Interface
│   │       └── presentation/ # UI (Compose), ViewModel, State
├── backend/               # Mã nguồn Server (Node.js)
│   ├── src/
│   ├── Dockerfile         # Cấu hình container hóa cho Render
│   └── package.json
└── .github/workflows/     # File cấu hình robot CI/CD cho GitHub
```

-----

## ⚙️ Hướng dẫn cài đặt

1.  **Clone dự án:**
    ```bash
    git clone https://github.com/baohuy6505/FlashBrain.git
    ```
2.  **Chạy Backend (Local):**
      * Vào folder `backend`, tạo file `.env` và cấu hình các biến `MONGODB_URI`, `JWT_SECRET`.
      * Chạy lệnh: `npm start`.
3.  **Chạy Android:**
      * Mở folder `android` bằng Android Studio.
      * Thay đổi `BASE_URL` trong `NetworkModule.kt` thành địa chỉ API của bạn.
      * Nhấn **Run** hoặc lấy APK từ mục **Actions** trên GitHub.

-----

## 🧠 Giải thích thuật toán SM-2

Thuật toán tính toán khoảng cách ôn tập tiếp theo ($I$) dựa trên độ dễ ($EF$) và đánh giá của người dùng ($q$ từ 0-5):

  * Nếu $q \ge 3$: $I(n) = I(n-1) \times EF$
  * Độ dễ mới: $EF' = EF + (0.1 - (5-q) \times (0.08 + (5-q) \times 0.02))$
