class INotificationService {
    async scheduleNotification(userId, dto) {
        throw new Error("Method not implemented");

    }

    async getMyPendingNotifications(userId) {
        throw new Error("Method not implemented");
    }
}

module.exports = INotificationService