class INotificationRepository {
    async create(notificationData) {
        throw new Error("Method not implemented");

    }

    async getPendingNotifications(userId) {
        throw new Error("Method not implemented");
    }
}

module.exports = INotificationRepository