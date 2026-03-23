class IAuthService {
    async register(body) {
        throw new Error("Method not implemented");
    }
    async login(body) {
        throw new Error("Method not implemented");
    }
    // async getUserById(userId) {
    //     throw new Error("Method not implemented");
    // }
    // async updateUser(userId, userUpdate) {
    //     throw new Error("Method not implemented");
    // }
    // async deleteUser(userId) {
    //     throw new Error("Method not implemented");
    // }
}           

module.exports = IAuthService;