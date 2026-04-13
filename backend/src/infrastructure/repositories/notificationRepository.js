const INotificationRepository = require('../interfaces/INotificationRepository')
const NotificationModel = require('../../domain/models/Notification');
const { v4: uuidv4 } = require('uuid');

class NotificationRepository extends INotificationRepository {
    async upsertDailyNotification(userId, data) {
        return await NotificationModel.findOneAndUpdate(
            { user_id: userId },
            data,
            { 
                new: true, 
                upsert: true, 
                setDefaultsOnInsert: true 
            }
        );
    }

    async getMyNotificationSetting(userId) {
        return await NotificationModel.findOne({ user_id: userId });
    }
}

module.exports = new NotificationRepository();