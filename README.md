# Nam Keep - Ứng dụng Ghi chú cho Android

![Platform](https://img.shields.io/badge/platform-Android-green.svg)
![MinAPI](https://img.shields.io/badge/API-26%2B-brightgreen.svg)
![Language](https://img.shields.io/badge/language-Java-orange.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

**Nam Keep** là một ứng dụng ghi chú và quản lý công việc đa chức năng được phát triển trên nền tảng Android. Lấy cảm hứng từ Google Keep, ứng dụng này được xây dựng nhằm mục đích thể hiện các kỹ năng phát triển ứng dụng Android hiện đại, từ thiết kế giao diện người dùng, kiến trúc phần mềm, đến việc tích hợp các thư viện phổ biến và tương tác với API.

## Giới thiệu

Trong thế giới kỹ thuật số bận rộn, việc ghi lại những ý tưởng, công việc và thông tin quan trọng một cách nhanh chóng là rất cần thiết. Nam Keep ra đời để giải quyết nhu cầu đó, cung cấp một giao diện trực quan, gọn gàng cùng với bộ tính năng mạnh mẽ giúp người dùng dễ dàng nắm bắt, tổ chức và tìm kiếm thông tin mọi lúc, mọi nơi.

## Demo & Hình ảnh

| Màn hình chính | Thêm ghi chú mới | Ghi chú dạng Checklist |
| :---: | :---: | :---: |
| <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/66fac5e2-d587-4986-9e1a-f074e2bad542" /> | <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/a4328f96-14eb-4856-a8e8-c99a0b613f9b" /> | <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/1ff44fd2-3c87-4e66-a874-6e94a89d4508" /> |

| Ghi chú dạng bản vẽ | Quản lý Nhãn | Tìm kiếm |
| :---: | :---: | :---: |
| <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/fe6616a9-d584-4b9d-8168-d1807ada23b0" /> | <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/a90e250b-90e1-4911-96ec-c9317da70b85" /> | <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/40602415-f9ac-44a2-97ec-74b57a44c286" /> |

| Menu | Thêm vào ghi chú | Thêm mau và ảnh nền |
| :---: | :---: | :---: |
| <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/dbfa33db-d4e7-4c46-97f3-11a838f8da71" /> | <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/34abfe01-3fb4-40c3-91b1-804f3c5a2dc2" /> | <img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/002f329e-1a42-484a-b242-3a913431680d" /> |


## Tính năng nổi bật

Ứng dụng được trang bị đầy đủ các tính năng cần thiết của một trình ghi chú hiện đại:

* **✍️ Tạo ghi chú đa dạng:**
    * Ghi chú văn bản đơn giản.
    * Ghi chú dạng danh sách kiểm tra (Checklist) để theo dõi công việc.
    * Ghi chú bằng bản vẽ tay hoặc chữ viết tay.
    * Ghi âm giọng nói.
    * Đính kèm hình ảnh từ thư viện hoặc máy ảnh.
* **🎨 Tổ chức và Tùy chỉnh:**
    * **Gán Nhãn (Labels):** Phân loại ghi chú theo các chủ đề khác nhau để dễ dàng quản lý.
    * **Mã màu:** Thay đổi màu nền của ghi chú để phân biệt và làm nổi bật.
    * **Kéo thả:** Kéo thả ghi chú quan trọng lên đầu danh sách.
* **🔔 Nhắc nhở và Lưu trữ:**
    * **Đặt lịch nhắc nhở:** Thiết lập thông báo cho các ghi chú quan trọng để không bỏ lỡ công việc.
    * **Lưu trữ (Archive):** Ẩn các ghi chú đã hoàn thành nhưng không muốn xóa.
    * **Thùng rác (Trash):** Xóa các ghi chú.
* **🔍 Tìm kiếm thông minh:** Tìm kiếm nhanh chóng ghi chú theo tiêu đề, nội dung, hoặc nhãn.
* **👤 Quản lý người dùng:** Hỗ trợ đăng ký, đăng nhập để đồng bộ hóa dữ liệu (qua API).
* **📱 Widget Màn hình chính:** Thêm ghi chú nhanh chóng ngay từ màn hình chính của điện thoại.

## Kiến trúc và Công nghệ sử dụng

Dự án được xây dựng dựa trên các công nghệ và kiến trúc phần mềm hiện đại nhằm đảm bảo tính ổn định, dễ bảo trì và mở rộng.

* **Ngôn ngữ:** **Java**
* **Kiến trúc:** **MVVM (Model-View-ViewModel)**
    * Sử dụng `ViewModel` để quản lý dữ liệu liên quan đến UI, tách biệt logic khỏi `Activity`/`Fragment`.
    * Sử dụng `LiveData` để xây dựng các thành phần UI theo hướng dữ liệu (data-driven) và nhận biết vòng đời (lifecycle-aware).
* **Giao diện người dùng (UI):**
    * **Material Design:** Tuân thủ các nguyên tắc thiết kế của Google để mang lại trải nghiệm người dùng nhất quán và hiện đại.
    * **ViewBinding:** Thay thế `findViewById` một cách an toàn và hiệu quả.
    * **RecyclerView:** Hiển thị danh sách ghi chú một cách hiệu quả và linh hoạt.
    * **ConstraintLayout:** Xây dựng layout phức tạp và đáp ứng (responsive).
    * **Navigation Component:** Quản lý luồng di chuyển giữa các màn hình một cách đơn giản.
    * **Lottie:** Tích hợp các animation đẹp mắt và mượt mà.
* **Networking:**
    * **Retrofit & Gson:** Thư viện mạnh mẽ để thực hiện các cuộc gọi API RESTful một cách dễ dàng và an toàn.
    * **Volley:** Một lựa chọn khác để xử lý các yêu cầu mạng.
* **Tải và hiển thị hình ảnh:**
    * **Glide:** Thư viện quản lý, tải và caching hình ảnh hiệu quả.
* **Xử lý bất đồng bộ:**
    * Sử dụng callbacks của Retrofit và các cơ chế của Android SDK để xử lý các tác vụ nền.
* **Các thư viện khác:**
    * **ButterKnife:** Giảm mã boilerplate cho việc binding view.
    * **Flexbox Layout:** Hiển thị danh sách nhãn một cách linh hoạt.
    * **WorkManager:** Lên lịch cho các tác vụ nền đáng tin cậy như gửi thông báo nhắc nhở.

## Cài đặt và Chạy dự án

Để chạy dự án trên máy của bạn, hãy làm theo các bước sau:

1.  **Clone repository:**
    ```bash
    git clone [https://github.com/hellonamdeptrai/Keep.git](https://github.com/hellonamdeptrai/Keep.git)
    ```
2.  **Mở bằng Android Studio:**
    * Mở Android Studio (phiên bản Chipmunk trở lên).
    * Chọn `File -> Open` và trỏ đến thư mục vừa clone.
3.  **Đồng bộ Gradle:**
    * Android Studio sẽ tự động tải và đồng bộ các dependencies đã được định nghĩa trong file `build.gradle`.
4.  **Cấu hình API (Nếu có):**
    * Dự án sử dụng Retrofit để kết nối đến API. Bạn cần đảm bảo backend server đang chạy và cập nhật địa chỉ IP/domain trong mã nguồn.
5.  **Chạy ứng dụng:**
    * Nhấn nút `Run 'app'` (Shift + F10) và chọn một máy ảo hoặc thiết bị thật để triển khai ứng dụng.

## Hướng phát triển trong tương lai

- [ ] Chuyển đổi dự án sang ngôn ngữ **Kotlin** và sử dụng **Coroutines** để xử lý bất đồng bộ.
- [ ] Áp dụng **Dependency Injection** với Hilt hoặc Dagger.
- [ ] Viết **Unit Test** và **UI Test** để đảm bảo chất lượng mã nguồn.
- [ ] Thêm tính năng chia sẻ ghi chú và cộng tác với người dùng khác.