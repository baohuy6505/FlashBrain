const notificationService = require('../../application/services/notificationService');
const NotificationRequestDto = require('../../application/dtos/notificationDto/notificationRequestDto');

class NotificationController {
    // API: Cài đặt giờ học hằng ngày
    async setDailyReminder(req, res) {
        try {
            const userId = req.user.userId;
            const dto = new NotificationRequestDto(req.body);
            await dto.validate();

            const setting = await notificationService.setDailyReminder(userId, dto);

            return res.status(200).json({
                success: true,
                message: "Đã lưu giờ thông báo hằng ngày",
                data: setting
            });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    // API: Xem cài đặt hiện tại
    async getMySetting(req, res) {
        try {
            const userId = req.user.userId;
            const setting = await notificationService.getMySetting(userId);

            return res.status(200).json({
                success: true,
                message: "Lấy cài đặt thông báo thành công",
                data: setting || { message: "Bạn chưa cài đặt giờ học" }
            });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }
}

module.exports = new NotificationController();