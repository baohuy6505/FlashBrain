class IUserProgressRepository {
    async getUserProgress(userId) {
        throw new Error("Method not implemented");
    }
    
    async upsertProgress(userId, updateData) {
        throw new Error("Method not implemented");
    }
}

module.exports = IUserProgressRepository