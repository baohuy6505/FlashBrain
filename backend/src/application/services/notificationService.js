const INotificationService = require('../interfaces/INotificationService')
const notificationRepository = require('../../infrastructure/repositories/notificationRepository');
const { v4: uuidv4 } = require('uuid');

class NotificationService extends INotificationService {
    async setDailyReminder(userId, dto) {
        const updateData = {
            $set: {
                title: dto.title,
                message: dto.message,
                daily_time: dto.time,
                is_active: true
            },
            $setOnInsert: {
                _id: uuidv4()
            }
        };

        return await notificationRepository.upsertDailyNotification(userId, updateData);
    }

    // Hàm để user Bật/Tắt thông báo mà không cần đổi giờ
    async toggleNotification(userId, isActive) {
        return await notificationRepository.upsertDailyNotification(userId, { is_active: isActive });
    }

    async getMySetting(userId) {
        return await notificationRepository.getMyNotificationSetting(userId);
    }
}

module.exports = new NotificationService();