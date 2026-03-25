class IAuthService {
    async register(body) {
        throw new Error("Method not implemented");
    }
    async login(body) {
        throw new Error("Method not implemented");
    }
    async getProfile(userId) {
        throw new Error("Method not implemented");
    }
    async updateProfile(userId, updateDto) {
        throw new Error("Method not implemented");
    }
    async changePassword(userId, changePassDto) {
        throw new Error("Method not implemented");
    }
    async deleteMyAccount(userId, password) {
        throw new Error("Method not implemented");
    }
}           

module.exports = IAuthService;