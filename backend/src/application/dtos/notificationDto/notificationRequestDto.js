class NotificationRequestDto {
    constructor(data) {
        this.time = data.time; // Ví dụ: "20:30"
        // Cho phép gửi lên hoặc lấy mặc định nếu không gửi
        this.title = data.title || "Đến giờ học rồi bạn ơi!"; 
        this.message = data.message || "Vào FlashBrain ôn tập để giữ chuỗi Streak nào!";
    }

    async validate() {
        if (!this.time) {
            throw new Error("Vui lòng cung cấp thời gian nhận thông báo (time).");
        }
        
        const timeRegex = /^([01]\d|2[0-3]):([0-5]\d)$/;
        if (!timeRegex.test(this.time)) {
            throw new Error("Thời gian không hợp lệ. Vui lòng dùng định dạng HH:mm (ví dụ: 08:30 hoặc 20:00)");
        }
    }
}

module.exports = NotificationRequestDto;