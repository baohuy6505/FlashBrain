const IUserProgressRepository = require('../interfaces/IUserProgressRepository')
const UserProgressModel = require('../../domain/models/UserProgress');

class UserProgressRepository extends IUserProgressRepository {
    async getUserProgress(userId) {
        return await UserProgressModel.findById(userId);
    }

    async upsertProgress(userId, updateData) {
        return await UserProgressModel.findByIdAndUpdate(
            userId,
            { $set: updateData },
            { 
                new: true, 
                upsert: true, // NẾU CHƯA CÓ DOCUMENT NÀO CỦA USER NÀY -> TẠO MỚI LUÔN
                setDefaultsOnInsert: true 
            }
        );
    }
}

module.exports = new UserProgressRepository();