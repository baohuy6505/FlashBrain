const userProgressService = require('../../application/services/userProgressService');

class UserProgressController {
    // API: Điểm danh học hôm nay
    async markStudyToday(req, res) {
        try {
            const userId = req.user.userId;
            const progress = await userProgressService.markStudyToday(userId);

            return res.status(200).json({
                success: true,
                message: "Đã cập nhật chuỗi ngày học thành công",
                data: progress
            });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    // API: Lấy thông tin quá trình học
    async getMyProgress(req, res) {
        try {
            const userId = req.user.userId;
            const progress = await userProgressService.getMyProgress(userId);

            return res.status(200).json({
                success: true,
                message: "Lấy tiến độ học tập thành công",
                data: progress
            });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }
}

module.exports = new UserProgressController();