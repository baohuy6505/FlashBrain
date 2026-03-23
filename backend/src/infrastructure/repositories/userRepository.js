const IUserRepository = require('../interfaces/IUserRepository');
const User = require('../../domain/models/User');

class UserRepository extends IUserRepository {
    async createUser(userData) {
        const user = new User({
            _id: userData._id,
            email: userData.email,
            name: userData.name,
            passwordHash: userData.passwordHash,
            role: userData.role || 'USER',
            subscriptionType: userData.subscriptionType || 'FREE',
        });
        return await user.save();
    }
    
    async getUserByEmail(email) {
        return await User.findOne({ email: email, isDeleted: false });
    }
}

module.exports = new UserRepository();