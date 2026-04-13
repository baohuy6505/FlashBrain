const cron = require('node-cron');
const NotificationModel = require('../../domain/models/Notification');

class NotificationJob {
    start() {
        console.log("🤖 Bot Thông Báo đã khởi động và đang canh giờ...");

        // Cú pháp '* * * * *' nghĩa là: Chạy hàm này mỗi 1 phút một lần
        cron.schedule('* * * * *', async () => {
            
            // 1. Lấy giờ hiện tại của Server (Ví dụ: 20:30)
            const now = new Date();
            // Hàm padStart(2, '0') để đảm bảo luôn có 2 chữ số (VD: "08:05" thay vì "8:5")
            const currentHour = String(now.getHours()).padStart(2, '0');
            const currentMinute = String(now.getMinutes()).padStart(2, '0');
            const currentTimeString = `${currentHour}:${currentMinute}`; 

            try {
                // 2. Tìm tất cả user cài đặt giờ học TRÙNG với giờ hiện tại
                const usersToNotify = await NotificationModel.find({ 
                    daily_time: currentTimeString, 
                    is_active: true 
                });

                if (usersToNotify.length > 0) {
                    console.log(`⏰ Đúng ${currentTimeString}! Tìm thấy ${usersToNotify.length} user cần đi học.`);
                    
                    // 3. Vòng lặp bắn thông báo
                    for (const user of usersToNotify) {
                        // TẠM THỜI CHÚNG TA SẼ CONSOLE.LOG (Vì chưa tích hợp Firebase)
                        console.log(`>> [TING TING] Gửi thông báo tới UserID: ${user.user_id}`);
                        console.log(`>> Tiêu đề: ${user.title} | Nội dung: ${user.message}`);
                        console.log("-------------------------------------------------");
                    }
                }
            } catch (error) {
                console.error("Lỗi khi chạy Bot Thông Báo:", error);
            }
        });
    }
}

module.exports = new NotificationJob();