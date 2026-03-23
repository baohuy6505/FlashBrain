class IUserRepository {
    async createUser(user) {
        throw new Error("Method not implemented");
    }
    async getUserByEmail(email) {
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

module.exports = IUserRepository;