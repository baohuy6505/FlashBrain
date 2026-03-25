class IUserRepository {
    async create(user) {
        throw new Error("Method not implemented");
    }
    async findByEmail(email) {
        throw new Error("Method not implemented");
    }   
    async findById(userId) {
        throw new Error("Method not implemented");
    }
    async updateUser(userId, userUpdate) {
        throw new Error("Method not implemented"); //cập nhật tên, ảnh, mật khẩu, subscriptionType
    }
    async deleteUser(userId) {
        throw new Error("Method not implemented");//xóa tài khoản
    }
}

module.exports = IUserRepository; 