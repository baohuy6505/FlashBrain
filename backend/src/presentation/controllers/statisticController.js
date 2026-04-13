const statisticService = require('../../application/services/satisticService');

class StatisticController {
    async getMyStatistics(req, res) {
        try {
            const userId = req.user.userId;
            const stats = await statisticService.getUserDashboardStats(userId);

            return res.status(200).json({
                success: true,
                message: "Lấy thống kê thành công",
                data: stats
            });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }
}

module.exports = new StatisticController();