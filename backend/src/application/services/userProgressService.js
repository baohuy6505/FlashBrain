const IUserProgressService = require('../interfaces/IUserProgressService')
const userProgressRepository = require('../../infrastructure/repositories/userProgressRepository');

class UserProgressService extends IUserProgressService {

    // Hàm phụ trợ: Xóa phần giờ/phút/giây, chỉ giữ lại Ngày/Tháng/Năm để so sánh cho chuẩn
    _normalizeDate(date) {
        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
    }

    async markStudyToday(userId) {
        const progress = await userProgressRepository.getUserProgress(userId);
        const today = this._normalizeDate(new Date());

        let newStreak = 1;
        
        // NẾU USER ĐÃ CÓ DATA PROGRESS TỪ TRƯỚC
        if (progress && progress.last_study_date) {
            const lastStudyDate = this._normalizeDate(progress.last_study_date);
            
            // Tính khoảng cách giữa hôm nay và ngày học cuối cùng (đơn vị: mili-giây)
            const diffTime = today.getTime() - lastStudyDate.getTime();
            const diffDays = diffTime / (1000 * 3600 * 24); // Đổi ra số ngày

            if (diffDays === 0) {
                // Trường hợp 1: Hôm nay đã điểm danh rồi -> Không làm gì cả
                return progress; 
            } else if (diffDays === 1) {
                // Trường hợp 2: Hôm qua có học -> Streak tăng thêm 1
                newStreak = progress.streak_days + 1;
            } else {
                // Trường hợp 3: Nghỉ quá 1 ngày -> Chuỗi bị đứt, làm lại từ đầu (Streak = 1)
                newStreak = 1;
            }
        }

        // Cập nhật lại vào DB: Lưu Streak mới và Cập nhật ngày học cuối là Hôm nay
        const updateData = {
            streak_days: newStreak,
            last_study_date: new Date() // Lưu thời gian thực tế lúc user bấm học
        };

        const updatedProgress = await userProgressRepository.upsertProgress(userId, updateData);
        return updatedProgress;
    }

    // Tiện tay viết luôn hàm Lấy thông tin Streak để hiển thị lên app
    async getMyProgress(userId) {
        let progress = await userProgressRepository.getUserProgress(userId);
        if (!progress) {
            // Nếu chưa có, trả về mặc định là 0
            progress = { streak_days: 0, total_learned: 0, last_study_date: null };
        }
        return progress;
    }
}

module.exports = new UserProgressService();